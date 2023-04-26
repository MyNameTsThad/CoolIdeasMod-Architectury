package com.thaddev.coolideas.mechanics.keybinds;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.lwjgl.glfw.GLFW;

public class KeybindUtils {
    public static final String KEY_CATEGORY_ITEMABILITIES = "key.category.coolideas.itemabilities";
    public static final String KEY_MAINHAND_1 = "key.coolideas.mainhand1";
    public static final String KEY_MAINHAND_2 = "key.coolideas.mainhand2";
    public static final String KEY_OFFHAND = "key.coolideas.offhand";
    public static final String KEY_HELMET = "key.coolideas.helmet";
    public static final String KEY_CHESTPLATE = "key.coolideas.chestplate";
    public static final String KEY_LEGGINGS = "key.coolideas.leggings";
    public static final String KEY_BOOTS = "key.coolideas.boots";
    public static final String KEY_ULTIMATE = "key.coolideas.ultimate";

    public static final KeyMapping MAINHAND_1 = new KeyMapping(KEY_MAINHAND_1,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_R, KEY_CATEGORY_ITEMABILITIES);
    public static final KeyMapping MAINHAND_2 = new KeyMapping(KEY_MAINHAND_2,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_F, KEY_CATEGORY_ITEMABILITIES);
    public static final KeyMapping OFFHAND = new KeyMapping(KEY_OFFHAND,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_X, KEY_CATEGORY_ITEMABILITIES);
    public static final KeyMapping HELMET = new KeyMapping(KEY_HELMET,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_T, KEY_CATEGORY_ITEMABILITIES);
    public static final KeyMapping CHESTPLATE = new KeyMapping(KEY_CHESTPLATE,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_C, KEY_CATEGORY_ITEMABILITIES);
    public static final KeyMapping LEGGINGS = new KeyMapping(KEY_LEGGINGS,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_G, KEY_CATEGORY_ITEMABILITIES);
    public static final KeyMapping BOOTS = new KeyMapping(KEY_BOOTS,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_V, KEY_CATEGORY_ITEMABILITIES);
    public static final KeyMapping ULTIMATE = new KeyMapping(KEY_ULTIMATE,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_Z, KEY_CATEGORY_ITEMABILITIES);

    @FunctionalInterface
    public interface KeybindAction {
        void execute(Level level, Player player);
    }

}
