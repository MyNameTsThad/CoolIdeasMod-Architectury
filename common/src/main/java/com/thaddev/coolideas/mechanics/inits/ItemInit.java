package com.thaddev.coolideas.mechanics.inits;

import com.thaddev.coolideas.CoolIdeasMod;
import com.thaddev.coolideas.content.items.materials.MicrochipItem;
import com.thaddev.coolideas.content.items.materials.SiliconPCBItem;
import com.thaddev.coolideas.content.items.materials.SoulContainerBlockItem;
import com.thaddev.coolideas.content.items.tools.TweezersItem;
import com.thaddev.coolideas.content.items.weapons.*;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.world.item.*;

public class ItemInit {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(CoolIdeasMod.MODID, Registry.ITEM_REGISTRY);

    public static final RegistrySupplier<Item> SCYTHE = ITEMS.register("scythe",
        () -> new ScytheItem(Tiers.NETHERITE, 6, -2.7F, 2D,
            new Item.Properties()
                .tab(CreativeModeTab.TAB_COMBAT)
                .fireResistant()
                .rarity(Rarity.RARE)
        ));

    public static final RegistrySupplier<Item> WOODEN_SHORTBOW = ITEMS.register("wooden_shortbow",
        () -> new WoodenShortBowItem(new Item.Properties()
            .tab(CreativeModeTab.TAB_COMBAT)
            .stacksTo(1)
            .durability(125)));
    public static final RegistrySupplier<Item> IRON_SHORTBOW = ITEMS.register("iron_shortbow",
        () -> new IronShortBowItem(new Item.Properties()
            .tab(CreativeModeTab.TAB_COMBAT)
            .stacksTo(1)
            .durability(650)));
    public static final RegistrySupplier<Item> DIAMOND_SHORTBOW = ITEMS.register("diamond_shortbow",
        () -> new DiamondShortBowItem(new Item.Properties()
            .tab(CreativeModeTab.TAB_COMBAT)
            .stacksTo(1)
            .durability(1075)));

    public static final RegistrySupplier<Item> DIAMOND_HEADED_ARROW = ITEMS.register("diamond_headed_arrow",
        () -> new DiamondHeadedArrowItem(new Item.Properties()
            .tab(CreativeModeTab.TAB_COMBAT)));
    public static final RegistrySupplier<Item> TIPPED_DIAMOND_HEADED_ARROW = ITEMS.register("tipped_diamond_headed_arrow",
        () -> new TippedDiamondHeadedArrowItem(new Item.Properties()
            .tab(CreativeModeTab.TAB_COMBAT)));

    public static final RegistrySupplier<Item> TWEEZERS = ITEMS.register("tweezers",
        () -> new TweezersItem(new Item.Properties()
            .tab(CreativeModeTab.TAB_TOOLS)
            .stacksTo(1)
            .durability(400)));

    public static final RegistrySupplier<Item> RAW_RUBBER_BOTTLE = ITEMS.register("raw_rubber_bottle",
        () -> new Item(new Item.Properties()
            .tab(CreativeModeTab.TAB_MATERIALS)
            .craftRemainder(Items.GLASS_BOTTLE)
            .stacksTo(8)));
    public static final RegistrySupplier<Item> RUBBER_BOTTLE = ITEMS.register("rubber_bottle",
        () -> new Item(new Item.Properties()
            .tab(CreativeModeTab.TAB_MATERIALS)
            .craftRemainder(Items.GLASS_BOTTLE)
            .stacksTo(8)));
    public static final RegistrySupplier<Item> RUBBER_BAND = ITEMS.register("rubber_band",
        () -> new Item(new Item.Properties()
            .tab(CreativeModeTab.TAB_MATERIALS)));
    public static final RegistrySupplier<Item> LATEX_BAND = ITEMS.register("latex_band",
        () -> new Item(new Item.Properties()
            .tab(CreativeModeTab.TAB_MATERIALS)));
    public static final RegistrySupplier<Item> SPIDER_WEB = ITEMS.register("spider_web",
        () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS)));
    public static final RegistrySupplier<Item> IRON_ROD = ITEMS.register("iron_rod",
        () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS)));
    public static final RegistrySupplier<Item> DIAMOND_ROD = ITEMS.register("diamond_rod",
        () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS)));
    public static final RegistrySupplier<Item> DIAMOND_ARROW_HEAD = ITEMS.register("diamond_arrow_head",
        () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_COMBAT)));
    public static final RegistrySupplier<Item> CARBON_FIBER = ITEMS.register("carbon_fiber",
        () -> new Item(new Item.Properties()
            .tab(CreativeModeTab.TAB_MATERIALS)
            .fireResistant()));
    public static final RegistrySupplier<Item> RAW_SILICON = ITEMS.register("raw_silicon",
        () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS)));
    public static final RegistrySupplier<Item> SILICON = ITEMS.register("silicon",
        () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS)));
    public static final RegistrySupplier<Item> SILICON_PCB = ITEMS.register("silicon_pcb",
        () -> new SiliconPCBItem(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS)));
    public static final RegistrySupplier<Item> MICROCHIP = ITEMS.register("microchip",
        () -> new MicrochipItem(new Item.Properties()
            .tab(CreativeModeTab.TAB_MATERIALS)
            .stacksTo(1)
            .rarity(Rarity.UNCOMMON)));

    public static final RegistrySupplier<Item> SOUL_VIAL = ITEMS.register("soul_vial",
        () -> new SoulContainerBlockItem(BlockInit.SOUL_VIAL.get(),
            new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS),
            SoulContainerBlockItem.ContainerTypes.VIAL
        ));
    public static final RegistrySupplier<Item> SOUL_BOTTLE = ITEMS.register("soul_bottle",
        () -> new SoulContainerBlockItem(BlockInit.SOUL_BOTTLE.get(),
            new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS),
            SoulContainerBlockItem.ContainerTypes.BOTTLE
        ));
    public static final RegistrySupplier<Item> SOUL_JAR = ITEMS.register("soul_jar",
        () -> new SoulContainerBlockItem(BlockInit.SOUL_JAR.get(),
            new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS),
            SoulContainerBlockItem.ContainerTypes.JAR
        ));
    public static final RegistrySupplier<Item> SOUL_GALLON = ITEMS.register("soul_gallon",
        () -> new SoulContainerBlockItem(BlockInit.SOUL_GALLON.get(),
            new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS),
            SoulContainerBlockItem.ContainerTypes.GALLON
        ));
}
