package com.thaddev.coolideas;

import com.thaddev.coolideas.content.items.weapons.DiamondShortBowItem;
import com.thaddev.coolideas.content.items.weapons.IronShortBowItem;
import com.thaddev.coolideas.mechanics.inits.GlobalLootModifierInit;
import com.thaddev.coolideas.mechanics.inits.ItemPropertiesInit;
import com.thaddev.coolideas.mechanics.inits.PotionRecipeInit;
import com.thaddev.coolideas.mechanics.networking.Packets;
import dev.architectury.platform.forge.EventBuses;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(CoolIdeasMod.MODID)
public class CoolIdeasModForge {
    public static EnchantmentCategory SHORTBOW = EnchantmentCategory.create("SHORTBOW",
            (item) -> EnchantmentCategory.BOW.canEnchant(item) ||
                    item instanceof IronShortBowItem || item instanceof DiamondShortBowItem);

    public CoolIdeasModForge() {
        // Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(CoolIdeasMod.MODID, FMLJavaModLoadingContext.get().getModEventBus());
        CoolIdeasMod.init();
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        GlobalLootModifierInit.GLOBAL_LOOT_MODIFIERS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public void setup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            Packets.register();
            PotionRecipeInit.register();
            ItemPropertiesInit.register();
        });
    }
}
