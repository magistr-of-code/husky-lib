package maxmag_change.husky.utill.logic.room;

import maxmag_change.husky.utill.logic.door.DeserializedDoor;
import maxmag_change.husky.utill.logic.door.Door;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Box;

import java.util.List;

public class DeserializedRoom {

    Identifier structureName;
    RoomSettings settings;
    Box roomSize = new Box(0,0,0,0,0,0);
    List<DeserializedDoor> doors = DefaultedList.of();

    public DeserializedRoom(Identifier structureName, Box roomSize, List<Door> doors, RoomSettings settings){
        this.structureName = structureName;
        this.roomSize = roomSize;
        for(int i = 0; i < doors.size(); ++i) {
            Door door = doors.get(i);
            if (door!=Door.EMPTY&&door!=null){
                this.doors.add(new DeserializedDoor(door.getBlocks(),door.getCenterBlock(),door.getDirection()));
            }
        }
        this.settings = settings;
    }

    public Room toRoom(){
        List<Door> DDoor = new java.util.ArrayList<>(List.of());
        List<DeserializedDoor> doors = this.doors;

        for(int i = 0; i < doors.size(); ++i) {
            DDoor.add(i,doors.get(i).toDoor());
        }

        return new Room(this.structureName,this.roomSize,DDoor,this.settings);
    }
}
