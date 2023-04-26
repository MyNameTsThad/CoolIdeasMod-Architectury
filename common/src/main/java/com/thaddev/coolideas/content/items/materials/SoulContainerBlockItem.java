package com.thaddev.coolideas.content.items.materials;

import com.thaddev.coolideas.content.blocks.SoulBottleBlock;
import com.thaddev.coolideas.content.blocks.SoulGallonBlock;
import com.thaddev.coolideas.content.blocks.SoulJarBlock;
import com.thaddev.coolideas.content.blocks.SoulVialBlock;
import com.thaddev.coolideas.mechanics.data.PlayerSouls;
import com.thaddev.coolideas.mechanics.data.PlayerSoulsRetriever;
import com.thaddev.coolideas.mechanics.inits.ItemInit;
import com.thaddev.coolideas.mechanics.networking.Packets;
import com.thaddev.coolideas.mechanics.networking.packets.ClientboundPlayerSoulsSyncPacket;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

public class SoulContainerBlockItem extends ItemNameBlockItem {
    private final ContainerTypes type;
    //SIZES:
    //vial: 10 souls x 9
    //bottle: 50 souls x 6
    //jar: 200 souls x 3
    //gallon: 1000 souls x 1

    public SoulContainerBlockItem(Block pBlock, Properties pProperties, ContainerTypes type) {
        super(pBlock, pProperties);
        this.type = type;
    }

    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        AtomicReference<InteractionResultHolder<ItemStack>> resultHolder = new AtomicReference<>(InteractionResultHolder.pass(pPlayer.getItemInHand(pHand)));
        PlayerSouls playerSouls = PlayerSoulsRetriever.get(pPlayer);
        // if the container is empty and the player has enough souls to fit, start using the item
        // if the container is full and the player hasnt filled up their soul capacity, start using the item
        if ((!isFilled(pPlayer.getItemInHand(pHand)) && playerSouls.getSouls() >= type.getSoulCapacity()) ||
                (isFilled(pPlayer.getItemInHand(pHand)) && playerSouls.getSouls() < playerSouls.getSoulCapacity(pPlayer))) {
            resultHolder.set(ItemUtils.startUsingInstantly(pLevel, pPlayer, pHand));
        }
        return resultHolder.get();
    }

    /**
     * How long it takes to use or consume an item
     */
    public int getUseDuration(ItemStack pStack) {
        return type.getSoulCapacity() / 5;
    }

    public static boolean isFilled(ItemStack stack) {
        CompoundTag compoundtag = stack.getTag();
        return compoundtag != null && compoundtag.getBoolean("Filled");
    }

    public static void setFilled(ItemStack stack, boolean filled) {
        if (stack.getItem() instanceof SoulContainerBlockItem) {
            CompoundTag compoundtag = stack.getOrCreateTag();
            compoundtag.putBoolean("Filled", filled);
        }
    }

    public static Item getItemFromType(ContainerTypes type) {
        return
                switch (type) {
                    case BOTTLE -> ItemInit.SOUL_BOTTLE.get();
                    case JAR -> ItemInit.SOUL_JAR.get();
                    case GALLON -> ItemInit.SOUL_GALLON.get();
                    default -> ItemInit.SOUL_VIAL.get();
                };
    }

    public ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pEntityLiving) {
        Player player = pEntityLiving instanceof Player ? (Player) pEntityLiving : null;
        if (player instanceof ServerPlayer) {
            CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayer) player, pStack);
        }

        boolean isFilled = isFilled(pStack);

        if (player != null) {
            if (!pLevel.isClientSide) {
                PlayerSouls playerSouls = PlayerSoulsRetriever.get(player);
                // if the container is empty and the player has enough souls to fit, subtract the souls from the player and fill the container
                // if the container is full and the player hasnt filled up their soul capacity, add the souls to the player until they are full and if so, discard the rest of the souls
                if (!isFilled && playerSouls.getSouls() >= type.getSoulCapacity()) {
                    playerSouls.addSouls(-type.getSoulCapacity());
                } else if (isFilled && playerSouls.getSouls() < playerSouls.getSoulCapacity(player)) {
                    int soulCapacityLeft = playerSouls.getSoulCapacity(player) - playerSouls.getSouls();
                    playerSouls.addSouls(Math.min(soulCapacityLeft, type.getSoulCapacity()));
                }

                pLevel.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.SOUL_ESCAPE, SoundSource.PLAYERS, 3f, 1F);
                PlayerSoulsRetriever.set(playerSouls, player);
                Packets.sendToPlayer(new ClientboundPlayerSoulsSyncPacket(playerSouls.getSouls(), playerSouls.getSavedSoulCapacity(), playerSouls.isUseCustomSoulCapacity()), (ServerPlayer) player);
            }

            player.awardStat(Stats.ITEM_USED.get(this));
            if (!player.getAbilities().instabuild) {
                pStack.shrink(1);
            }

            if (pStack.isEmpty()) {
                if (isFilled) {
                    return new ItemStack(getItemFromType(this.type));
                } else {
                    ItemStack toReturn = new ItemStack(getItemFromType(this.type));
                    setFilled(toReturn, true);
                    return toReturn;
                }
            }

            if (isFilled) {
                if (!player.getInventory().add(new ItemStack(getItemFromType(this.type)))) {
                    ItemEntity item = new ItemEntity(pLevel, player.getX(), player.getEyeY() - 3f, player.getZ(), new ItemStack(getItemFromType(this.type)));
                    item.setThrower(player.getUUID());
                    item.setPickUpDelay(40);
                    float f8 = Mth.sin(player.getXRot() * ((float) Math.PI / 180F));
                    float f2 = Mth.cos(player.getXRot() * ((float) Math.PI / 180F));
                    float f3 = Mth.sin(player.getYRot() * ((float) Math.PI / 180F));
                    float f4 = Mth.cos(player.getYRot() * ((float) Math.PI / 180F));
                    float f5 = player.getRandom().nextFloat() * ((float) Math.PI * 2F);
                    float f6 = 0.02F * player.getRandom().nextFloat();
                    item.setDeltaMovement((double) (-f3 * f2 * 0.3F) + Math.cos(f5) * (double) f6, -f8 * 0.3F + 0.1F + (player.getRandom().nextFloat() - player.getRandom().nextFloat()) * 0.1F, (double) (f4 * f2 * 0.3F) + Math.sin(f5) * (double) f6);
                    pLevel.addFreshEntity(item);
                }
            } else {
                ItemStack toReturn = new ItemStack(getItemFromType(this.type));
                setFilled(toReturn, true);
                if (!player.getInventory().add(toReturn)) {
                    ItemEntity item = new ItemEntity(pLevel, player.getX(), player.getEyeY() - 3f, player.getZ(), toReturn);
                    item.setThrower(player.getUUID());
                    item.setPickUpDelay(40);
                    float f8 = Mth.sin(player.getXRot() * ((float) Math.PI / 180F));
                    float f2 = Mth.cos(player.getXRot() * ((float) Math.PI / 180F));
                    float f3 = Mth.sin(player.getYRot() * ((float) Math.PI / 180F));
                    float f4 = Mth.cos(player.getYRot() * ((float) Math.PI / 180F));
                    float f5 = player.getRandom().nextFloat() * ((float) Math.PI * 2F);
                    float f6 = 0.02F * player.getRandom().nextFloat();
                    item.setDeltaMovement((double) (-f3 * f2 * 0.3F) + Math.cos(f5) * (double) f6, -f8 * 0.3F + 0.1F + (player.getRandom().nextFloat() - player.getRandom().nextFloat()) * 0.1F, (double) (f4 * f2 * 0.3F) + Math.sin(f5) * (double) f6);
                    pLevel.addFreshEntity(item);
                }
            }
        }

        pEntityLiving.gameEvent(GameEvent.DRINK);
        return pStack;
    }

    /**
     * returns the action that specifies what animation to play when the items is being used
     */
    public @NotNull UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.DRINK;
    }

    public enum ContainerTypes {
        VIAL(10, 9),
        BOTTLE(50, 6),
        JAR(200, 3),
        GALLON(1000, 1);

        private final int soulCapacity;
        private final int numsInBlock;

        ContainerTypes(int soulCapacity, int numsInBlock) {
            this.soulCapacity = soulCapacity;
            this.numsInBlock = numsInBlock;
        }

        public int getSoulCapacity() {
            return soulCapacity;
        }

        public int getNumsInBlock() {
            return numsInBlock;
        }

        public int getIndex() {
            return Arrays.stream(ContainerTypes.values()).filter(type -> type.equals(this)).findFirst().get().ordinal();
        }

        public static ContainerTypes fromIndex(int idx) {
            return Arrays.stream(ContainerTypes.values()).filter(type -> type.ordinal() == idx).findFirst().get();
        }

        public static int getSoulCapacityFromBlockState(BlockState blockState) {
            if (blockState.getBlock() instanceof SoulVialBlock ||
                    blockState.getBlock() instanceof SoulBottleBlock ||
                    blockState.getBlock() instanceof SoulJarBlock ||
                    blockState.getBlock() instanceof SoulGallonBlock) {
                if (blockState.getBlock() instanceof SoulVialBlock) {
                    int containers = blockState.getValue(SoulVialBlock.CONTAINER_COUNT);
                    return VIAL.getSoulCapacity() * containers;
                } else if (blockState.getBlock() instanceof SoulBottleBlock) {
                    int containers = blockState.getValue(SoulBottleBlock.CONTAINER_COUNT);
                    return BOTTLE.getSoulCapacity() * containers;
                } else if (blockState.getBlock() instanceof SoulJarBlock) {
                    int containers = blockState.getValue(SoulJarBlock.CONTAINER_COUNT);
                    return JAR.getSoulCapacity() * containers;
                } else if (blockState.getBlock() instanceof SoulGallonBlock) {
                    return GALLON.getSoulCapacity();
                }
            }
            return 0;
        }
    }

    @Override
    public @NotNull SoundEvent getDrinkingSound() {
        return SoundEvents.HONEY_DRINK;
    }

    @Override
    public @NotNull SoundEvent getEatingSound() {
        return SoundEvents.HONEY_DRINK;
    }
}
