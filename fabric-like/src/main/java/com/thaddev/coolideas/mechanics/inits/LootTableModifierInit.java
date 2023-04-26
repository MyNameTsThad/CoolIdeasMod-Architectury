package com.thaddev.coolideas.mechanics.inits;

import com.thaddev.coolideas.CoolIdeasMod;
import com.thaddev.coolideas.content.items.materials.MicrochipItem;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.functions.SetNbtFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

public class LootTableModifierInit {
    private static final ResourceLocation DUNGEON_CHEST = new ResourceLocation("minecraft", "chests/simple_dungeon");
    private static final ResourceLocation JUNGLE_TEMPLE = new ResourceLocation("minecraft", "chests/jungle_temple_dispenser");
    private static final ResourceLocation DESERT_TEMPLE = new ResourceLocation("minecraft", "chests/desert_pyramid");
    private static final ResourceLocation BASTION_BRIDGE = new ResourceLocation("minecraft", "chests/bastion_bridge");
    private static final ResourceLocation BASTION_HOGLIN_STABLE = new ResourceLocation("minecraft", "chests/bastion_hoglin_stable");
    private static final ResourceLocation BASTION_TREASURE = new ResourceLocation("minecraft", "chests/bastion_treasure");
    private static final ResourceLocation BASTION_OTHER = new ResourceLocation("minecraft", "chests/bastion_other");

    public static void modifyLootTables() {
        CoolIdeasMod.LOGGER.debug("Modifying Loot tables for " + CoolIdeasMod.MODID + " (4/11)");
        LootTableEvents.MODIFY.register(((resourceManager, lootManager, id, tableBuilder, source) -> {
            if (DUNGEON_CHEST.equals(id) || JUNGLE_TEMPLE.equals(id) || DESERT_TEMPLE.equals(id)) {
                LootPool.Builder poolBuilder = LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1))
                        .conditionally(LootItemRandomChanceCondition.randomChance(0.1f).build())
                        .with(LootItem.lootTableItem(ItemInit.MICROCHIP.get()).build())
                        .apply(SetNbtFunction.setTag(MicrochipItem.getType(MicrochipItem.MicrochipTypes.HOMING)))
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1f, 1f)).build());
                tableBuilder.pool(poolBuilder.build());
            }
            if (BASTION_BRIDGE.equals(id) || BASTION_HOGLIN_STABLE.equals(id) || BASTION_TREASURE.equals(id) || BASTION_OTHER.equals(id)) {
                LootPool.Builder poolBuilder = LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1))
                        .conditionally(LootItemRandomChanceCondition.randomChance(0.25f).build())
                        .with(LootItem.lootTableItem(ItemInit.MICROCHIP.get()).build())
                        .apply(SetNbtFunction.setTag(MicrochipItem.getType(MicrochipItem.MicrochipTypes.HOMING)))
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1f, 1f)).build());
                tableBuilder.pool(poolBuilder.build());
            }
        }));
    }
}
