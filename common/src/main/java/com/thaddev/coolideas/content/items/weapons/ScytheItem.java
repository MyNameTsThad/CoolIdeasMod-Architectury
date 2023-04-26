package com.thaddev.coolideas.content.items.weapons;


import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.mojang.datafixers.util.Pair;
import com.thaddev.coolideas.content.ReachAttributeRetriever;
import com.thaddev.coolideas.content.entities.SoulOrb;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class ScytheItem extends SwordItem {
    private final Multimap<Attribute, AttributeModifier> defaultAttributeModifiers;

    protected static final Map<Block, Pair<Predicate<UseOnContext>, Consumer<UseOnContext>>> TILLABLES = Maps.newHashMap(ImmutableMap.of(Blocks.GRASS_BLOCK, Pair.of(HoeItem::onlyIfAirAbove, HoeItem.changeIntoState(Blocks.FARMLAND.defaultBlockState())), Blocks.DIRT_PATH, Pair.of(HoeItem::onlyIfAirAbove, HoeItem.changeIntoState(Blocks.FARMLAND.defaultBlockState())), Blocks.DIRT, Pair.of(HoeItem::onlyIfAirAbove, HoeItem.changeIntoState(Blocks.FARMLAND.defaultBlockState())), Blocks.COARSE_DIRT, Pair.of(HoeItem::onlyIfAirAbove, HoeItem.changeIntoState(Blocks.DIRT.defaultBlockState())), Blocks.ROOTED_DIRT, Pair.of(useOnContext -> true, HoeItem.changeIntoStateAndDropItem(Blocks.DIRT.defaultBlockState(), Items.HANGING_ROOTS))));

    public ScytheItem(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, double reach, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
        float attackDamage = (float) pAttackDamageModifier + pTier.getAttackDamageBonus();
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", attackDamage, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", pAttackSpeedModifier, AttributeModifier.Operation.ADDITION));
        builder.put(ReachAttributeRetriever.getReachAttribute(), new AttributeModifier("Weapon modifier", reach, AttributeModifier.Operation.ADDITION));
        this.defaultAttributeModifiers = builder.build();
    }

    @Override
    public @NotNull Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(@NotNull EquipmentSlot pEquipmentSlot) {
        return pEquipmentSlot == EquipmentSlot.MAINHAND ? this.defaultAttributeModifiers : super.getDefaultAttributeModifiers(pEquipmentSlot);
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext pContext) {
        BlockPos blockPos;
        Level level = pContext.getLevel();
        Pair<Predicate<UseOnContext>, Consumer<UseOnContext>> pair = TILLABLES.get(level.getBlockState(blockPos = pContext.getClickedPos()).getBlock());
        if (pair != null) {
            Predicate<UseOnContext> predicate = pair.getFirst();
            Consumer<UseOnContext> consumer = pair.getSecond();
            if (predicate.test(pContext)) {
                Player player2 = pContext.getPlayer();
                level.playSound(player2, blockPos, SoundEvents.HOE_TILL, SoundSource.BLOCKS, 1.0f, 1.0f);
                if (!level.isClientSide) {
                    consumer.accept(pContext);
                    if (player2 != null) {
                        pContext.getItemInHand().hurtAndBreak(1, player2, player -> player.broadcastBreakEvent(pContext.getHand()));
                    }
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
        } else if (level.getBlockState(blockPos).is(Blocks.SOUL_SAND) ||
                level.getBlockState(blockPos).is(Blocks.SOUL_SOIL) ||
                level.getBlockState(blockPos).is(Blocks.SOUL_TORCH) ||
                level.getBlockState(blockPos).is(Blocks.SOUL_WALL_TORCH) ||
                level.getBlockState(blockPos).is(Blocks.SOUL_FIRE) ||
                level.getBlockState(blockPos).is(Blocks.SOUL_CAMPFIRE) ||
                level.getBlockState(blockPos).is(Blocks.SOUL_LANTERN)
        ){
            Player player = pContext.getPlayer();
            level.playSound(player, blockPos, SoundEvents.SOUL_ESCAPE, SoundSource.BLOCKS, 1.0F, 1.0F);
            //right click with scythe to break block and harvest souls
            // soul sand -> 5 souls
            // soul soil -> 3 souls
            // soul torch, wall torch, lantern -> 1 soul (turn into normal torch, wall torch, lantern)
            // soul fire, campfire -> 2 souls (extinguish fire)
            if (!level.isClientSide) {
                if (player != null) {
                    if (level.getBlockState(blockPos).is(Blocks.SOUL_SAND)) {
                        level.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 11);
                        level.gameEvent(GameEvent.BLOCK_CHANGE, blockPos, GameEvent.Context.of(player, Blocks.AIR.defaultBlockState()));
                        pContext.getItemInHand().hurtAndBreak(1, player, (p_150845_) -> {
                            p_150845_.broadcastBreakEvent(pContext.getHand());
                        });
                        SoulOrb.award((ServerLevel) level, Vec3.atCenterOf(blockPos), 5);
                    }
                    else if (level.getBlockState(blockPos).is(Blocks.SOUL_SOIL)) {
                        level.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 11);
                        level.gameEvent(GameEvent.BLOCK_CHANGE, blockPos, GameEvent.Context.of(player, Blocks.AIR.defaultBlockState()));
                        pContext.getItemInHand().hurtAndBreak(1, player, (p_150845_) -> {
                            p_150845_.broadcastBreakEvent(pContext.getHand());
                        });
                        SoulOrb.award((ServerLevel) level, Vec3.atCenterOf(blockPos), 3);
                    }
                    else if (level.getBlockState(blockPos).is(Blocks.SOUL_TORCH)) {
                        level.setBlock(blockPos, Blocks.TORCH.defaultBlockState(), 1);
                        level.gameEvent(GameEvent.BLOCK_CHANGE, blockPos, GameEvent.Context.of(player, Blocks.TORCH.defaultBlockState()));
                        pContext.getItemInHand().hurtAndBreak(1, player, (p_150845_) -> {
                            p_150845_.broadcastBreakEvent(pContext.getHand());
                        });
                        SoulOrb.award((ServerLevel) level, Vec3.atCenterOf(blockPos), 1);
                    } else if (level.getBlockState(blockPos).is(Blocks.SOUL_WALL_TORCH)) {
                        level.setBlock(blockPos, Blocks.WALL_TORCH.defaultBlockState(), 1);
                        level.gameEvent(GameEvent.BLOCK_CHANGE, blockPos, GameEvent.Context.of(player, Blocks.WALL_TORCH.defaultBlockState()));
                        pContext.getItemInHand().hurtAndBreak(1, player, (p_150845_) -> {
                            p_150845_.broadcastBreakEvent(pContext.getHand());
                        });
                        SoulOrb.award((ServerLevel) level, Vec3.atCenterOf(blockPos), 1);
                    } else if (level.getBlockState(blockPos).is(Blocks.SOUL_LANTERN)) {
                        level.setBlock(blockPos, Blocks.LANTERN.defaultBlockState(), 1);
                        level.gameEvent(GameEvent.BLOCK_CHANGE, blockPos, GameEvent.Context.of(player, Blocks.LANTERN.defaultBlockState()));
                        pContext.getItemInHand().hurtAndBreak(1, player, (p_150845_) -> {
                            p_150845_.broadcastBreakEvent(pContext.getHand());
                        });
                        SoulOrb.award((ServerLevel) level, Vec3.atCenterOf(blockPos), 1);
                    } else if (level.getBlockState(blockPos).is(Blocks.SOUL_FIRE)) {
                        BlockState old = level.getBlockState(blockPos);
                        BlockState newBlock = Blocks.FIRE.defaultBlockState()
                                .setValue(FireBlock.AGE, old.getValue(FireBlock.AGE))
                                .setValue(FireBlock.EAST, old.getValue(FireBlock.EAST))
                                .setValue(FireBlock.NORTH, old.getValue(FireBlock.NORTH))
                                .setValue(FireBlock.SOUTH, old.getValue(FireBlock.SOUTH))
                                .setValue(FireBlock.WEST, old.getValue(FireBlock.WEST))
                                .setValue(FireBlock.UP, old.getValue(FireBlock.UP));
                        level.setBlock(blockPos, newBlock, 1);
                        SoulOrb.award((ServerLevel) level, Vec3.atCenterOf(blockPos), 2);
                    } else if (level.getBlockState(blockPos).is(Blocks.SOUL_CAMPFIRE)) {
                        BlockState old = level.getBlockState(blockPos);
                        BlockState newBlock = Blocks.CAMPFIRE.defaultBlockState()
                                .setValue(CampfireBlock.LIT, old.getValue(CampfireBlock.LIT))
                                .setValue(CampfireBlock.SIGNAL_FIRE, old.getValue(CampfireBlock.SIGNAL_FIRE))
                                .setValue(CampfireBlock.WATERLOGGED, old.getValue(CampfireBlock.WATERLOGGED));
                        level.setBlock(blockPos, newBlock, 1);
                        SoulOrb.award((ServerLevel) level, Vec3.atCenterOf(blockPos), 2);
                    }
                }
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        } else {
            return InteractionResult.PASS;
        }
        return InteractionResult.PASS;
    }
}
