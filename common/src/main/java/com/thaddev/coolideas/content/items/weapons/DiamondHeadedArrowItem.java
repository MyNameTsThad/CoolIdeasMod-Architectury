package com.thaddev.coolideas.content.items.weapons;

import com.thaddev.coolideas.content.entities.projectiles.DiamondHeadedArrow;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class DiamondHeadedArrowItem extends ArrowItem {

    public DiamondHeadedArrowItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull AbstractArrow createArrow(@NotNull Level world, @NotNull ItemStack arrowStack, @NotNull LivingEntity livingEntity) {
        DiamondHeadedArrow arrow = new DiamondHeadedArrow(livingEntity, world);
        arrow.setEffectsFromItem(arrowStack);
        return arrow;
    }
}
