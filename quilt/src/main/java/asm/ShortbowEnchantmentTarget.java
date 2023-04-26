package asm;

import com.thaddev.coolideas.content.items.weapons.DiamondShortBowItem;
import com.thaddev.coolideas.content.items.weapons.IronShortBowItem;
import com.thaddev.coolideas.mixin.fabric.EnchantmentCategoryMixin;
import net.minecraft.world.item.Item;

public class ShortbowEnchantmentTarget extends EnchantmentCategoryMixin {
    @Override
    public boolean canEnchant(Item item) {
        return (item instanceof IronShortBowItem || item instanceof DiamondShortBowItem);
    }
}
