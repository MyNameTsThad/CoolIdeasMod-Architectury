package com.thaddev.coolideas.mechanics.networking;

import com.thaddev.coolideas.CoolIdeasMod;
import com.thaddev.coolideas.mechanics.networking.packets.ClientboundPlayerSoulsSyncPacket;
import dev.architectury.networking.NetworkChannel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class Packets {
    private static NetworkChannel INSTANCE;
    public static void register(){
        NetworkChannel net = NetworkChannel.create(new ResourceLocation(CoolIdeasMod.MODID, "networking"));

        INSTANCE = net;

        net.register(ClientboundPlayerSoulsSyncPacket.class,
                        ClientboundPlayerSoulsSyncPacket::encode,
                        ClientboundPlayerSoulsSyncPacket::new,
                        ClientboundPlayerSoulsSyncPacket::apply);
    }

    public static <MSG> void sendToServer(MSG msg) {
        INSTANCE.sendToServer(msg);
    }

    public static <MSG> void sendToPlayer(MSG msg, ServerPlayer player) {
        INSTANCE.sendToPlayer(player, msg);
    }
}
