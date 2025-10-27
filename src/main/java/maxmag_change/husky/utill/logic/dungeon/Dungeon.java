package maxmag_change.husky.utill.logic.dungeon;

import maxmag_change.husky.cca.HuskyWorldComponents;
import maxmag_change.husky.registries.RoomRegistry;
import maxmag_change.husky.utill.logic.room.CustomIdentifier;
import maxmag_change.husky.utill.logic.room.LastRoom;
import maxmag_change.husky.utill.logic.room.Room;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;

import java.util.List;
import java.util.Objects;

public class Dungeon {

    public BBH bbh = new BBH(DefaultedList.of());
    public DungeonSettings settings;
    public List<LastRoom> lastRooms = DefaultedList.of();
    public int rooms;
    public int id;

    public Dungeon(DungeonSettings settings){
        this.settings=settings;
    }

    public Dungeon(CustomIdentifier startingRoom, String group, int maxRooms){
        this.settings=new DungeonSettings(startingRoom,group,maxRooms);
    }

    public static Dungeon readFromNbt(NbtCompound compound) {
        Dungeon dungeon = new Dungeon(DungeonSettings.readFromNbt(compound.getCompound("settings")));
        dungeon.bbh = BBH.readFromNbt(compound.getCompound("bbh"));
        List<LastRoom> lastRoomList = DefaultedList.of();

        for (int i = 0;; i++) {
            NbtCompound lastRoomCompound = compound.getCompound("lastRoom" + i);

            if (lastRoomCompound.equals(new NbtCompound())){
                break;
            }

            lastRoomList.add(LastRoom.readFromNbt(lastRoomCompound));
        }

        dungeon.lastRooms=lastRoomList;

        dungeon.rooms=compound.getInt("rooms");

        return dungeon;
    }

    public NbtCompound writeToNbt(NbtCompound compound){
        compound.put("bbh",bbh.writeToNbt(new NbtCompound()));
        compound.put("settings",settings.writeToNbt(new NbtCompound()));
        for (int i = 0; i < lastRooms.size(); i++) {
            compound.put("lastRoom" + i,lastRooms.get(i).writeToNbt(new NbtCompound()));
        }
        compound.putInt("rooms",rooms);

        return compound;
    }

    public void createDungeon(ServerWorld world,BlockPos pos){
        RoomRegistry.getType(this.settings.startingRoom.toIdentifier()).generate(world,this,this.bbh,pos,BlockRotation.NONE,5);

        //bbh.recalculate();

        HuskyWorldComponents.DUNGEONS.get(world).addDungeon(this);
    }

    public static Room getRandomRoom(Random random, List<Room> rooms) {
        Room matchingRoom = rooms.get(0);

        //
        double totalWeight = 0;
        for (Room room : rooms) {
            totalWeight += room.getSettings().getWeight();
        }

        double randomNumber = random.nextDouble() * totalWeight;

        double cumulativeWeight = 0;
        for (Room room : rooms) {
            cumulativeWeight += room.getSettings().getWeight();
            if (randomNumber <= cumulativeWeight) {
                return room;
            }
        }
        return matchingRoom;
    }

    public void generateBranch(ServerWorld world, LastRoom lastRoom) {
//        if (this.bbh.boxes.size()>10){
//            bbh.recalculate();
//        }

        RoomRegistry.getType(lastRoom.identifier).generateBranches(world,this,this.bbh,lastRoom.pos,lastRoom.door,2);

        if (this.lastRooms.isEmpty() || this.rooms>this.settings.maxRooms){
            HuskyWorldComponents.DUNGEONS.get(world).idToDungeon.remove(this.id,this);
        }
    }
}
