package com.thaddev.coolideas.mechanics.inits;

import com.thaddev.coolideas.CoolIdeasMod;
import com.thaddev.coolideas.Utils;
import com.thaddev.coolideas.content.effects.VulnerabilityEffect;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class EffectInit {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(CoolIdeasMod.MODID, Registry.MOB_EFFECT_REGISTRY);

    public static final RegistrySupplier<MobEffect> VULNERABILITY = MOB_EFFECTS.register("vulnerability",
        () -> new VulnerabilityEffect(MobEffectCategory.HARMFUL, Utils.rgbToInteger(50, 0, 0)));
}
