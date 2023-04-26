package com.thaddev.coolideas.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;

public class SoulsDataProvider {
    public static int getSouls(EntityDataSaver player) {
        CompoundTag nbt = player.getPersistentData();
        return nbt.getInt("Souls");
    }

    public static CompoundTag addSouls(EntityDataSaver player, int souls) {
        CompoundTag nbt = player.getPersistentData();
        int currentSouls = nbt.getInt("Souls");
        currentSouls += souls;
        nbt.putInt("Souls", currentSouls);
        //sync(currentSouls, getSavedSoulCapacity(player), isUseCustomSoulCapacity(player), (ServerPlayer) player);
        return nbt;
    }

    public static CompoundTag setSouls(EntityDataSaver player, int souls) {
        CompoundTag nbt = player.getPersistentData();
        nbt.putInt("Souls", souls);
        //sync(souls, getSavedSoulCapacity(player), isUseCustomSoulCapacity(player), (ServerPlayer) player);
        return nbt;
    }

    public static int getSoulCapacity(EntityDataSaver player, Player playerOptional) {
        if (!isUseCustomSoulCapacity(player)) {
            return getScaledSouls(playerOptional.experienceLevel);
        } else {
            return getSavedSoulCapacity(player);
        }
    }

    public static int getSavedSoulCapacity(EntityDataSaver player){
        CompoundTag nbt = player.getPersistentData();
        return nbt.getInt("CustomSoulCapacity");
    }

    public static CompoundTag setCustomSoulCapacity(EntityDataSaver player, int customSoulCapacity) {
        CompoundTag nbt = player.getPersistentData();
        if (isUseCustomSoulCapacity(player)) {
            nbt.putInt("CustomSoulCapacity", customSoulCapacity);
            //sync(getSouls(player), customSoulCapacity, isUseCustomSoulCapacity(player), (ServerPlayer) player);
        }
        return nbt;
    }

    public static boolean isUseCustomSoulCapacity(EntityDataSaver player) {
        CompoundTag nbt = player.getPersistentData();
        return nbt.getBoolean("UseCustomSoulCapacity");
    }

    public static CompoundTag setUseCustomSoulCapacity(EntityDataSaver player, boolean useCustomSoulCapacity) {
        CompoundTag nbt = player.getPersistentData();
        nbt.putBoolean("UseCustomSoulCapacity", useCustomSoulCapacity);
        //sync(getSouls(player), getSavedSoulCapacity(player), useCustomSoulCapacity, (ServerPlayer) player);
        return nbt;
    }

    private static int getScaledSouls(int level) {
        return ((1 + level) * level) * 10;
    }

//    public static void sync(int souls, int customSoulCapacity, boolean useCustomSoulCapacity, ServerPlayer player) {
//        FriendlyByteBuf buf = PacketByteBufs.create();
//        buf.writeInt(souls);
//        buf.writeInt(customSoulCapacity);
//        buf.writeBoolean(useCustomSoulCapacity);
//        ServerPlayNetworking.send(player, Packets.PLAYER_SOULS_SYNC_ID, buf);
//    }
}
