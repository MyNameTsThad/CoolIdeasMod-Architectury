package com.thaddev.coolideas.mechanics.inits;

import com.thaddev.coolideas.CoolIdeasMod;
import com.thaddev.coolideas.content.blocks.SoulBottleBlock;
import com.thaddev.coolideas.content.blocks.SoulGallonBlock;
import com.thaddev.coolideas.content.blocks.SoulJarBlock;
import com.thaddev.coolideas.content.blocks.SoulVialBlock;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;

import java.util.function.Supplier;

public class BlockInit {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(CoolIdeasMod.MODID, Registry.BLOCK_REGISTRY);

    public static RegistrySupplier<Block> SILICON_ORE = registerBlock("silicon_ore",
        () -> new DropExperienceBlock(BlockBehaviour.Properties
            .of(Material.STONE, MaterialColor.NETHER)
            .requiresCorrectToolForDrops()
            .strength(3f)
            .sound(SoundType.NETHER_ORE), UniformInt.of(4, 10)
        ),
        CreativeModeTab.TAB_MATERIALS);

    public static final RegistrySupplier<Block> SOUL_VIAL = registerBlockWithoutItem("soul_vial",
        () -> new SoulVialBlock(BlockBehaviour.Properties
            .of(Material.GLASS)
            .strength(0.1F)
            .noOcclusion()
            .sound(SoundType.GLASS)
            .isValidSpawn((pState, pLevel, pPos, pValue) -> false)
            .isRedstoneConductor((pState, pLevel, pPos) -> false)
            .isSuffocating((pState, pLevel, pPos) -> false)
            .isViewBlocking((pState, pLevel, pPos) -> false)
        ));

    public static final RegistrySupplier<Block> SOUL_BOTTLE = registerBlockWithoutItem("soul_bottle",
        () -> new SoulBottleBlock(BlockBehaviour.Properties
            .of(Material.GLASS)
            .strength(0.1F)
            .noOcclusion()
            .sound(SoundType.GLASS)
            .isValidSpawn((pState, pLevel, pPos, pValue) -> false)
            .isRedstoneConductor((pState, pLevel, pPos) -> false)
            .isSuffocating((pState, pLevel, pPos) -> false)
            .isViewBlocking((pState, pLevel, pPos) -> false)
        ));

    public static final RegistrySupplier<Block> SOUL_JAR = registerBlockWithoutItem("soul_jar",
        () -> new SoulJarBlock(BlockBehaviour.Properties
            .of(Material.GLASS)
            .strength(0.1F)
            .noOcclusion()
            .sound(SoundType.GLASS)
            .isValidSpawn((pState, pLevel, pPos, pValue) -> false)
            .isRedstoneConductor((pState, pLevel, pPos) -> false)
            .isSuffocating((pState, pLevel, pPos) -> false)
            .isViewBlocking((pState, pLevel, pPos) -> false)
        ));

    public static final RegistrySupplier<Block> SOUL_GALLON = registerBlockWithoutItem("soul_gallon",
        () -> new SoulGallonBlock(BlockBehaviour.Properties
            .of(Material.GLASS)
            .strength(0.1F)
            .noOcclusion()
            .sound(SoundType.GLASS)
            .isValidSpawn((pState, pLevel, pPos, pValue) -> false)
            .isRedstoneConductor((pState, pLevel, pPos) -> false)
            .isSuffocating((pState, pLevel, pPos) -> false)
            .isViewBlocking((pState, pLevel, pPos) -> false)
        ));


    private static <T extends Block> RegistrySupplier<T> registerBlock(String name, Supplier<T> block, CreativeModeTab tab) {
        RegistrySupplier<T> returnVal = BLOCKS.register(name, block);
        registerBlockItem(name, returnVal, tab);
        return returnVal;
    }

    private static <T extends Block> RegistrySupplier<T> registerBlockWithoutItem(String name, Supplier<T> block) {
        return BLOCKS.register(name, block);
    }

    private static <T extends Block> RegistrySupplier<Item> registerBlockItem(String name, RegistrySupplier<T> block, CreativeModeTab tab) {
        return ItemInit.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().tab(tab)));
    }
}
