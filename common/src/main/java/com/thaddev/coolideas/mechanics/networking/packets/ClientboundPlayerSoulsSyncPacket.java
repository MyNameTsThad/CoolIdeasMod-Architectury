package com.thaddev.coolideas.mechanics.networking.packets;

import com.thaddev.coolideas.client.ClientSoulsData;
import com.thaddev.coolideas.mechanics.data.PlayerSouls;
import com.thaddev.coolideas.mechanics.data.PlayerSoulsRetriever;
import dev.architectury.networking.NetworkManager;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;

import java.util.function.Supplier;

public class ClientboundPlayerSoulsSyncPacket {
    private int souls;

    private int customSoulCapacity;
    private boolean useCustomSoulCapacity;

    public ClientboundPlayerSoulsSyncPacket(int souls, int customSoulCapacity, boolean useCustomSoulCapacity) {
        this.souls = souls;
        this.customSoulCapacity = customSoulCapacity;
        this.useCustomSoulCapacity = useCustomSoulCapacity;
    }

    public ClientboundPlayerSoulsSyncPacket(FriendlyByteBuf buf) {
        this.souls = buf.readInt();
        this.customSoulCapacity = buf.readInt();
        this.useCustomSoulCapacity = buf.readBoolean();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(this.souls);
        buf.writeInt(this.customSoulCapacity);
        buf.writeBoolean(this.useCustomSoulCapacity);
    }

    public boolean apply(Supplier<NetworkManager.PacketContext> supplier) {
        if (Minecraft.getInstance().player != null) {
            PlayerSouls playerSouls = PlayerSoulsRetriever.get(Minecraft.getInstance().player);
            playerSouls.setSouls(this.souls);
            playerSouls.setCustomSoulCapacity(this.customSoulCapacity);
            playerSouls.setUseCustomSoulCapacity(this.useCustomSoulCapacity);
            ClientSoulsData.setSouls(this.souls);
            ClientSoulsData.setSoulCapacity(playerSouls.getSoulCapacity(Minecraft.getInstance().player));
            PlayerSoulsRetriever.set(playerSouls, Minecraft.getInstance().player);
        }
        return true;
    }
}
