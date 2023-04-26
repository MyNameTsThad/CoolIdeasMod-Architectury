package com.thaddev.coolideas.content.blocks;

import com.thaddev.coolideas.content.entities.SoulOrb;
import com.thaddev.coolideas.content.items.SoulchargableItemUtils;
import com.thaddev.coolideas.content.items.materials.SoulContainerBlockItem;
import com.thaddev.coolideas.mechanics.inits.TagsInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class SoulVialBlock extends FallingBlock implements SimpleWaterloggedBlock {
    public static final int MIN_VIALS = 1;
    public static final int MAX_VIALS = SoulContainerBlockItem.ContainerTypes.VIAL.getNumsInBlock();
    public static final IntegerProperty CONTAINER_COUNT = IntegerProperty.create("container_count", MIN_VIALS, MAX_VIALS);
    public static final BooleanProperty FILLED = BooleanProperty.create("filled");
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    private static final VoxelShape ONE_AABB = Block.box(7.0D, 0.0D, 6.0D, 9.0D, 4.25D, 8.0D);
    private static final VoxelShape TWO_AABB = Block.box(6.0D, 0.0D, 6.0D, 11.0D, 4.25D, 9.0D);
    private static final VoxelShape THREE_AABB = Block.box(5.0D, 0.0D, 5.0D, 11.0D, 4.25D, 10.0D);
    private static final VoxelShape FOUR_AABB = Block.box(5.0D, 0.0D, 5.0D, 11.0D, 4.25D, 11.0D);
    private static final VoxelShape FIVE_AABB = Block.box(4.0D, 0.0D, 4.0D, 11.0D, 4.25D, 12.0D);
    private static final VoxelShape SIX_AABB = Block.box(4.0D, 0.0D, 4.0D, 13.0D, 4.25D, 12.0D);
    private static final VoxelShape SEVEN_AABB = Block.box(4.0D, 0.0D, 4.0D, 13.0D, 4.25D, 12.0D);
    private static final VoxelShape EIGHT_AABB = Block.box(3.0D, 0.0D, 4.0D, 14.0D, 4.25D, 11.0D);
    private static final VoxelShape NINE_AABB = Block.box(4.0D, 0.0D, 4.0D, 12.0D, 4.25D, 12.0D);

    public SoulVialBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any().setValue(CONTAINER_COUNT, 1).setValue(FILLED, false).setValue(WATERLOGGED, false));
    }

    @Override
    public boolean canBeReplaced(BlockState pState, BlockPlaceContext pUseContext) {
        return (!pUseContext.isSecondaryUseActive() && pUseContext.getItemInHand().getItem() == this.asItem() && pState.getValue(CONTAINER_COUNT) < MAX_VIALS && pState.getValue(FILLED) == SoulContainerBlockItem.isFilled(pUseContext.getItemInHand()))
            || super.canBeReplaced(pState, pUseContext);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        BlockState blockstate = pContext.getLevel().getBlockState(pContext.getClickedPos());
        if (blockstate.is(this)) {
            blockstate.setValue(FILLED, SoulContainerBlockItem.isFilled(pContext.getItemInHand()));
            return blockstate.cycle(CONTAINER_COUNT);
        } else {
            FluidState fluidstate = pContext.getLevel().getFluidState(pContext.getClickedPos());
            boolean flag = fluidstate.getType() == Fluids.WATER;
            return Objects.requireNonNull(super.getStateForPlacement(pContext)).setValue(WATERLOGGED, flag).setValue(FILLED, SoulContainerBlockItem.isFilled(pContext.getItemInHand()));
        }
    }

    /**
     * Update the provided state given the provided neighbor direction and neighbor state, returning a new state.
     * For example, fences make their connections to the passed in state if possible, and wet concrete powder immediately
     * returns its solidified counterpart.
     * Note that this method should ideally consider only the specific direction passed in.
     */
    @Override
    public @NotNull BlockState updateShape(BlockState pState, Direction pDirection, BlockState pNeighborState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pNeighborPos) {
        if (pState.getValue(WATERLOGGED)) {
            pLevel.scheduleTick(pCurrentPos, Fluids.WATER, Fluids.WATER.getTickDelay(pLevel));
        }

        return super.updateShape(pState, pDirection, pNeighborState, pLevel, pCurrentPos, pNeighborPos);
    }

    @Override
    public @NotNull FluidState getFluidState(BlockState pState) {
        return pState.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(pState);
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return
            switch (pState.getValue(CONTAINER_COUNT)) {
                case 2 -> TWO_AABB;
                case 3 -> THREE_AABB;
                case 4 -> FOUR_AABB;
                case 5 -> FIVE_AABB;
                case 6 -> SIX_AABB;
                case 7 -> SEVEN_AABB;
                case 8 -> EIGHT_AABB;
                case 9 -> NINE_AABB;
                default -> ONE_AABB;
            };
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(CONTAINER_COUNT, FILLED, WATERLOGGED);
    }

    @Override
    public boolean placeLiquid(LevelAccessor pLevel, BlockPos pPos, BlockState pState, FluidState pFluidState) {
        if (!pState.getValue(WATERLOGGED) && pFluidState.getType() == Fluids.WATER) {
            BlockState blockstate = pState.setValue(WATERLOGGED, true);
            pLevel.setBlock(pPos, blockstate, 3);
            pLevel.scheduleTick(pPos, pFluidState.getType(), pFluidState.getType().getTickDelay(pLevel));
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        return Block.canSupportCenter(pLevel, pPos.below(), Direction.UP);
    }

    @Override
    public void playerDestroy(Level pLevel, Player pPlayer, BlockPos pPos, BlockState pState, @Nullable BlockEntity pBlockEntity, ItemStack pTool) {
        super.playerDestroy(pLevel, pPlayer, pPos, pState, pBlockEntity, pTool);
        if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, pTool) <= 0 && pState.getValue(FILLED)){
            SoulOrb.award((ServerLevel) pLevel, Vec3.atCenterOf(pPos), SoulContainerBlockItem.ContainerTypes.VIAL.getSoulCapacity());
        }
        this.decreaseContainers(pLevel, pPos, pState);
    }

    private void decreaseContainers(Level pLevel, BlockPos pPos, BlockState pState) {
        pLevel.playSound(null, pPos, this.soundType.getBreakSound(), SoundSource.BLOCKS, 0.7F, 1);
        int i = pState.getValue(CONTAINER_COUNT);
        if (i <= 1) {
            pLevel.destroyBlock(pPos, false);
        } else {
            pLevel.setBlock(pPos, pState.setValue(CONTAINER_COUNT, i - 1), 2);
            pLevel.gameEvent(GameEvent.BLOCK_DESTROY, pPos, GameEvent.Context.of(pState));
            pLevel.levelEvent(2001, pPos, Block.getId(pState));
        }

    }

    @Override
    public void onBrokenAfterFall(Level pLevel, BlockPos pPos, FallingBlockEntity pFallingBlock) {
        pLevel.playSound(null, pPos, this.soundType.getBreakSound(), SoundSource.BLOCKS, 1F, 1);
        boolean overChargeSucess = false;
        if (pLevel.getBlockState(pPos.below()).getBlock() instanceof SmithingTableBlock) {
            //get an item frame inside pPos and facing up
            ItemFrame frame = pLevel.getEntitiesOfClass(ItemFrame.class, new AABB(pPos), EntitySelector.ENTITY_STILL_ALIVE)
                    .stream()
                    .filter(itemFrame -> itemFrame.getDirection() == Direction.UP)
                    .findFirst()
                    .orElse(null);
            if (frame != null) {
                ItemStack stack = frame.getItem();
                if (stack.is(TagsInit.SOULCHARGABLE) && SoulchargableItemUtils.isCharged(stack) && !SoulchargableItemUtils.isFullyOverCharged(stack)) {
                    SoulchargableItemUtils.incrementOverChargeProgress(stack, SoulContainerBlockItem.ContainerTypes.VIAL.getSoulCapacity());
                    pLevel.playSound(null, pPos, SoundEvents.RESPAWN_ANCHOR_CHARGE, SoundSource.BLOCKS, 0.25F, 1);
                    pLevel.playSound(null, pPos, SoundEvents.SOUL_ESCAPE, SoundSource.BLOCKS, 0.5F, 2);
                    overChargeSucess = true;
                }
            }
        }
        if (pFallingBlock.getBlockState().getValue(FILLED) && !overChargeSucess) {
            SoulOrb.award((ServerLevel) pLevel, Vec3.atCenterOf(pPos),
                    SoulContainerBlockItem.ContainerTypes.VIAL.getSoulCapacity() * pFallingBlock.getBlockState().getValue(CONTAINER_COUNT));
        }
        pFallingBlock.discard();
    }

    @Override
    public void onLand(Level pLevel, BlockPos pPos, BlockState pState, BlockState pReplaceableState, FallingBlockEntity pFallingBlock) {
        onBrokenAfterFall(pLevel, pPos, pFallingBlock);
        pLevel.setBlockAndUpdate(pPos, Blocks.AIR.defaultBlockState());
    }
}
