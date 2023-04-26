package com.thaddev.coolideas.content;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.entity.ai.attributes.Attribute;

public class ReachAttributeRetriever {
    @ExpectPlatform
    public static Attribute getReachAttribute() {
        throw new AssertionError();
    }
}
