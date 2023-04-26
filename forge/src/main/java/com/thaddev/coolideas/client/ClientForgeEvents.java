package com.thaddev.coolideas.client;

import com.thaddev.coolideas.CoolIdeasMod;
import com.thaddev.coolideas.Utils;
import com.thaddev.coolideas.content.items.SoulchargableItemUtils;
import com.thaddev.coolideas.mechanics.capabilities.PlayerSoulsProvider;
import com.thaddev.coolideas.mechanics.inits.TagsInit;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.awt.*;
import java.text.DecimalFormat;

import static com.thaddev.coolideas.Utils.component;

@Mod.EventBusSubscriber(modid = CoolIdeasMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientForgeEvents {
    @SubscribeEvent
    public static void onRenderHud(final RenderGuiOverlayEvent.Post event) {
        if (Minecraft.getInstance().player != null){
            Minecraft.getInstance().player.getCapability(PlayerSoulsProvider.PLAYER_SOULS).ifPresent(playerSouls -> {
                ClientSoulsData.setSouls(playerSouls.getSouls());
                ClientSoulsData.setSoulCapacity(playerSouls.getSoulCapacity(Minecraft.getInstance().player));
                if (Minecraft.getInstance().options.advancedItemTooltips) {
                    Minecraft.getInstance().font.draw(event.getPoseStack(), Utils.fromNoTag("(%$aqua)Souls: " + playerSouls.getSouls() + " / " + playerSouls.getSoulCapacity(Minecraft.getInstance().player)), 10, 20, 100);
                }
            });
        }
    }

    @SubscribeEvent
    public static void onTooltip(final ItemTooltipEvent event) {
        if (event.getItemStack().is(TagsInit.SOULCHARGABLE)) {
            boolean advancedTooltips = Minecraft.getInstance().options.advancedItemTooltips;
            int progress = SoulchargableItemUtils.getSoulChargeProgress(event.getItemStack());
            float overchargePercent = SoulchargableItemUtils.getOverChargePercentDecimal(event.getItemStack());
            if (SoulchargableItemUtils.isCharged(event.getItemStack())) {
                event.getToolTip().add(1, component(Utils.fromNoTag("(%$italic)(%$aqua)It's potential has been unlocked.")));
            } else if (progress != 0) {
                if (progress < 1000) {
                    event.getToolTip().add(1, component(Utils.fromNoTag("(%$italic)You feel tingly..."))
                            .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#606eff"))));
                } else if (progress < 2000) {
                    event.getToolTip().add(1, component(Utils.fromNoTag("(%$italic)You can start to feel its power..."))
                            .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#4d8bff"))));
                } else if (progress < 3000) {
                    event.getToolTip().add(1, component(Utils.fromNoTag("(%$italic)You feel it linking with you soul..."))
                            .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#3aa8ff"))));
                } else if (progress < 4000) {
                    event.getToolTip().add(1, component(Utils.fromNoTag("(%$italic)It's starting to glow..."))
                            .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#27c5ff"))));
                } else if (progress < 5000) {
                    event.getToolTip().add(1, component(Utils.fromNoTag("(%$italic)It feels heavy."))
                            .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#14e2ff"))));
                }
            }
            if (overchargePercent != 0) {
                Color startColor = new Color(0x00ffff);
                Color endColor = new Color(0xe000ff);

                // Define the percentage as a decimal (0.0 to 1.0)
                double percentage = SoulchargableItemUtils.getOverChargePercentDecimal(event.getItemStack());

                // Interpolate between the colors based on the percentage
                int red = (int) (startColor.getRed() + percentage * (endColor.getRed() - startColor.getRed()));
                int green = (int) (startColor.getGreen() + percentage * (endColor.getGreen() - startColor.getGreen()));
                int blue = (int) (startColor.getBlue() + percentage * (endColor.getBlue() - startColor.getBlue()));
                Color interpolatedColor = new Color(red, green, blue);

                // Convert the interpolated color to an integer hex code
                int interpolatedHex = interpolatedColor.getRGB() & 0x00ffffff;
                String hexCode = "#" + Integer.toHexString(interpolatedHex).toUpperCase();

                event.getToolTip().add(1, component(Utils.fromNoTag("Overcharged " +
                        new DecimalFormat("0.00").format(overchargePercent * 100)
                        + "% (" + SoulchargableItemUtils.getOverChargeProgress(event.getItemStack()) + "/10000)"))
                        .withStyle(Style.EMPTY.withColor(TextColor.parseColor(hexCode))));
            }
            if (advancedTooltips) {
                event.getToolTip().add(progress == 0 ? 1 : 2, component(Utils.fromNoTag("(%$italic)(%$dark_gray)" +
                        new DecimalFormat("0.00").format((((float) progress) / 5000) * 100)
                        + "% (" + progress + "/5000)")));
            }
        }
    }

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event){
        
    }
}
