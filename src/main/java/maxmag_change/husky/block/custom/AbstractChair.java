package maxmag_change.husky.block.custom;

import maxmag_change.husky.entity.ChairEntity;
import maxmag_change.husky.entity.HuskyEntities;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class AbstractChair extends HorizontalFacingBlock {
    public AbstractChair(Settings settings) {
        super(settings);
        setDefaultState(this.getStateManager().getDefaultState()
                .with(Properties.HORIZONTAL_FACING, Direction.NORTH)
        );

        this.height = 0.36f;
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        Direction facing = ctx.getHorizontalPlayerFacing();
        return this.getDefaultState().with(Properties.HORIZONTAL_FACING, facing);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
        stateManager.add(Properties.HORIZONTAL_FACING);
    }

    public float height;

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient) {
            return ActionResult.CONSUME;
        }

        if (player.isSpectator() || player.isSneaking()) {
            return ActionResult.FAIL;
        }

        List<ChairEntity> active = world.getEntitiesByClass(ChairEntity.class, new Box(pos), Entity::hasPassengers);
        if (active == null)
            return ActionResult.FAIL;

        List<Entity> hasPassenger = new ArrayList<>();
        active.forEach(chairEntity -> hasPassenger.add(chairEntity.getFirstPassenger()));
        if (!active.isEmpty() && hasPassenger.stream().anyMatch(Entity::isPlayer)) {
            return ActionResult.FAIL;
        }
        else if (!active.isEmpty()) {
            hasPassenger.forEach(Entity::stopRiding);
            return ActionResult.SUCCESS;
        }
        else if (sitEntity(world, pos, state, player) == ActionResult.SUCCESS) {
            return ActionResult.SUCCESS;
        }
        return ActionResult.CONSUME;
    }


    public ActionResult sitEntity(World world, BlockPos pos, BlockState state, Entity entityToSit) {
        double px = pos.getX() + 0.5;
        double pz = pos.getZ() + 0.5;
        Direction direction = state.get(FACING);
//        switch (direction) {
//            case EAST -> {
//                px = pos.getX() + 0.1;
//                pz = pos.getZ() + 0.5;
//            }
//            case WEST -> {
//                px = pos.getX() + 0.9;
//                pz = pos.getZ() + 0.5;
//            }
//            case SOUTH -> {
//                px = pos.getX() + 0.5;
//                pz = pos.getZ() + 0.1;
//            }
//            default -> {
//                px = pos.getX() + 0.5;
//                pz = pos.getZ() + 0.9;
//            }
//        }
        double py = pos.getY() + this.height;
        float yaw = state.get(FACING).getOpposite().asRotation();
        ChairEntity chairEntity = HuskyEntities.CHAIR.create(world);
        chairEntity.refreshPositionAndAngles(px, py, pz, yaw, 0);
        chairEntity.setNoGravity(true);
        chairEntity.setSilent(true);
        chairEntity.setInvisible(false);
        chairEntity.setInvulnerable(true);
        chairEntity.setAiDisabled(true);
        chairEntity.setNoDrag(true);
        chairEntity.setHeadYaw(yaw);
        chairEntity.setYaw(yaw);
        chairEntity.setBodyYaw(yaw);
        if (world.spawnEntity(chairEntity)) {
            entityToSit.startRiding(chairEntity, true);
            entityToSit.setYaw(yaw);
            entityToSit.setHeadYaw(yaw);
            chairEntity.setYaw(yaw);
            chairEntity.setBodyYaw(yaw);
            chairEntity.setHeadYaw(yaw);

            return ActionResult.SUCCESS;
        }
        return ActionResult.CONSUME;
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        super.onEntityCollision(state, world, pos, entity);
        List<ChairEntity> active = world.getEntitiesByClass(ChairEntity.class, new Box(pos), Entity::hasPassengers);
        if (active == null || !active.isEmpty())
            return;

        if (entity instanceof PlayerEntity || entity instanceof IronGolemEntity || entity instanceof AbstractMinecartEntity || entity.hasVehicle() || !(entity instanceof LivingEntity) || entity instanceof ChairEntity) {
            return;
        }
        sitEntity(world, pos, state, entity);
    }

    @Override
    public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
        return false;
    }
}
