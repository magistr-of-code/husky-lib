package maxmag_change.husky.utill.logic.dead_end.vanilla;

import maxmag_change.husky.utill.logic.dead_end.DeadEnd;
import maxmag_change.husky.utill.logic.door.Door;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class DungeonDeadEnd extends DeadEnd {

    List<BlockState> blocks = List.of(
            Blocks.ANDESITE.getDefaultState(),
            Blocks.COBBLESTONE.getDefaultState(),
            Blocks.STONE_BRICKS.getDefaultState(),
            Blocks.MOSSY_COBBLESTONE.getDefaultState(),
            Blocks.MOSSY_STONE_BRICKS.getDefaultState()
    );

    @Override
    public void generate(World world, Door door, BlockPos pos) {
        door.getBlocks().forEach(blockPos ->{

            BlockState block = blocks.get(0);

            double totalWeight = 0;
            for (BlockState state : blocks) {
                totalWeight += 1;
            }

            double randomNumber = world.getRandom().nextDouble() * totalWeight;

            double cumulativeWeight = 0;
            for (BlockState state : blocks) {
                cumulativeWeight += 1;
                if (randomNumber <= cumulativeWeight) {
                    block = state;
                    break;
                }
            }

            BlockPos newPos = blockPos.add(door.getDirection().getVector()).add(door.getCenterBlock()).add(pos);
            if (!world.getBlockState(newPos).isOpaque() || !world.getBlockState(newPos).isSolid()){
                world.setBlockState(newPos,block);
            }
        });
    }
}
