package maxmag_change.husky.utill.logic;

import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Box;

import java.util.List;

public class DeserializedRoom {

    Identifier structureName;
    Box roomSize = new Box(0,0,0,0,0,0);
    List<DeserializedDoor> doors = List.of();

    public Room toRoom(){
        List<Door> DDoor = new java.util.ArrayList<>(List.of());
        List<DeserializedDoor> doors = this.doors;

        for(int i = 0; i < doors.size(); ++i) {
            DDoor.add(i,doors.get(i).toDoor());
        }

        return new Room(this.structureName,this.roomSize,DDoor);
    }
}
