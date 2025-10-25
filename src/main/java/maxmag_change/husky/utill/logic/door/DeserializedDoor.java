package maxmag_change.husky.utill.logic.door;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;

import java.util.List;

public class DeserializedDoor {
    List<DoorPos> blocks = List.of();
    DoorPos centerBlock = new DoorPos(0,0,0);
    DoorDirection direction = DoorDirection.EAST;

    public DeserializedDoor(DefaultedList<DoorPos> blocks, DoorPos centerBlock, DoorDirection direction){
        this.blocks = blocks;
        this.centerBlock = centerBlock;
        this.direction = direction;
    }

    public DeserializedDoor(DefaultedList<BlockPos> blocks, BlockPos centerBlock, Direction direction){

        List<DoorPos> blocks2 = new java.util.ArrayList<>(List.of());
        for (int i = 0; i < blocks.size(); i++) {
            blocks2.add(i,new DoorPos(blocks.get(i).getX(),blocks.get(i).getY(),blocks.get(i).getZ()));
        }

        this.blocks = blocks2;
        this.centerBlock = new DoorPos(centerBlock.getX(),centerBlock.getY(),centerBlock.getZ());
        this.direction = doorDirectionFromDirection(direction);
    }

    public Door toDoor(){
        List<BlockPos> blocks2 = new java.util.ArrayList<>(List.of());
        for (int i = 0; i < blocks.size(); i++) {
            blocks2.add(i,blockPosFromDoorPos(blocks.get(i)));
        }

        return new Door(blocks2,blockPosFromDoorPos(centerBlock),directionFromDoorDirection(direction));
    }

    public static Direction directionFromDoorDirection(DoorDirection doorDirection){
        return switch (doorDirection){
            case DOWN -> Direction.DOWN;
            case UP -> Direction.UP;
            case NORTH -> Direction.NORTH;
            case SOUTH -> Direction.SOUTH;
            case WEST -> Direction.WEST;
            case EAST -> Direction.EAST;
        };
    }

    public static DoorDirection doorDirectionFromDirection(Direction direction){
        return switch (direction){
            case DOWN -> DoorDirection.DOWN;
            case UP -> DoorDirection.UP;
            case NORTH -> DoorDirection.NORTH;
            case SOUTH -> DoorDirection.SOUTH;
            case WEST -> DoorDirection.WEST;
            case EAST -> DoorDirection.EAST;
        };
    }

    public static BlockPos blockPosFromDoorPos(DoorPos doorPos){
        return new BlockPos(doorPos.getX(),doorPos.getY(),doorPos.getZ());
    }

    private enum DoorDirection {
        DOWN,
        UP,
        NORTH,
        SOUTH,
        WEST,
        EAST
    }

    private class DoorPos {
        private int x;
        private int y;
        private int z;

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getZ() {
            return z;
        }

        public void setX(int x) {
            this.x = x;
        }

        public void setY(int y) {
            this.y = y;
        }

        public void setZ(int z) {
            this.z = z;
        }

        public DoorPos(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }
}
