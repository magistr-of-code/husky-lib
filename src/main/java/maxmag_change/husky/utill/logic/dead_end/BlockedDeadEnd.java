package maxmag_change.husky.utill.logic.dead_end;

import maxmag_change.husky.utill.logic.door.Door;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class BlockedDeadEnd extends DeadEnd{
    BlockState block;

    public BlockedDeadEnd(BlockState block){
        this.block = block;
    }

    public BlockState getBlock() {
        return block;
    }

    @Override
    public void generate(World world, Door door, BlockPos pos) {

        door.getBlocks().forEach(blockPos ->{
            BlockPos newPos = blockPos.add(door.getDirection().getVector()).add(door.getCenterBlock()).add(pos);
            if (!world.getBlockState(newPos).isOpaque() || !world.getBlockState(newPos).isSolid()){
                world.setBlockState(newPos,block);
            }
        });
    }
}
