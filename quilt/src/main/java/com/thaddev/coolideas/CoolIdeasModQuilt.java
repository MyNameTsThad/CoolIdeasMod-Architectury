package com.thaddev.coolideas;

import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;

public class CoolIdeasModQuilt implements ModInitializer {
    @Override
    public void onInitialize(ModContainer mod) {
        CoolIdeasModFabricLike.init();
    }
}
