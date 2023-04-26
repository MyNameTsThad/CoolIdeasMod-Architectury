package com.thaddev.coolideas.mechanics.inits;

import com.thaddev.coolideas.CoolIdeasMod;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;

public class PotionInit {
    public static  final DeferredRegister<Potion> POTIONS = DeferredRegister.create(CoolIdeasMod.MODID, Registry.POTION_REGISTRY);

    public static final RegistrySupplier<Potion> VULNERABILITY = POTIONS.register("vulnerability",
            () -> new Potion(new MobEffectInstance(EffectInit.VULNERABILITY.get(), 2700, 0)));
    public static final RegistrySupplier<Potion> VULNERABILITY_2 = POTIONS.register("vulnerability_2",
            () -> new Potion(new MobEffectInstance(EffectInit.VULNERABILITY.get(), 1950, 1)));
    public static final RegistrySupplier<Potion> VULNERABILITY_3 = POTIONS.register("vulnerability_3",
            () -> new Potion(new MobEffectInstance(EffectInit.VULNERABILITY.get(), 1200, 2)));
    public static final RegistrySupplier<Potion> VULNERABILITY_4 = POTIONS.register("vulnerability_4",
            () -> new Potion(new MobEffectInstance(EffectInit.VULNERABILITY.get(), 450, 3)));

    public static final RegistrySupplier<Potion> VULNERABILITY_LONG = POTIONS.register("vulnerability_long",
            () -> new Potion(new MobEffectInstance(EffectInit.VULNERABILITY.get(), 4050, 0)));
    public static final RegistrySupplier<Potion> VULNERABILITY_2_LONG = POTIONS.register("vulnerability_2_long",
            () -> new Potion(new MobEffectInstance(EffectInit.VULNERABILITY.get(), 2925, 1)));
    public static final RegistrySupplier<Potion> VULNERABILITY_3_LONG = POTIONS.register("vulnerability_3_long",
            () -> new Potion(new MobEffectInstance(EffectInit.VULNERABILITY.get(), 1800, 2)));
    public static final RegistrySupplier<Potion> VULNERABILITY_4_LONG = POTIONS.register("vulnerability_4_long",
            () -> new Potion(new MobEffectInstance(EffectInit.VULNERABILITY.get(), 675, 3)));

}
