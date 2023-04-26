package com.thaddev.coolideas.util;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class ShortbowEnchantmentCategoryRetriever {
    @ExpectPlatform
    public static EnchantmentCategory get() {
        throw new AssertionError();
    }
}
