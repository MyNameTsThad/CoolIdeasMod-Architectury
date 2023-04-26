package com.thaddev.coolideas.mechanics.inits;

import com.thaddev.coolideas.CoolIdeasMod;
import com.thaddev.coolideas.content.items.SoulchargableItemUtils;
import com.thaddev.coolideas.content.items.materials.SoulContainerBlockItem;
import dev.architectury.registry.item.ItemPropertiesRegistry;
import net.minecraft.resources.ResourceLocation;

public class ItemPropertiesInit {
    public static void register() {
        ItemPropertiesRegistry.register(ItemInit.SCYTHE.get(), new ResourceLocation(CoolIdeasMod.MODID, "charged"),
            (pStack, pLevel, pEntity, pSeed) -> SoulchargableItemUtils.isCharged(pStack) ? 1.0F : 0.0F
        );

        ItemPropertiesRegistry.register(ItemInit.SOUL_VIAL.get(), new ResourceLocation(CoolIdeasMod.MODID, "filled"),
            (pStack, pLevel, pEntity, pSeed) -> SoulContainerBlockItem.isFilled(pStack) ? 1.0F : 0.0F
        );

        ItemPropertiesRegistry.register(ItemInit.SOUL_BOTTLE.get(), new ResourceLocation(CoolIdeasMod.MODID, "filled"),
            (pStack, pLevel, pEntity, pSeed) -> SoulContainerBlockItem.isFilled(pStack) ? 1.0F : 0.0F
        );

        ItemPropertiesRegistry.register(ItemInit.SOUL_JAR.get(), new ResourceLocation(CoolIdeasMod.MODID, "filled"),
            (pStack, pLevel, pEntity, pSeed) -> SoulContainerBlockItem.isFilled(pStack) ? 1.0F : 0.0F
        );

        ItemPropertiesRegistry.register(ItemInit.SOUL_GALLON.get(), new ResourceLocation(CoolIdeasMod.MODID, "filled"),
            (pStack, pLevel, pEntity, pSeed) -> SoulContainerBlockItem.isFilled(pStack) ? 1.0F : 0.0F
        );
    }
}
