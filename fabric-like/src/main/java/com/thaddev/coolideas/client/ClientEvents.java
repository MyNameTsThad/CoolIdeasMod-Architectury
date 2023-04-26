package com.thaddev.coolideas.client;


import com.thaddev.coolideas.CoolIdeasMod;
import com.thaddev.coolideas.Utils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.Minecraft;

@Environment(EnvType.CLIENT)
public class ClientEvents {
    public static void registerClientEvents() {
        CoolIdeasMod.LOGGER.debug("Registering Client Events for " + CoolIdeasMod.MODID); //it now does something :sunglasses:

        HudRenderCallback.EVENT.register((matrixStack, tickDelta) -> {
            if (Minecraft.getInstance().player != null){
                int souls = ClientSoulsData.getSouls();
                int maxSouls = ClientSoulsData.getSoulCapacity();
                Minecraft.getInstance().font.drawShadow(matrixStack, Utils.fromNoTag("(%$aqua)Souls: " + souls + " / " + maxSouls), 10, 10, 100);
            }
        });
    }
}
