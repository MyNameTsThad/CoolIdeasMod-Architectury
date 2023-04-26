package com.thaddev.coolideas.mixin;

import com.thaddev.coolideas.content.items.SoulchargableItemUtils;
import com.thaddev.coolideas.mechanics.AbstractItemStackMixin;
import com.thaddev.coolideas.mechanics.data.PlayerSouls;
import com.thaddev.coolideas.mechanics.data.PlayerSoulsRetriever;
import com.thaddev.coolideas.mechanics.inits.TagsInit;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Inventory.class)
public abstract class InventoryMixin {
    @Shadow @Final public NonNullList<ItemStack> armor;

    @Shadow @Final public Player player;

    @Inject(method = "tick", at = @At("TAIL"))
    public void onArmorTick(CallbackInfo ci) {
        armor.forEach(itemStack -> {
            ((AbstractItemStackMixin)(Object)itemStack).setOwner(player);
            if (itemStack.is(TagsInit.SOULCHARGABLE)) {
                if (SoulchargableItemUtils.isCharged(itemStack)) {
                    PlayerSouls playerSouls = PlayerSoulsRetriever.get(player);
                    ((AbstractItemStackMixin)(Object)itemStack).setMaxDurability(itemStack.getItem().getMaxDamage() + (playerSouls.getSouls() / 2));
                }
            }
        });
    }
}
