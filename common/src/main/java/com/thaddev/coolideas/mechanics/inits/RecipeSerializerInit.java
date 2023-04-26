package com.thaddev.coolideas.mechanics.inits;

import com.thaddev.coolideas.CoolIdeasMod;
import com.thaddev.coolideas.mechanics.recipes.ApplyDiamondShortbowRecipe;
import com.thaddev.coolideas.mechanics.recipes.ApplySiliconPCBRecipe;
import com.thaddev.coolideas.mechanics.recipes.TippedDiamondHeadedArrowRecipe;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;

public class RecipeSerializerInit {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPES = DeferredRegister.create(CoolIdeasMod.MODID, Registry.RECIPE_SERIALIZER_REGISTRY);

    public static final RegistrySupplier<SimpleRecipeSerializer<TippedDiamondHeadedArrowRecipe>> TIPPED_DIAMOND_HEADED_ARROW =
        RECIPES.register(
            "crafting_special_tipped_diamond_headed_arrow",
            () -> new SimpleRecipeSerializer<>(TippedDiamondHeadedArrowRecipe::new)
        );

    public static final RegistrySupplier<RecipeSerializer<ApplySiliconPCBRecipe>> APPLY_SILICON_PCB =
        RECIPES.register(
            "smithing_special_apply_silicon_pcb",
            ApplySiliconPCBRecipe.Serializer::new
        );

    public static final RegistrySupplier<RecipeSerializer<ApplyDiamondShortbowRecipe>> APPLY_DIAMOND_SHORTBOW =
        RECIPES.register(
            "smithing_special_apply_diamond_shortbow",
            ApplyDiamondShortbowRecipe.Serializer::new
        );
}
