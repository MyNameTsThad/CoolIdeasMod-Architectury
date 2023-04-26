package com.thaddev.coolideas.mechanics.data.fabric;

import com.thaddev.coolideas.mechanics.data.PlayerSouls;
import com.thaddev.coolideas.util.EntityDataSaver;
import com.thaddev.coolideas.util.SoulsDataProvider;
import net.minecraft.world.entity.player.Player;

public class PlayerSoulsRetrieverImpl {
    public static PlayerSouls get(Player player) {
        EntityDataSaver data = (EntityDataSaver) player;
        PlayerSouls playerSouls = new PlayerSouls();
        playerSouls.setSouls(SoulsDataProvider.getSouls(data));
        playerSouls.setCustomSoulCapacity(SoulsDataProvider.getSavedSoulCapacity(data));
        playerSouls.setUseCustomSoulCapacity(SoulsDataProvider.isUseCustomSoulCapacity(data));
        return playerSouls;
    }

    public static void set(PlayerSouls playerSouls, Player player) {
        EntityDataSaver data = (EntityDataSaver) player;
        SoulsDataProvider.setSouls(data, playerSouls.getSouls());
        SoulsDataProvider.setCustomSoulCapacity(data, playerSouls.getSavedSoulCapacity());
        SoulsDataProvider.setUseCustomSoulCapacity(data, playerSouls.isUseCustomSoulCapacity());
    }
}
