package com.thaddev.coolideas.client;

public class ClientSoulsData {
    private static int souls;
    private static int soulCapacity;

    public static int getSouls() {
        return souls;
    }
    public static void setSouls(int souls) {
        ClientSoulsData.souls = souls;
        //CoolIdeasMod.LOGGER.info("ClientSoulsData.souls = " + souls);
    }

    public static int getSoulCapacity() {
        return soulCapacity;
    }

    public static void setSoulCapacity(int soulCapacity) {
        ClientSoulsData.soulCapacity = soulCapacity;
        //CoolIdeasMod.LOGGER.info("ClientSoulsData.soulCapacity = " + soulCapacity);
    }
}
