package com.thaddev.coolideas.quilt;

import com.thaddev.coolideas.fabriclike.CoolIdeasModFabricLike;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;

public class ExampleModQuilt implements ModInitializer {
    @Override
    public void onInitialize(ModContainer mod) {
        CoolIdeasModFabricLike.init();
    }
}
