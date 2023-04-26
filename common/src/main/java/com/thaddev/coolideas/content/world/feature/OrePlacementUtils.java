package com.thaddev.coolideas.content.world.feature;

import net.minecraft.world.level.levelgen.placement.*;

import java.util.List;

public class OrePlacementUtils {
    public static List<PlacementModifier> orePlacement(PlacementModifier modifier1, PlacementModifier modifier2){
        return List.of(modifier1, InSquarePlacement.spread(), modifier2, BiomeFilter.biome());
    }

    public static List<PlacementModifier> commonOrePlacement(int veinsPerChunk, PlacementModifier modifier){
        return orePlacement(CountPlacement.of(veinsPerChunk), modifier);
    }

    public static List<PlacementModifier> rareOrePlacement(int veinsPerChunk, PlacementModifier modifier){
        return orePlacement(RarityFilter.onAverageOnceEvery(veinsPerChunk), modifier);
    }
}
