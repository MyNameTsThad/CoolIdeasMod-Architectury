package com.thaddev.coolideas.mixin;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.thaddev.coolideas.content.items.SoulchargableItemUtils;
import com.thaddev.coolideas.mechanics.AbstractItemStackMixin;
import com.thaddev.coolideas.mechanics.data.PlayerSouls;
import com.thaddev.coolideas.mechanics.data.PlayerSoulsRetriever;
import com.thaddev.coolideas.mechanics.inits.TagsInit;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.awt.*;
import java.util.Optional;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements AbstractItemStackMixin {
    @Shadow
    public abstract Item getItem();

    @Shadow
    public abstract void removeTagKey(String pKey);

    private int maxDurability;
    private Entity owner;

    @Inject(method = "getMaxDamage", at = @At("RETURN"), cancellable = true)
    public void getMaxDamage(CallbackInfoReturnable<Integer> callbackInfoReturnable) {
        callbackInfoReturnable.setReturnValue(getMaxDurability());
    }

    @Inject(method = "inventoryTick", at = @At("TAIL"))
    public void inventoryTick(Level pLevel, Entity pEntity, int pInventorySlot, boolean pIsCurrentItem, CallbackInfo ci) {
        ItemStack thisStack;
        setOwner(pEntity);
        if ((thisStack = (ItemStack) (Object) this).is(TagsInit.SOULCHARGABLE) && pEntity instanceof Player player) {
            if (SoulchargableItemUtils.isCharged(thisStack)) {
                PlayerSouls playerSouls = PlayerSoulsRetriever.get(player);
                setMaxDurability(getItem().getMaxDamage() + (playerSouls.getSouls() / 2));
            }
        }
    }

    @Inject(method = "<init>(Lnet/minecraft/world/level/ItemLike;ILjava/util/Optional;)V", at = @At("TAIL"))
    public void constructor1(ItemLike itemLike, int i, Optional optional, CallbackInfo ci) {
        setMaxDurability(getItem().getMaxDamage());
    }

    @Inject(method = "<init>(Lnet/minecraft/nbt/CompoundTag;)V", at = @At("TAIL"))
    public void constructor2(CompoundTag pCompoundTag, CallbackInfo ci) {
        setMaxDurability(getItem().getMaxDamage());
    }


    @Override
    public int getMaxDurability() {
        return maxDurability;
    }

    @Override
    public Entity getOwner() {
        return owner;
    }

    @Override
    public void setOwner(Entity owner) {
        this.owner = owner;
    }

    @Override
    public void setMaxDurability(int durability) {
        this.maxDurability = durability;
    }

    //added to fix the bar width and color mismatch with the real durability

    @Inject(method = "getBarWidth", at = @At("RETURN"), cancellable = true)
    public void getBarWidth(CallbackInfoReturnable<Integer> cir) {
        if (SoulchargableItemUtils.isOverCharged((ItemStack) (Object) this)) {
            cir.setReturnValue((int) (SoulchargableItemUtils.getOverChargePercentDecimal((ItemStack) (Object) this) * 13));
            return;
        }
        cir.setReturnValue(
                Math.round(13.0F - (float) ((ItemStack) (Object) this).getDamageValue() * 13.0F / getMaxDurability())
        );
    }

    @Inject(method = "getBarColor", at = @At("RETURN"), cancellable = true)
    public void getBarColor(CallbackInfoReturnable<Integer> cir) {
        if (SoulchargableItemUtils.isOverCharged((ItemStack) (Object) this)) {
            // Define the starting and ending colors
            Color startColor = new Color(0x00ffff);
            Color endColor = new Color(0xe000ff);

            // Define the percentage as a decimal (0.0 to 1.0)
            double percentage = SoulchargableItemUtils.getOverChargePercentDecimal((ItemStack) (Object) this);

            // Interpolate between the colors based on the percentage
            int red = (int) (startColor.getRed() + percentage * (endColor.getRed() - startColor.getRed()));
            int green = (int) (startColor.getGreen() + percentage * (endColor.getGreen() - startColor.getGreen()));
            int blue = (int) (startColor.getBlue() + percentage * (endColor.getBlue() - startColor.getBlue()));
            Color interpolatedColor = new Color(red, green, blue);

            // Convert the interpolated color to an integer hex code
            int interpolatedHex = interpolatedColor.getRGB() & 0x00ffffff;
            cir.setReturnValue(interpolatedHex);
            return;
        }
        float f = Math.max(0.0F, (getMaxDurability() - (float) ((ItemStack) (Object) this).getDamageValue()) / getMaxDurability());
        cir.setReturnValue(
                Mth.hsvToRgb(f / 3.0F, 1.0F, 1.0F)
        );
    }

    @Inject(method = "isBarVisible", at = @At("RETURN"), cancellable = true)
    public void isBarVisible(CallbackInfoReturnable<Boolean> cir) {
        if (SoulchargableItemUtils.isOverCharged((ItemStack) (Object) this)) {
            cir.setReturnValue(true);
        }
    }

    @Redirect(method = "getTooltipLines", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;getAttributeModifiers(Lnet/minecraft/world/entity/EquipmentSlot;)Lcom/google/common/collect/Multimap;"))
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(ItemStack instance, EquipmentSlot optional) {
        //get original attribute modifiers
        Multimap<Attribute, AttributeModifier> multimap = (instance).getAttributeModifiers(optional);
        //filter out all modifiers have their modifier amount equal to 0 and add the rest into a new multimap
        Multimap<Attribute, AttributeModifier> newMultimap = multimap.entries().stream()
                .filter(entry -> entry.getValue().getName().startsWith("Soulcharged") || entry.getValue().getAmount() != 0)
                .collect(HashMultimap::create, (multimap1, entry) -> multimap1.put(entry.getKey(), entry.getValue()), HashMultimap::putAll);

        //if the item is a sword or axe, but the slot is an armor slot, return null
        //if the item is armor, but the slot is a hand slot, also return null
        if (instance.getItem() instanceof SwordItem || instance.getItem() instanceof AxeItem) {
            if (optional.getType() == EquipmentSlot.Type.HAND) {
                return newMultimap;
            } else {
                return HashMultimap.create();
            }
        }
        if (instance.getItem() instanceof ArmorItem armorPiece) {
            if (optional == armorPiece.getSlot()) {
                return newMultimap;
            } else {
                return HashMultimap.create();
            }
        }
        return HashMultimap.create();
    }

    @Redirect(method = "hurtAndBreak", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;hurt(ILnet/minecraft/util/RandomSource;Lnet/minecraft/server/level/ServerPlayer;)Z"))
    public boolean hurt(ItemStack instance, int amount, RandomSource random, ServerPlayer user) {
        if (SoulchargableItemUtils.isOverCharged((ItemStack) (Object) this)) {
            //decrement souls before damaging item
            SoulchargableItemUtils.incrementOverChargeProgress((ItemStack) (Object) this, -(amount));
        }
        return instance.hurt(0, random, user);
    }
}
