package com.thaddev.coolideas.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.thaddev.coolideas.client.ClientSoulsData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class SoulHudOverlay {
    private static final ResourceLocation BARS = new ResourceLocation("textures/gui/bars.png");

    public static final IGuiOverlay SOUL_CHARGE_BAR = ((gui, poseStack, partialTick, width, height) -> {
        if (Minecraft.getInstance().player != null) {
            int x = 10;
            int y = 10;

            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderColor(1, 1, 1, 1);
            RenderSystem.setShaderTexture(0, BARS);
            //empty bar
            GuiComponent.blit(poseStack, x, y, 0, 10, 182, 5, 256, 256);
            //filled bar
            GuiComponent.blit(poseStack, x, y, 0, 15, Math.round(182 * ((float)ClientSoulsData.getSouls() / (float)ClientSoulsData.getSoulCapacity())), 5, 256, 256);
            //dividers
            GuiComponent.blit(poseStack, x, y, 0, 110, 182, 5, 256, 256);
        }
    });
}
