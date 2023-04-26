package com.thaddev.coolideas.mechanics;

import net.minecraft.world.entity.Entity;

public interface AbstractItemStackMixin {
    int getMaxDurability();
    void setMaxDurability(int durability);

    Entity getOwner();

    void setOwner(Entity owner);
}
