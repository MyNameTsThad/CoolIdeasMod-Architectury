package com.thaddev.coolideas.util.forge;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ForgeEventInvokersImpl {
    public static int onArrowLoose(ItemStack stack, Level level, Player player, int charge, boolean hasAmmo) {
        return net.minecraftforge.event.ForgeEventFactory.onArrowLoose(stack, level, player, charge, hasAmmo);
    }
}
