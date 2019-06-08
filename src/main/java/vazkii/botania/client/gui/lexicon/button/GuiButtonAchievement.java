/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Apr 3, 2015, 5:44:36 PM (GMT)]
 */
package vazkii.botania.client.gui.lexicon.button;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.advancements.AdvancementsScreen;
import net.minecraft.client.gui.advancements.AdvancementsScreen;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.client.gui.lexicon.GuiLexicon;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class GuiButtonAchievement extends GuiButtonLexicon {

	public GuiButtonAchievement(int id, int x, int y) {
		super(id, x, y, 11, 11, "");
	}

    @Override
    public void onClick(double mouseX, double mouseY) {
        super.onClick(mouseX, mouseY);
        if(Minecraft.getInstance().player != null) {
            AdvancementsScreen gui = new AdvancementsScreen(Minecraft.getInstance().player.connection.getAdvancementManager());
            Minecraft.getInstance().displayGuiScreen(gui);
            ResourceLocation tab = new ResourceLocation(LibMisc.MOD_ID, "main/root");
            gui.setSelectedTab(Minecraft.getInstance().player.connection.getAdvancementManager().getAdvancementList().getAdvancement(tab));
        }
    }

    @Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		hovered = mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
		int k = getHoverState(hovered);

		Minecraft.getInstance().textureManager.bindTexture(GuiLexicon.texture);
		GlStateManager.color4f(1F, 1F, 1F, 1F);
		drawTexturedModalRect(x, y, k == 2 ? 109 : 98, 191, 11, 11);

		List<String> tooltip = new ArrayList<>();
		tooltip.add(TextFormatting.GREEN + I18n.format("botaniamisc.advancements"));

		int tooltipY = (tooltip.size() - 1) * 10;
		if(k == 2)
			RenderHelper.renderTooltip(mouseX, mouseY + tooltipY, tooltip);
	}

}