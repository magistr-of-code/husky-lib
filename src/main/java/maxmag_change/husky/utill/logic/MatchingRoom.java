package maxmag_change.husky.utill.logic;

import net.minecraft.util.BlockRotation;

public class MatchingRoom {
    Room room;
    BlockRotation rotation;
    int matchingDoorIndex;

    MatchingRoom(Room room,BlockRotation rotation,int matchingDoorIndex){
        this.room=room;
        this.rotation=rotation;
        this.matchingDoorIndex=matchingDoorIndex;
    }

    public Room getRoom() {
        return room;
    }

    public BlockRotation getRotation() {
        return rotation;
    }

    public Door getMatchingDoor(){
        return this.getRoom().getDoors().get(this.getMatchingDoorIndex());
    }

    public int getMatchingDoorIndex() {
        return matchingDoorIndex;
    }
}
