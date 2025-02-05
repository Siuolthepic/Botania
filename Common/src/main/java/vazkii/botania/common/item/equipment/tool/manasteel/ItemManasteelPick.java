/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.equipment.tool.manasteel;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.item.ISortableTool;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.client.gui.ItemsRemainingRenderHandler;
import vazkii.botania.common.helper.PlayerHelper;
import vazkii.botania.common.item.equipment.ICustomDamageItem;
import vazkii.botania.common.item.equipment.tool.ToolCommons;

import javax.annotation.Nonnull;

import java.util.function.Consumer;
import java.util.regex.Pattern;

public class ItemManasteelPick extends PickaxeItem implements ICustomDamageItem, ISortableTool {

	private static final Pattern TORCH_PATTERN = Pattern.compile("(?:(?:(?:[A-Z-_.:]|^)torch)|(?:(?:[a-z-_.:]|^)Torch))(?:[A-Z-_.:]|$)");

	private static final int MANA_PER_DAMAGE = 60;

	public ItemManasteelPick(Properties props) {
		this(BotaniaAPI.instance().getManasteelItemTier(), props, -2.8F);
	}

	public ItemManasteelPick(Tier mat, Properties props, float attackSpeed) {
		super(mat, 1, attackSpeed, props);
	}

	@Override
	public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
		int manaPerDamage = ((ItemManasteelPick) stack.getItem()).getManaPerDamage();
		return ToolCommons.damageItemIfPossible(stack, amount, entity, manaPerDamage);
	}

	@Nonnull
	@Override
	public InteractionResult useOn(UseOnContext ctx) {
		Player player = ctx.getPlayer();

		if (player != null) {
			for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
				ItemStack stackAt = player.getInventory().getItem(i);
				if (!stackAt.isEmpty() && TORCH_PATTERN.matcher(stackAt.getItem().getDescriptionId()).find()) {
					ItemStack displayStack = stackAt.copy();
					InteractionResult did = PlayerHelper.substituteUse(ctx, stackAt);
					if (did.consumesAction() && !ctx.getLevel().isClientSide) {
						ItemsRemainingRenderHandler.send(player, displayStack, TORCH_PATTERN);
					}
					return did;
				}
			}
		}

		return InteractionResult.PASS;
	}

	public int getManaPerDamage() {
		return MANA_PER_DAMAGE;
	}

	@Override
	public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean selected) {
		if (!world.isClientSide && entity instanceof Player player && stack.getDamageValue() > 0 && ManaItemHandler.instance().requestManaExactForTool(stack, player, MANA_PER_DAMAGE * 2, true)) {
			stack.setDamageValue(stack.getDamageValue() - 1);
		}
	}

	@Override
	public int getSortingPriority(ItemStack stack, BlockState state) {
		return ToolCommons.getToolPriority(stack);
	}
}
