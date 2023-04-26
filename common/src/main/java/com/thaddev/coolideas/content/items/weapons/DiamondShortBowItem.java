package com.thaddev.coolideas.content.items.weapons;

import com.thaddev.coolideas.Utils;
import com.thaddev.coolideas.mechanics.inits.ItemInit;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.thaddev.coolideas.Utils.component;

public class DiamondShortBowItem extends ShortBowBase {

    public DiamondShortBowItem(Properties properties) {
        super(properties);
    }

    @Override
    public double getPowerBaseDamage(double baseDamage, int powerLevel) {
        return baseDamage + (double) powerLevel + 1D;
    }

    @Override
    public float getCritChance() {
        return 0.4f;
    }

    @Override
    public float getRubberbandHitChance() {
        return 0.4f;
    }

    @Override
    public int getTicksOnFire() {
        return 150;
    }

    @Override
    public float getRubberbandHitDamage() {
        return 8f;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level world, @NotNull List<Component> tooltip, @NotNull TooltipFlag flags) {
        tooltip.add(component(Utils.fromNoTag("(%$blue)Shoots Instantly! (%$italic)Make sure to not hit yourself!")));
        super.appendHoverText(stack, world, tooltip, flags);
    }

    @Override
    public int getDefaultProjectileRange() {
        return 30;
    }

    @Override
    public boolean isValidRepairItem(@NotNull ItemStack thisItem, ItemStack repairItem) {
        return repairItem.getItem() == ItemInit.DIAMOND_ROD.get() || repairItem.getItem() == ItemInit.LATEX_BAND.get()
                || repairItem.getItem() == ItemInit.CARBON_FIBER.get() || super.isValidRepairItem(thisItem, repairItem);
    }

    @Override
    public boolean canHomeArrows() {
        return true;
    }
}
