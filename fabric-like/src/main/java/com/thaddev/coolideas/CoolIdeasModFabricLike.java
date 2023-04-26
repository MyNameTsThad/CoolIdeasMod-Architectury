package com.thaddev.coolideas;

import com.thaddev.coolideas.client.ClientEvents;
import com.thaddev.coolideas.client.renderer.entity.DiamondHeadedArrowRenderer;
import com.thaddev.coolideas.client.renderer.entity.SoulOrbRenderer;
import com.thaddev.coolideas.mechanics.inits.*;
import com.thaddev.coolideas.mechanics.networking.Packets;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.world.item.alchemy.PotionUtils;

public class CoolIdeasModFabricLike {
    public static void init() {
        CoolIdeasMod.init();
        PotionRecipeInit.registerPotionRecipes();
        LootTableModifierInit.modifyLootTables();
    }

    public static void initClient() {
        ClientEvents.registerClientEvents();
        EntityRendererRegistry.register(EntityTypeInit.DIAMOND_HEADED_ARROW.get(), DiamondHeadedArrowRenderer::new);
        EntityRendererRegistry.register(EntityTypeInit.SOUL_ORB.get(), SoulOrbRenderer::new);
        ColorProviderRegistry.ITEM.register(
                (itemStack, i) -> i > 0 ? -1 : PotionUtils.getColor(itemStack),
                ItemInit.TIPPED_DIAMOND_HEADED_ARROW.get()
        );
        ItemPropertiesInit.register();
        Packets.register();
    }
}
