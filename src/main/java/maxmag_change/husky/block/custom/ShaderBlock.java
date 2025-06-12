package maxmag_change.husky.block.custom;

import maxmag_change.husky.block.entity.HuskyBlockEntities;
import maxmag_change.husky.block.entity.custom.RoomAnchorBlockEntity;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class ShaderBlock extends BlockWithEntity implements BlockEntityProvider {
    public ShaderBlock(Settings settings) {
        super(settings);
    }



    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new RoomAnchorBlockEntity(pos, state);
    }

    //TODO SHADER BASED BLOCK PRETTY PLEASE

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, HuskyBlockEntities.ROOM_ANCHOR_BLOCK_ENTITY,
                (world1, pos, state1, blockEntity) -> blockEntity.tick(world1, pos, state1));
    }
}
