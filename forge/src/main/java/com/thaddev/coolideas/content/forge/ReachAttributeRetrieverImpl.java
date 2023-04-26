package com.thaddev.coolideas.content.forge;

import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraftforge.common.ForgeMod;

public class ReachAttributeRetrieverImpl {
    public static Attribute getReachAttribute() {
        return ForgeMod.ATTACK_RANGE.get();
    }
}