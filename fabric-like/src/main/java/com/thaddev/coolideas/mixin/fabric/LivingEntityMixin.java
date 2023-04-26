package com.thaddev.coolideas.mixin.fabric;

import com.thaddev.coolideas.content.entities.SoulOrb;
import com.thaddev.coolideas.content.entities.projectiles.DiamondHeadedArrow;
import com.thaddev.coolideas.content.entities.projectiles.ShortBowArrow;
import com.thaddev.coolideas.mechanics.inits.EffectInit;
import com.thaddev.coolideas.mechanics.inits.ItemInit;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Inject(method = "hurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;actuallyHurt(Lnet/minecraft/world/damagesource/DamageSource;F)V", shift = At.Shift.BEFORE))
    public void damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity thisEntity = (LivingEntity) (Object) this;
        MobEffectInstance effect;
        if ((effect = thisEntity.getEffect(EffectInit.VULNERABILITY.get())) != null && thisEntity.invulnerableTime > 10f) {
            int amplifier = Math.min(effect.getAmplifier() + 1, 4);
            int toReduce = amplifier > 3 ? ((amplifier - 1) * 2) + 3 : amplifier * 2;
            thisEntity.invulnerableTime = 20 - toReduce;
        }
    }

    @Inject(method = "actuallyHurt", at = @At("HEAD"))
    public void actuallyHurt(DamageSource source, float amount, CallbackInfo ci) {
        LivingEntity thisEntity = (LivingEntity) (Object) this;
        if (!thisEntity.isInvulnerableTo(source)) {
            if ((source.getDirectEntity() instanceof DiamondHeadedArrow || source.getDirectEntity() instanceof ShortBowArrow) && source.getEntity() instanceof ServerPlayer player) {
                player.level.playSound(null, player.position().x, player.position().y, player.position().z, SoundEvents.ARROW_HIT_PLAYER, SoundSource.PLAYERS, 0.3F, 0.5F);
            }
        }
    }

    @Inject(method = "dropExperience", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ExperienceOrb;award(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/phys/Vec3;I)V", shift = At.Shift.BEFORE))
    public void dropExperience(CallbackInfo ci) {
        LivingEntity thisEntity = (LivingEntity) (Object) this;
        if (thisEntity.getKillCredit() != null && thisEntity.getKillCredit() instanceof Player player &&
            (player.getMainHandItem().is(ItemInit.SCYTHE.get()) ||
                player.getOffhandItem().is(ItemInit.SCYTHE.get()))) {
            if (!thisEntity.getLevel().isClientSide()) {
                SoulOrb.award((ServerLevel) thisEntity.getLevel(), thisEntity.position(), ((LivingEntityAccessor) thisEntity).callGetExperienceReward());
            }
        }
    }

    @Inject(method = "knockback", at = @At("HEAD"), cancellable = true)
    public void knockback(double strength, double x, double z, CallbackInfo ci) {
        LivingEntity thisEntity = (LivingEntity) (Object) this;
        if (thisEntity.getKillCredit() instanceof Player player && player.getMainHandItem().is(ItemInit.SCYTHE.get())) {
            strength *= -1;
            strength *= 1.0D - thisEntity.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE);

            thisEntity.hasImpulse = true;
            Vec3 vec3 = thisEntity.getDeltaMovement();
            Vec3 vec31 = (new Vec3(x, 0, z)).normalize().scale(strength);
            thisEntity.setDeltaMovement(vec3.x / 2.0D - vec31.x, thisEntity.isOnGround() ? Math.min(0.4D, vec3.y / 2.0D + (-strength)) : vec3.y, vec3.z / 2.0D - vec31.z);

            ci.cancel();
        }
    }
}
