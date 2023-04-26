package com.thaddev.coolideas.mechanics.data;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.entity.player.Player;

public class PlayerSoulsRetriever {
    @ExpectPlatform
    public static PlayerSouls get(Player player) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static void set(PlayerSouls playerSouls, Player player) {
        throw new AssertionError();
    }
}
