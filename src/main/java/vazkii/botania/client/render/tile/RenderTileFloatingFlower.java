/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jul 8, 2014, 10:58:46 PM (GMT)]
 */
package vazkii.botania.client.render.tile;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.LazyOptional;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.item.IFloatingFlower;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.common.core.handler.ConfigHandler;

import javax.annotation.Nonnull;
import java.util.Random;

public class RenderTileFloatingFlower extends TileEntityRenderer {

	@Override
	public void render(@Nonnull TileEntity tile, double d0, double d1, double d2, float t, int digProgress) {
		LazyOptional<IFloatingFlower> floatingOpt = tile.getCapability(BotaniaAPI.FLOATING_FLOWER_CAP);
		if(!floatingOpt.isPresent())
			return;

		IFloatingFlower floatingData = floatingOpt.orElseThrow(NullPointerException::new);
		BlockRendererDispatcher brd = Minecraft.getInstance().getBlockRendererDispatcher();
		GlStateManager.pushMatrix();
		GlStateManager.color4f(1F, 1F, 1F, 1F);
		GlStateManager.translated(d0, d1, d2);

		double worldTime = ClientTickHandler.ticksInGame + t;
		if(tile.getWorld() != null)
			worldTime += new Random(tile.getPos().hashCode()).nextInt(1000);

		GlStateManager.translatef(0.5F, 0, 0.5F);
		GlStateManager.rotatef(-((float) worldTime * 0.5F), 0F, 1F, 0F);
		GlStateManager.translated(-0.5, (float) Math.sin(worldTime * 0.05F) * 0.1F, 0.5);

		GlStateManager.rotatef(4F * (float) Math.sin(worldTime * 0.04F), 1F, 0F, 0F);

		Minecraft.getInstance().textureManager.bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);

		/* todo 1.13
		IBlockState state = tile.getWorld().getBlockState(tile.getPos());
		state = state.getBlock().getExtendedState(state, tile.getWorld(), tile.getPos());
		IBakedModel model = brd.getBlockModelShapes().getModelManager().getModel(new ModelResourceLocation("botania:floating_special_flower", "inventory"));
		brd.getBlockModelRenderer().renderModelBrightness(model, state, 1.0F, true);
		*/

		GlStateManager.popMatrix();

	}

}
