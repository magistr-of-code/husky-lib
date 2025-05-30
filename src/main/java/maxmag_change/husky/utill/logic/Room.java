package maxmag_change.husky.utill.logic;

import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class Room {
    Identifier structureName;
    Box roomSize = new Box(0,0,0,0,0,0);
    DefaultedList<Door> doors = DefaultedList.ofSize(27, Door.EMPTY);;

    Room(Identifier structureName){
        this.structureName = structureName;
    }

    public Box getRoomSize() {
        return roomSize;
    }

    public DefaultedList<Door> getDoors() {
        return doors;
    }

    public DefaultedList<BlockPos> getDoorPosition(int index) {
        DefaultedList<Door> doors = this.getDoors();
        return doors.get(index).getBlocks();
    }

    public Identifier getStructureName() {
        return structureName;
    }
}
