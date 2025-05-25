package maxmag_change.husky.utill.logic;

import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class Room {
    Identifier structureName;
    Box roomSize = new Box(0,0,0,0,0,0);
    List<Door> doors;

    Room(Identifier structureName){
        this.structureName = structureName;
    }

    public Box getRoomSize() {
        return roomSize;
    }



    public List<Door> getDoors() {
        return doors;
    }

    public List<Vec3d> getDoorPosition(int index) {
        List<Door> doors = this.getDoors();
        return doors.get(index).getBlocks();
    }

    public Identifier getStructureName() {
        return structureName;
    }
}
