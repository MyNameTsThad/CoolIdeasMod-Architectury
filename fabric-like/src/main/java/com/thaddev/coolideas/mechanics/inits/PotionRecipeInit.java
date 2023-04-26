package com.thaddev.coolideas.mechanics.inits;

import com.thaddev.coolideas.CoolIdeasMod;
import net.fabricmc.fabric.api.registry.FabricBrewingRecipeRegistry;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;

public class PotionRecipeInit {
    public static void registerPotionRecipes() {
        CoolIdeasMod.LOGGER.debug("Registering Potion Recipes for " + CoolIdeasMod.MODID + " (10/11)");

        //normal
        FabricBrewingRecipeRegistry.registerPotionRecipe(Potions.WEAKNESS, Ingredient.of(Items.FERMENTED_SPIDER_EYE), PotionInit.VULNERABILITY.get());
        FabricBrewingRecipeRegistry.registerPotionRecipe(PotionInit.VULNERABILITY.get(), Ingredient.of(Items.SUSPICIOUS_STEW), PotionInit.VULNERABILITY_2.get());
        FabricBrewingRecipeRegistry.registerPotionRecipe(PotionInit.VULNERABILITY_2.get(), Ingredient.of(Items.SHULKER_SHELL), PotionInit.VULNERABILITY_3.get());
        FabricBrewingRecipeRegistry.registerPotionRecipe(PotionInit.VULNERABILITY_3.get(), Ingredient.of(Items.NETHER_STAR), PotionInit.VULNERABILITY_4.get());

        //long
        FabricBrewingRecipeRegistry.registerPotionRecipe(PotionInit.VULNERABILITY.get(), Ingredient.of(Items.REDSTONE), PotionInit.VULNERABILITY_LONG.get());
        FabricBrewingRecipeRegistry.registerPotionRecipe(PotionInit.VULNERABILITY_2.get(), Ingredient.of(Items.REDSTONE), PotionInit.VULNERABILITY_2_LONG.get());
        FabricBrewingRecipeRegistry.registerPotionRecipe(PotionInit.VULNERABILITY_3.get(), Ingredient.of(Items.REDSTONE_BLOCK), PotionInit.VULNERABILITY_3_LONG.get());
        FabricBrewingRecipeRegistry.registerPotionRecipe(PotionInit.VULNERABILITY_4.get(), Ingredient.of(Items.REDSTONE_BLOCK), PotionInit.VULNERABILITY_4_LONG.get());
    }
}