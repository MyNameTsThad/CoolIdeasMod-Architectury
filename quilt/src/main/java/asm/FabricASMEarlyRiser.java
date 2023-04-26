package asm;

import com.chocohead.mm.api.ClassTinkerers;
import com.thaddev.coolideas.CoolIdeasMod;
import org.quiltmc.loader.api.MappingResolver;
import org.quiltmc.loader.api.QuiltLoader;

public class FabricASMEarlyRiser implements Runnable {
    @Override
    public void run() {
        MappingResolver remapper = QuiltLoader.getMappingResolver();

        CoolIdeasMod.LOGGER.debug("Initializing " + CoolIdeasMod.MODID + " Early Riser");
        String enchantmentTarget = remapper.mapClassName("intermediary", "net.minecraft.class_1886");
        ClassTinkerers.enumBuilder(enchantmentTarget).addEnumSubclass("SHORTBOW", "com.thaddev.coolideas.asm.ShortbowEnchantmentTarget").build();
    }
}
