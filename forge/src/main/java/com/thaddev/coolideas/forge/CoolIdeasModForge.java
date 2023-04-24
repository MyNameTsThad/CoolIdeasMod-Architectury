package com.thaddev.coolideas.forge;

import com.thaddev.coolideas.CoolIdeasMod;
import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(CoolIdeasMod.MOD_ID)
public class CoolIdeasModForge {
    public CoolIdeasModForge() {
        // Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(CoolIdeasMod.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        CoolIdeasMod.init();
    }
}
