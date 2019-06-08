/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jun 7, 2014, 2:24:51 PM (GMT)]
 */
package vazkii.botania.common.block.tile;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.registries.ObjectHolder;
import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.common.lib.LibMisc;

public class TilePlatform extends TileCamo {
	@ObjectHolder(LibMisc.MOD_ID + ":" + LibBlockNames.PLATFORM)
	public static TileEntityType<TilePlatform> TYPE;

	public TilePlatform() {
		super(TYPE);
	}

	public boolean onWanded(PlayerEntity player) {
		if(player != null) {
			if(camoState == null || player.isSneaking())
				swapSelfAndPass(this, true);
			else swapSurroudings(this, false);
			return true;
		}

		return false;
	}

	private void swapSelfAndPass(TilePlatform tile, boolean empty) {
		swap(tile, empty);
		swapSurroudings(tile, empty);
	}

	private void swapSurroudings(TilePlatform tile, boolean empty) {
		for(Direction dir : Direction.values()) {
			BlockPos pos = tile.getPos().offset(dir);
			TileEntity tileAt = world.getTileEntity(pos);
			if(tileAt instanceof TilePlatform) {
				TilePlatform platform = (TilePlatform) tileAt;
				if(empty == (platform.camoState != null))
					swapSelfAndPass(platform, empty);
			}
		}
	}

	private void swap(TilePlatform tile, boolean empty) {
		tile.camoState = empty ? null : camoState;
		world.notifyBlockUpdate(tile.getPos(), world.getBlockState(tile.getPos()), world.getBlockState(tile.getPos()), 8);
	}

}
