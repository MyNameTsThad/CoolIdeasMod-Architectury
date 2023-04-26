package com.thaddev.coolideas.mixin.fabric;

import com.thaddev.coolideas.mechanics.inits.ItemInit;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.MultiShotEnchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MultiShotEnchantment.class)
public class MultishotEnchantmentMixin extends EnchantmentMixin {
    @Inject(method = "getMaxLevel", at = @At("HEAD"), cancellable = true)
    public void getMaxLevel(CallbackInfoReturnable<Integer> callbackInfoReturnable) {
        callbackInfoReturnable.setReturnValue(2);
    }

    @Override
    protected void customCanEnchant(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (stack.is(ItemInit.DIAMOND_SHORTBOW.get()) || stack.is(ItemInit.IRON_SHORTBOW.get())) {
            cir.setReturnValue(true);
        }
    }
}