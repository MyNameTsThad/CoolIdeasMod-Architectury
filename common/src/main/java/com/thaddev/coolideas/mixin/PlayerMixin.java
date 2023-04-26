package com.thaddev.coolideas.mixin;

import com.thaddev.coolideas.Utils;
import com.thaddev.coolideas.mechanics.data.PlayerSouls;
import com.thaddev.coolideas.mechanics.data.PlayerSoulsRetriever;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class PlayerMixin {
    @Inject(at = @At("TAIL"), method = "getCurrentItemAttackStrengthDelay", cancellable = true)
    public void getCurrentItemAttackStrengthDelay(CallbackInfoReturnable<Float> cir) {
        Player self = (Player) (Object) this;
        PlayerSouls playerSouls = PlayerSoulsRetriever.get(self);
        cir.setReturnValue(
                (float) (1.0D / (self.getAttributeValue(Attributes.ATTACK_SPEED) * Utils.calculateScaledBonus(playerSouls, self)) * 20.0D)
        );
    }
}
