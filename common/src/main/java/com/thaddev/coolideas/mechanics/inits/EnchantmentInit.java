package com.thaddev.coolideas.mechanics.inits;

import com.thaddev.coolideas.CoolIdeasMod;
import com.thaddev.coolideas.content.enchantments.InheritEnchantment;
import com.thaddev.coolideas.content.enchantments.PrecisionEnchantment;
import com.thaddev.coolideas.util.ShortbowEnchantmentCategoryRetriever;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;

public class EnchantmentInit {
    public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(CoolIdeasMod.MODID, Registry.ENCHANTMENT_REGISTRY);

    public static RegistrySupplier<Enchantment> PRECISION = ENCHANTMENTS.register("precision",
            () -> new PrecisionEnchantment(Enchantment.Rarity.UNCOMMON, ShortbowEnchantmentCategoryRetriever.get(), new EquipmentSlot[]{EquipmentSlot.MAINHAND}));
    public static RegistrySupplier<Enchantment> INHERIT = ENCHANTMENTS.register("inherit",
            () -> new InheritEnchantment(Enchantment.Rarity.UNCOMMON, ShortbowEnchantmentCategoryRetriever.get(), new EquipmentSlot[]{EquipmentSlot.MAINHAND}));
}
