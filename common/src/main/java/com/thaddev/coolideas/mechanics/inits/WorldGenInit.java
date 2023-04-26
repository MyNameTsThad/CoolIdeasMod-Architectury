package com.thaddev.coolideas.mechanics.inits;

import com.google.common.base.Suppliers;
import com.thaddev.coolideas.CoolIdeasMod;
import com.thaddev.coolideas.content.world.feature.OrePlacementUtils;
import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.registry.level.biome.BiomeModifications;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.List;
import java.util.function.Supplier;

public class WorldGenInit {
    public static final Supplier<List<OreConfiguration.TargetBlockState>> NETHER_SILICON_ORES = Suppliers.memoize(
            () -> List.of(
                    OreConfiguration.target(OreFeatures.NETHER_ORE_REPLACEABLES, BlockInit.SILICON_ORE.get().defaultBlockState())
            )
    );
    public static void register() {
        LifecycleEvent.SETUP.register(() -> {
            Holder<ConfiguredFeature<OreConfiguration, ?>> SILICON_ORE = FeatureUtils.register(CoolIdeasMod.MODID + ":silicon_ore", Feature.ORE,
                    new OreConfiguration(NETHER_SILICON_ORES.get(), 4));
            Holder<PlacedFeature> SILICON_ORE_PLACED = PlacementUtils.register(CoolIdeasMod.MODID + ":silicon_ore", SILICON_ORE,
                    OrePlacementUtils.commonOrePlacement(
                            4,
                            HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(0), VerticalAnchor.aboveBottom(80))
                    ));
            BiomeModifications.addProperties((ctx, mutable) -> {
                if (ctx.hasTag(BiomeTags.IS_NETHER)) {
                    mutable.getGenerationProperties().addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, SILICON_ORE_PLACED);
                }
            });
        });
    }
}
