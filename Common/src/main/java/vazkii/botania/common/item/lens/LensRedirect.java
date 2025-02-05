/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.lens;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.api.mana.IManaSpreader;
import vazkii.botania.common.block.tile.mana.IThrottledPacket;
import vazkii.botania.common.helper.MathHelper;

public class LensRedirect extends Lens {

	@Override
	public boolean collideBurst(IManaBurst burst, HitResult pos, boolean isManaBlock, boolean shouldKill, ItemStack stack) {
		BlockPos coords = burst.getBurstSourceBlockPos();
		Entity entity = burst.entity();
		if (!entity.level.isClientSide && pos.getType() == HitResult.Type.BLOCK
				&& coords.getY() != Integer.MIN_VALUE
				&& !((BlockHitResult) pos).getBlockPos().equals(coords)) {
			BlockEntity tile = entity.level.getBlockEntity(((BlockHitResult) pos).getBlockPos());
			if (tile instanceof IManaSpreader spreader) {
				if (!burst.isFake()) {
					Vec3 tileVec = Vec3.atCenterOf(tile.getBlockPos());
					Vec3 sourceVec = Vec3.atCenterOf(coords);

					AABB axis;
					VoxelShape collideShape = entity.level.getBlockState(coords).getCollisionShape(entity.level, coords);
					if (collideShape.isEmpty()) {
						axis = new AABB(coords, coords.offset(1, 1, 1));
					} else {
						axis = collideShape.bounds().move(coords);
					}

					if (!axis.contains(sourceVec)) {
						sourceVec = new Vec3(axis.minX + (axis.maxX - axis.minX) / 2, axis.minY + (axis.maxY - axis.minY) / 2, axis.minZ + (axis.maxZ - axis.minZ) / 2);
					}

					Vec3 diffVec = sourceVec.subtract(tileVec);
					Vec3 diffVec2D = new Vec3(diffVec.x, diffVec.z, 0);
					Vec3 rotVec = new Vec3(0, 1, 0);
					double angle = MathHelper.angleBetween(rotVec, diffVec2D) / Math.PI * 180.0;

					if (sourceVec.x < tileVec.x) {
						angle = -angle;
					}

					spreader.setRotationX((float) angle + 90F);

					rotVec = new Vec3(diffVec.x, 0, diffVec.z);
					angle = MathHelper.angleBetween(diffVec, rotVec) * 180F / Math.PI;
					if (sourceVec.y < tileVec.y) {
						angle = -angle;
					}
					spreader.setRotationY((float) angle);

					spreader.commitRedirection();
					if (spreader instanceof IThrottledPacket pkt) {
						pkt.markDispatchable();
					}
				}
			}
		}

		return shouldKill;
	}

}
