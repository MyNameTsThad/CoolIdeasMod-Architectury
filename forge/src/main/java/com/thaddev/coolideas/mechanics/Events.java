package com.thaddev.coolideas.mechanics;

import com.thaddev.coolideas.CoolIdeasMod;
import com.thaddev.coolideas.Utils;
import com.thaddev.coolideas.content.blocks.SoulBottleBlock;
import com.thaddev.coolideas.content.blocks.SoulVialBlock;
import com.thaddev.coolideas.content.entities.SoulOrb;
import com.thaddev.coolideas.content.entities.projectiles.DiamondHeadedArrow;
import com.thaddev.coolideas.content.entities.projectiles.ShortBowArrow;
import com.thaddev.coolideas.content.items.SoulchargableItemUtils;
import com.thaddev.coolideas.content.items.materials.SoulContainerBlockItem;
import com.thaddev.coolideas.mechanics.capabilities.PlayerSoulsProvider;
import com.thaddev.coolideas.mechanics.data.PlayerSouls;
import com.thaddev.coolideas.mechanics.inits.BlockInit;
import com.thaddev.coolideas.mechanics.inits.EffectInit;
import com.thaddev.coolideas.mechanics.inits.ItemInit;
import com.thaddev.coolideas.mechanics.inits.TagsInit;
import com.thaddev.coolideas.mechanics.networking.Packets;
import com.thaddev.coolideas.mechanics.networking.packets.ClientboundPlayerSoulsSyncPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Objects;

@Mod.EventBusSubscriber(modid = CoolIdeasMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class Events {
    @SubscribeEvent
    public static void onPlayerStripAxe(final BlockEvent.BlockToolModificationEvent event) {
        if (!event.isSimulated() && event.getPlayer() instanceof ServerPlayer player
                && event.getToolAction() == ToolActions.AXE_STRIP
                && event.getState().is(TagsInit.REGULAR_LOGS)) {
            Level level = player.getLevel();
            BlockPos blockpos = event.getPos();

            InteractionHand otherHand = event.getContext().getHand() == InteractionHand.MAIN_HAND ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
            if (player.getItemInHand(otherHand).getItem() == Items.GLASS_BOTTLE) {
                ItemStack stack = player.getItemInHand(otherHand);
                ItemStack newStack = new ItemStack(ItemInit.RAW_RUBBER_BOTTLE.get(), 1);
                stack.shrink(1);
                player.level.playSound(null, blockpos.getX(), blockpos.getY(), blockpos.getZ(), SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1F, 1F);
                if (stack.isEmpty()) {
                    player.getInventory().removeItem(stack);
                    player.setItemInHand(otherHand, newStack);
                } else {
                    if (!player.addItem(newStack)) {
                        ItemEntity drop = new ItemEntity(level, blockpos.getX(), blockpos.getY(), blockpos.getZ(), newStack);
                        level.addFreshEntity(drop);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onLivingHurt(final LivingHurtEvent event) {
        if (event.getSource().getEntity() instanceof Player attacker &&
                Objects.equals(event.getSource().getEntity(), event.getSource().getDirectEntity())){ //melee hit
            // check if we're attacking with melee weapons
            ItemStack stack = attacker.getMainHandItem();
            if (stack.getItem() instanceof SwordItem ||
                    stack.getItem() instanceof AxeItem ||
                    stack.getItem() instanceof TridentItem) {
                if (stack.is(TagsInit.SOULCHARGABLE) && SoulchargableItemUtils.isCharged(stack) &&
                        !attacker.getCooldowns().isOnCooldown(stack.getItem())){
                    attacker.getCapability(PlayerSoulsProvider.PLAYER_SOULS).ifPresent(playerSouls -> {
                        //scale percent from 0 - 0.5 depending on xp level 0 - 30
                        event.setAmount(event.getAmount() * Utils.calculateScaledBonus(playerSouls, attacker));
                    });
                }
            }
        }

        //reduce the iframe time when the player is hit by a vulnerability effect
        MobEffectInstance effect;
        if ((effect = event.getEntity().getEffect(EffectInit.VULNERABILITY.get())) != null) {
            int amplifier = Math.min(effect.getAmplifier() + 1, 4);
            int toReduce = amplifier > 3 ? ((amplifier - 1) * 2) + 3 : amplifier * 2;
            event.getEntity().invulnerableTime = 20 - toReduce;
        }
        if ((event.getSource().getDirectEntity() instanceof DiamondHeadedArrow | event.getSource().getDirectEntity() instanceof ShortBowArrow) && event.getSource().getEntity() instanceof Player player) {
            player.level.playSound(null, player.position().x, player.position().y, player.position().z, SoundEvents.ARROW_HIT_PLAYER, SoundSource.PLAYERS, 0.3F, 0.5F);
        }

        //reduce player souls by how much damage they took if their health is less than 50% of their maximum health
        //whilst dropping soul orbs in the lost amount in the process
        if (event.getEntity() instanceof Player player && player.getHealth() < player.getMaxHealth() / 2 && !player.getLevel().isClientSide()) {
            player.getCapability(PlayerSoulsProvider.PLAYER_SOULS).ifPresent(playerSouls -> {
                playerSouls.addSouls(-(int) (event.getAmount()));
                Packets.sendToPlayer(new ClientboundPlayerSoulsSyncPacket(playerSouls.getSouls(), playerSouls.getSavedSoulCapacity(), playerSouls.isUseCustomSoulCapacity()), (ServerPlayer) event.getEntity());
                SoulOrb.award((ServerLevel) event.getEntity().getLevel(), player.position(), (int) (event.getAmount()));
            });
        }
    }

    @SubscribeEvent
    public static void onAttachCapabilitiesEvent(final AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player player) {
            if (!player.getCapability(PlayerSoulsProvider.PLAYER_SOULS).isPresent()) {
                event.addCapability(new ResourceLocation(CoolIdeasMod.MODID, "properties"), new PlayerSoulsProvider());
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerCloned(final PlayerEvent.Clone event) {
        event.getOriginal().reviveCaps();
        event.getOriginal().getCapability(PlayerSoulsProvider.PLAYER_SOULS).ifPresent(oldPlayerSouls -> {
            if (event.isWasDeath() && !event.getOriginal().getLevel().getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY)) {
                SoulOrb.award((ServerLevel) event.getOriginal().getLevel(), event.getOriginal().position(), oldPlayerSouls.getSouls());
            } else {
                event.getEntity().getCapability(PlayerSoulsProvider.PLAYER_SOULS).ifPresent(newPlayerSouls -> {
                    newPlayerSouls.copyFrom(oldPlayerSouls);
                    Packets.sendToPlayer(new ClientboundPlayerSoulsSyncPacket(newPlayerSouls.getSouls(), newPlayerSouls.getSavedSoulCapacity(), newPlayerSouls.isUseCustomSoulCapacity()), (ServerPlayer) event.getEntity());
                });
            }
        });
        event.getOriginal().invalidateCaps();
    }

    @SubscribeEvent
    public static void onRegisterCapabilities(final RegisterCapabilitiesEvent event) {
        event.register(PlayerSouls.class);
    }

    @SubscribeEvent
    public static void onDropExperience(final LivingExperienceDropEvent event) {
        if (event.getAttackingPlayer() != null &&
                (event.getAttackingPlayer().getItemInHand(InteractionHand.MAIN_HAND).is(ItemInit.SCYTHE.get()) ||
                        event.getAttackingPlayer().getItemInHand(InteractionHand.OFF_HAND).is(ItemInit.SCYTHE.get()))) {
            if (!event.getEntity().getLevel().isClientSide) {
                SoulOrb.award((ServerLevel) event.getEntity().getLevel(), event.getEntity().position(), event.getDroppedExperience());
            }
        }
    }

    @SubscribeEvent
    public static void onKnockback(final LivingKnockBackEvent event) {
        LivingEntity attacker;
        if ((attacker = event.getEntity().getKillCredit()) != null && attacker.getItemInHand(InteractionHand.MAIN_HAND).is(ItemInit.SCYTHE.get())) {
            event.setStrength(event.getStrength() * -1);
        }
        if (event.getEntity() instanceof Player player) {
            player.getCapability(PlayerSoulsProvider.PLAYER_SOULS).ifPresent(playerSouls -> {
                event.setStrength(event.getStrength() * (1.0f - (Utils.calculateScaledBonus(playerSouls, player) - 1f)));
            });
        }
    }

    @SubscribeEvent
    public static void onUseBlock(final PlayerInteractEvent.RightClickBlock event) {
        if (event.getEntity().getItemInHand(event.getHand()).is(TagsInit.SOULCHARGABLE)) {
            if (event.getLevel().getBlockState(event.getPos()).getBlock() == BlockInit.SOUL_VIAL.get() ||
                    event.getLevel().getBlockState(event.getPos()).getBlock() == BlockInit.SOUL_BOTTLE.get() ||
                    event.getLevel().getBlockState(event.getPos()).getBlock() == BlockInit.SOUL_JAR.get() ||
                    event.getLevel().getBlockState(event.getPos()).getBlock() == BlockInit.SOUL_GALLON.get()) {
                Block block = event.getLevel().getBlockState(event.getPos()).getBlock();
                BlockState state = event.getLevel().getBlockState(event.getPos());
                ItemStack stack = event.getEntity().getItemInHand(event.getHand());
                if (state.getValue(SoulVialBlock.FILLED) && !SoulchargableItemUtils.isCharged(stack)) {
                    int amountToAdd = SoulContainerBlockItem.ContainerTypes.getSoulCapacityFromBlockState(state);
                    SoulchargableItemUtils.incrementSoulChargeProgress(stack, amountToAdd);
                    event.getLevel().playSound(
                            null,
                            event.getEntity().position().x, event.getEntity().position().y, event.getEntity().position().z,
                            SoundEvents.SOUL_ESCAPE,
                            SoundSource.NEUTRAL,
                            1, 1f
                    );
                    event.getLevel().playSound(
                            null,
                            event.getEntity().position().x, event.getEntity().position().y, event.getEntity().position().z,
                            SoundEvents.ZOMBIE_VILLAGER_CURE,
                            SoundSource.NEUTRAL,
                            0.5f, 0.5f
                    );
                    if (SoulchargableItemUtils.tryCharge(stack)) {
                        event.getLevel().playSound(
                                null,
                                event.getEntity().position().x, event.getEntity().position().y, event.getEntity().position().z,
                                SoundEvents.ZOMBIE_VILLAGER_CONVERTED,
                                SoundSource.NEUTRAL,
                                1, 0.75f
                        );
                        event.getEntity().getCooldowns().addCooldown(event.getEntity().getItemInHand(event.getHand()).getItem(), 3000);
                    }
                    event.getLevel().setBlockAndUpdate(event.getPos(), state.setValue(SoulBottleBlock.FILLED, false));
                    event.setCancellationResult(InteractionResult.SUCCESS);
                }
            } else {
                event.setCancellationResult(InteractionResult.PASS);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerJoinWorld(final EntityJoinLevelEvent event) {
        if (!event.getLevel().isClientSide()) {
            if (event.getEntity() instanceof ServerPlayer player) {
                player.getCapability(PlayerSoulsProvider.PLAYER_SOULS).ifPresent(playerSouls -> {
                    //fix souls overfilling
                    if (playerSouls.getSouls() > playerSouls.getSoulCapacity(player)) {
                        playerSouls.setSouls(playerSouls.getSoulCapacity(player));
                    }
                    Packets.sendToPlayer(new ClientboundPlayerSoulsSyncPacket(playerSouls.getSouls(), playerSouls.getSavedSoulCapacity(), playerSouls.isUseCustomSoulCapacity()), player);
                });
            }
        }
    }

    @SubscribeEvent
    public static void onLivingDamage(final LivingDamageEvent event) {
        if (event.getSource().getEntity() instanceof Player player) {
            CoolIdeasMod.LOGGER.info(event.getEntity().getDisplayName().getString() + " was damaged by " + player.getDisplayName().getString() + " for " + event.getAmount() + " damage");
        }

        if (event.getEntity() instanceof Player player) {
            CoolIdeasMod.LOGGER.info(player.getDisplayName().getString() + " was damaged by " + event.getEntity().getDisplayName().getString() + " for " + event.getAmount() + " damage");
        }
    }

    @SubscribeEvent
    public static void onRightClickItem(final PlayerInteractEvent.RightClickItem event) {
        //log all attribute modifiers
        event.getItemStack().getAttributeModifiers(event.getHand() == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND).forEach((attribute, attributeModifier) -> {
            CoolIdeasMod.LOGGER.info("Attribute: " + attribute.getDescriptionId() + " Modifier: " + attributeModifier.getName() + " Amount: " + attributeModifier.getAmount() + " Operation: " + attributeModifier.getOperation());
        });
    }
}
