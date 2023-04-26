package com.thaddev.coolideas.util;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ForgeEventInvokers {
    @ExpectPlatform
    public static int onArrowLoose(ItemStack stack, Level level, Player player, int charge, boolean hasAmmo){
        throw new AssertionError();
    }
}
