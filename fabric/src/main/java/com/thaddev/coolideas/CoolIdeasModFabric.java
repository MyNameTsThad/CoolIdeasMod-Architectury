package com.thaddev.coolideas;

import net.fabricmc.api.ModInitializer;

public class CoolIdeasModFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        CoolIdeasModFabricLike.init();
    }
}
