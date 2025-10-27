package maxmag_change.husky.utill.logic.dungeon;

import maxmag_change.husky.HuskyLib;
import maxmag_change.husky.utill.logic.room.CustomIdentifier;
import maxmag_change.husky.utill.logic.room.RoomSettings;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

public class DungeonSettings {
    public CustomIdentifier startingRoom;
    public String group;
    public int maxRooms;


    public DungeonSettings(CustomIdentifier startingRoom, String group, int maxRooms){
        this.startingRoom=startingRoom;
        this.group = group;
        this.maxRooms=maxRooms;
    }

    public DungeonSettings(CustomIdentifier startingRoom, String group){
        this(startingRoom,group,-1);
    }

    public static DungeonSettings readFromNbt(NbtCompound compound) {
        Identifier identifier = Identifier.tryParse(compound.getString("startingRoom"));
        return new DungeonSettings(new CustomIdentifier(identifier.getNamespace(),identifier.getPath()),compound.getString("group"),compound.getInt("maxRooms"));
    }


    @Override
    public DungeonSettings clone() {
        try {
            return (DungeonSettings) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    @Override
    public String toString() {
        return "DungeonSettings{" +
                "startingRoom=" + startingRoom.toString() +
                ", group='" + group +
                ", maxRooms=" + maxRooms +
                '}';
    }

    public NbtCompound writeToNbt(NbtCompound compound) {
        compound.putString("startingRoom",startingRoom.toString());
        compound.putString("group",group);
        compound.putInt("maxRooms",maxRooms);
        return compound;
    }
}

