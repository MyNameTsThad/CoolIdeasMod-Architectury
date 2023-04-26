package com.thaddev.coolideas.asm;

import com.chocohead.mm.api.ClassTinkerers;
import com.thaddev.coolideas.CoolIdeasMod;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.MappingResolver;

public class FabricASMEarlyRiser implements Runnable {
    @Override
    public void run() {
        MappingResolver remapper = FabricLoader.getInstance().getMappingResolver();

        CoolIdeasMod.LOGGER.debug("Initializing " + CoolIdeasMod.MODID + " Early Riser");
        String enchantmentTarget = remapper.mapClassName("intermediary", "net.minecraft.class_1886");
        ClassTinkerers.enumBuilder(enchantmentTarget).addEnumSubclass("SHORTBOW", "com.thaddev.coolideas.asm.ShortbowEnchantmentTarget").build();
    }
}
