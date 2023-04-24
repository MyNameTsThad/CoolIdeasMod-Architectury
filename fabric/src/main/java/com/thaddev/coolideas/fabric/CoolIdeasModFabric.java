package com.thaddev.coolideas.fabric;

import net.examplemod.fabriclike.ExampleModFabricLike;
import net.fabricmc.api.ModInitializer;

public class CoolIdeasModFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        ExampleModFabricLike.init();
    }
}
