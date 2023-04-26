package com.thaddev.coolideas.util.fabric;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ForgeEventInvokersImpl {
    public static int onArrowLoose(ItemStack stack, Level level, Player player, int charge, boolean hasAmmo) {
        //no events here in fabric, do nothing
        return charge;
    }
}
