package com.thaddev.coolideas.content.fabric;

import com.jamieswhiteshirt.reachentityattributes.ReachEntityAttributes;
import net.minecraft.world.entity.ai.attributes.Attribute;

public class ReachAttributeRetrieverImpl {
    public static Attribute getReachAttribute() {
        return ReachEntityAttributes.ATTACK_RANGE;
    }
}
