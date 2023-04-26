package com.thaddev.coolideas.mechanics.inits;

import com.thaddev.coolideas.CoolIdeasMod;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class TagsInit {
    public static final TagKey<Block> REGULAR_LOGS = blockTag("regular_logs");
    public static final TagKey<Block> STRIPPED_LOGS = blockTag("stripped_logs");

    public static final TagKey<Item> SOULCHARGABLE = itemTag("soulchargable");

    private static TagKey<Block> blockTag(String name) {
        return TagKey.create(Registry.BLOCK_REGISTRY, new ResourceLocation(CoolIdeasMod.MODID, name));
    }

    private static TagKey<Item> itemTag(String name) {
        return TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(CoolIdeasMod.MODID, name));
    }
}
