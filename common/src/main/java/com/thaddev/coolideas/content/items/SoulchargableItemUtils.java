package com.thaddev.coolideas.content.items;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public class SoulchargableItemUtils {
    public static int SOULCHARGE_SOULS_REQUIRED = 5000;
    public static int OVERCHARGE_SOULS_REQUIRED = 10000;

    public static int getSoulChargeProgress(ItemStack stack){
        CompoundTag tag = stack.getOrCreateTag();
        return tag.getInt("SoulChargeProgress");
    }

    public static void incrementSoulChargeProgress(ItemStack stack, int toIncrement){
        CompoundTag tag = stack.getOrCreateTag();
        if (getSoulChargeProgress(stack) + toIncrement > SOULCHARGE_SOULS_REQUIRED){
            tag.putInt("SoulChargeProgress", SOULCHARGE_SOULS_REQUIRED);
        } else {
            tag.putInt("SoulChargeProgress", Math.max(getSoulChargeProgress(stack) + toIncrement, 0));
        }
    }

    public static int getOverChargeProgress(ItemStack stack){
        CompoundTag tag = stack.getOrCreateTag();
        return tag.getInt("OverChargeProgress");
    }

    public static void incrementOverChargeProgress(ItemStack stack, int toIncrement){
        CompoundTag tag = stack.getOrCreateTag();
        if (getOverChargeProgress(stack) + toIncrement > OVERCHARGE_SOULS_REQUIRED){
            tag.putInt("OverChargeProgress", OVERCHARGE_SOULS_REQUIRED);
        } else {
            tag.putInt("OverChargeProgress", Math.max(getOverChargeProgress(stack) + toIncrement, 0));
        }
    }

    public static boolean isReadyToCharge(ItemStack stack){
        return getSoulChargeProgress(stack) >= SOULCHARGE_SOULS_REQUIRED;
    }

    public static boolean isCharged(ItemStack stack){
        CompoundTag tag = stack.getOrCreateTag();
        return tag.getBoolean("IsCharged");
    }

    public static boolean isOverCharged(ItemStack stack){
        return getOverChargeProgress(stack) > 0;
    }

    public static boolean isFullyOverCharged(ItemStack stack){
        return getOverChargeProgress(stack) >= OVERCHARGE_SOULS_REQUIRED;
    }

    public static float getOverChargePercentDecimal(ItemStack stack){
        return ((float) getOverChargeProgress(stack) / (float) OVERCHARGE_SOULS_REQUIRED);
    }

    public static boolean tryCharge(ItemStack stack){
        if (isReadyToCharge(stack) && !isCharged(stack)){
            CompoundTag tag = stack.getOrCreateTag();
            tag.putBoolean("IsCharged", true);
            return true;
        }
        return false;
    }
}
