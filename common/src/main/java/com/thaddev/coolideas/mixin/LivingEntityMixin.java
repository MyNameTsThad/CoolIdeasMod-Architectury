package com.thaddev.coolideas.mixin;

import com.thaddev.coolideas.Utils;
import com.thaddev.coolideas.mechanics.data.PlayerSouls;
import com.thaddev.coolideas.mechanics.data.PlayerSoulsRetriever;
import net.minecraft.world.damagesource.CombatRules;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.atomic.AtomicReference;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Shadow
    protected abstract void hurtArmor(DamageSource pDamageSource, float pDamageAmount);

    @Shadow
    public abstract int getArmorValue();

    @Shadow
    public abstract double getAttributeValue(Attribute pAttribute);

    @Inject(at = @At("HEAD"), method = "knockback", cancellable = true)
    public void knockback(double strength, double x, double z, CallbackInfo ci) {
        LivingEntity self = (LivingEntity) (Object) this;
        strength *= 1.0 - self.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE);
        self.hasImpulse = true;
        Vec3 vec3 = self.getDeltaMovement();
        Vec3 vec32 = new Vec3(x, 0.0, z).normalize().scale(strength);
        self.setDeltaMovement(vec3.x / 2.0 - vec32.x, self.isOnGround() ? Math.min(0.4, vec3.y / 2.0 + strength) : vec3.y, vec3.z / 2.0 - vec32.z);
        ci.cancel();
    }

    @Inject(at = @At("TAIL"), method = "getDamageAfterArmorAbsorb", cancellable = true)
    public void getDamageAfterArmorAbsorb(DamageSource pDamageSource, float pDamageAmount, CallbackInfoReturnable<Float> cir) {
        LivingEntity self = (LivingEntity) (Object) this;
        AtomicReference<Float> f = new AtomicReference<>(pDamageAmount);
        if (!pDamageSource.isBypassArmor() && self instanceof Player player) {
            PlayerSouls playerSouls = PlayerSoulsRetriever.get(player);
            hurtArmor(pDamageSource, f.get());
            f.set(CombatRules.getDamageAfterAbsorb(f.get(), (float) getArmorValue(),
                    (float) getAttributeValue(Attributes.ARMOR_TOUGHNESS) * Utils.calculateScaledBonus(playerSouls, (Player) self)
            ));
        }

        cir.setReturnValue(f.get());
    }
}
