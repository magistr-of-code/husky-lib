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

    public static Dungeon readFromNbt(NbtCompound compound, String key) {
        Dungeon dungeon = new Dungeon(DungeonSettings.readFromNbt(compound,"settings"));
        dungeon.bbh = BBH.readFromNbt(compound,"bbh");
        List<LastRoom> lastRoomList = DefaultedList.of();

        for (int i = 0;; i++) {
            NbtCompound lastRoomCompound = compound.getCompound("lastRoom"+i);

            if (Objects.equals(lastRoomCompound, new NbtCompound())){
                break;
            }

            lastRoomList.add(LastRoom.readFromNbt(lastRoomCompound));
        }

        return dungeon;
    }

    public void writeToNbt(NbtCompound compound, String key){
        NbtCompound dungeon = new NbtCompound();
        bbh.writeToNbt(dungeon,"bbh");
        settings.writeToNbt(compound,"settings");
        for (int i = 0; i < lastRooms.size(); i++) {
            LastRoom lastRoom = lastRooms.get(i);
            lastRoom.writeToNbt(compound,"lastRoom" + i);
        }
        compound.putInt("rooms",rooms);
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
        RoomRegistry.getType(lastRoom.identifier).generateBranches(world,this,this.bbh,new BlockPos((int) lastRoom.box.minX, (int) lastRoom.box.minY, (int) lastRoom.box.minZ),lastRoom.door,2);

        if (this.lastRooms.isEmpty() || this.rooms>this.settings.maxRooms){
            HuskyWorldComponents.DUNGEONS.get(world).idToDungeon.remove(this.id,this);
        }
    }
}
