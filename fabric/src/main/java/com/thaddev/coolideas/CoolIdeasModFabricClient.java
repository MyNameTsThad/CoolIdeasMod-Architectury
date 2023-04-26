package com.thaddev.coolideas;

import net.fabricmc.api.ClientModInitializer;

public class CoolIdeasModFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        CoolIdeasModFabricLike.initClient();
    }
}
