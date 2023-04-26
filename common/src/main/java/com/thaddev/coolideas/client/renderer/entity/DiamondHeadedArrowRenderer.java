package com.thaddev.coolideas.client.renderer.entity;

import com.thaddev.coolideas.CoolIdeasMod;
import com.thaddev.coolideas.content.entities.projectiles.DiamondHeadedArrow;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@Environment(EnvType.CLIENT)
public class DiamondHeadedArrowRenderer extends ArrowRenderer<DiamondHeadedArrow> {

    public DiamondHeadedArrowRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(DiamondHeadedArrow entity) {
        Item refItem = entity.getPickupItem().getItem();
        return new ResourceLocation(CoolIdeasMod.MODID, "textures/entity/projectiles/"
                + Objects.requireNonNull(Registry.ITEM.getKey(refItem)).getPath() + ".png");
    }
}
