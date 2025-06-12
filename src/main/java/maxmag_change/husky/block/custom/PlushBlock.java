package maxmag_change.husky.block.custom;

import maxmag_change.husky.block.HuskyBlocks;
import maxmag_change.husky.block.entity.HuskyBlockEntities;
import maxmag_change.husky.block.entity.custom.PlushBlockEntity;
import maxmag_change.husky.registries.HuskySounds;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlushBlock extends BlockWithEntity implements BlockEntityProvider {
    public static final BooleanProperty WATERLOGGED;
    public static final DirectionProperty FACING;
    private static final VoxelShape SHAPE;

    static {
        WATERLOGGED = Properties.WATERLOGGED;
        FACING = Properties.HORIZONTAL_FACING;
        SHAPE = createCuboidShape(3.0, 0.0, 3.0, 13.0, 15.0, 13.0);
    }

    public PlushBlock(Settings settings) {
        super(settings);
    }

    public static SoundEvent getSound(BlockState state) {
        SoundEvent ret = SoundEvents.ENTITY_ALLAY_ITEM_THROWN;
        if (state.getBlock() == HuskyBlocks.MAX_PLUSHIE) {
            ret = HuskySounds.MAX_PLUSHIE_SQUISH;
        }

        return ret;
    }

    @Override
    public boolean supportsExternalFaceHiding(BlockState state) {
        return true;
    }


    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public @Nullable BlockState getPlacementState(ItemPlacementContext ctx) {
        FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
        return (BlockState) ((BlockState)this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing())).with(WATERLOGGED, fluidState.isEqualAndStill(Fluids.WATER));
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient()) {
            world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), getSound(state), SoundCategory.BLOCKS, 1.0F, 1.0F);
            BlockEntity entity = world.getBlockEntity(pos);
            if (entity instanceof PlushBlockEntity plushie) {
                plushie.squish(1);
            }
        }

        return ActionResult.SUCCESS;
    }

    @Override
    public BlockState getStateForNeighborUpdate(
            BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos
    ) {
        if ((Boolean)state.get(WATERLOGGED)) {
            world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }

        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new PlushBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, HuskyBlockEntities.PLUSH_BLOCK_ENTITY,
                (world1, pos, state1, blockEntity) -> blockEntity.tick(world1, pos, state1,blockEntity));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, WATERLOGGED);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return (Boolean)state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }
}
