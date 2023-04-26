package com.thaddev.coolideas.mixin.fabric;

import com.thaddev.coolideas.CoolIdeasMod;
import com.thaddev.coolideas.util.EntityDataSaver;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityDataSaverMixin implements EntityDataSaver {
    private CompoundTag persistentData;

    @Override
    public CompoundTag getPersistentData() {
        if(this.persistentData == null) {
            this.persistentData = new CompoundTag();
        }
        return persistentData;
    }

    @Inject(method = "save", at = @At("HEAD"))
    protected void save(CompoundTag nbt, CallbackInfoReturnable<CompoundTag> cir) {
        if(persistentData != null) {
            nbt.put(CoolIdeasMod.MODID + ".properties", persistentData);
        }
    }

    @Inject(method = "load", at = @At("HEAD"))
    protected void load(CompoundTag nbt, CallbackInfo ci) {
        if (nbt.contains(CoolIdeasMod.MODID + ".properties", 10)) {
            persistentData = nbt.getCompound(CoolIdeasMod.MODID + ".properties");
        }
    }
}
