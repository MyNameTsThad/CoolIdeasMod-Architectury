package com.thaddev.coolideas.mechanics.data.forge;

import com.thaddev.coolideas.mechanics.capabilities.PlayerSoulsProvider;
import com.thaddev.coolideas.mechanics.data.PlayerSouls;
import net.minecraft.world.entity.player.Player;

import java.util.concurrent.atomic.AtomicReference;

public class PlayerSoulsRetrieverImpl {
    public static PlayerSouls get(Player player) {
        AtomicReference<PlayerSouls> playerSouls = new AtomicReference<>();
        player.getCapability(PlayerSoulsProvider.PLAYER_SOULS).ifPresent(playerSouls::set);
        return playerSouls.get();
    }

    public static void set(PlayerSouls playerSouls, Player player) {
        player.getCapability(PlayerSoulsProvider.PLAYER_SOULS).ifPresent(playerSouls1 -> playerSouls1.copyFrom(playerSouls));
    }
}
