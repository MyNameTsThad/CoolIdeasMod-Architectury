package com.thaddev.coolideas.mechanics.networking.packets;

public class ServerboundUseItemAbilityPacket {
//    private InteractionHand hand;
//    private int sequence;
//
//    public ServerboundUseItemAbilityPacket(InteractionHand hand, int sequence){
//
//    }
//
//    public ServerboundUseItemAbilityPacket(FriendlyByteBuf buf){
//        this.souls = buf.readInt();
//        this.customSoulCapacity = buf.readInt();
//        this.useCustomSoulCapacity = buf.readBoolean();
//    }
//
//    public void toBytes(FriendlyByteBuf buf){
//        buf.writeInt(this.souls);
//        buf.writeInt(this.customSoulCapacity);
//        buf.writeBoolean(this.useCustomSoulCapacity);
//    }
//
//    public boolean handle(Supplier<NetworkEvent.Context> supplier){
//        NetworkEvent.Context context = supplier.get();
//        context.enqueueWork(() -> {
//            //client
//            if (Minecraft.getInstance().player != null) {
//                Minecraft.getInstance().player.getCapability(PlayerSoulsProvider.PLAYER_SOULS).ifPresent(playerSouls -> {
//                    playerSouls.setSouls(this.souls);
//                    playerSouls.setCustomSoulCapacity(this.customSoulCapacity);
//                    playerSouls.setUseCustomSoulCapacity(this.useCustomSoulCapacity);
//                    ClientSoulsData.setSouls(this.souls);
//                    ClientSoulsData.setSoulCapacity(playerSouls.getSoulCapacity(Minecraft.getInstance().player));
//                });
//            }
//        });
//        return true;
//    }
}
