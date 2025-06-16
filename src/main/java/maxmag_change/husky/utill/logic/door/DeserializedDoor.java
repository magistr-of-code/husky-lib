package maxmag_change.husky.utill.logic.door;

import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.List;

public class DeserializedDoor {
    List<BlockPos> blocks = List.of();
    BlockPos centerBlock = BlockPos.ORIGIN;
    Direction direction = Direction.EAST;

    public DeserializedDoor(DefaultedList<BlockPos> blocks, BlockPos centerBlock, Direction direction){
        this.blocks = blocks;
        this.centerBlock = centerBlock;
        this.direction = direction;
    }

    public Door toDoor(){
        return new Door(blocks,centerBlock,direction);
    }
}
