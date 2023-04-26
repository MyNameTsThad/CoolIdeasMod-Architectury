package com.thaddev.coolideas;

import com.mojang.logging.LogUtils;
import com.thaddev.coolideas.mechanics.inits.*;
import org.slf4j.Logger;

public class CoolIdeasMod {
    public static final String MODID = "coolideas";

    public static final Logger LOGGER = LogUtils.getLogger();
    
    public static void init() {
        BlockInit.BLOCKS.register();
        ItemInit.ITEMS.register();
        EntityTypeInit.ENTITIES.register();
        EnchantmentInit.ENCHANTMENTS.register();
        EffectInit.MOB_EFFECTS.register();
        PotionInit.POTIONS.register();
        RecipeSerializerInit.RECIPES.register();
        WorldGenInit.register();
    }
}
