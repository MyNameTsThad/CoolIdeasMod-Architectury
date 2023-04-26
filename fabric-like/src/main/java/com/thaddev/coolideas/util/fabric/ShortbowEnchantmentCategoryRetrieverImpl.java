package com.thaddev.coolideas.util.fabric;

import com.chocohead.mm.api.ClassTinkerers;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class ShortbowEnchantmentCategoryRetrieverImpl {
    public static EnchantmentCategory get() {
        return ClassTinkerers.getEnum(EnchantmentCategory.class, "SHORTBOW");
    }
}
