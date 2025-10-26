package maxmag_change.husky.utill.logic.room;

import maxmag_change.husky.HuskyLib;
import maxmag_change.husky.registries.DeadEndRegistry;
import maxmag_change.husky.registries.RoomRegistry;
import maxmag_change.husky.utill.HuskyMathHelper;
import maxmag_change.husky.utill.logic.dead_end.DeadEnd;
import maxmag_change.husky.utill.logic.door.Door;
import maxmag_change.husky.utill.logic.dungeon.BBH;
import maxmag_change.husky.utill.logic.dungeon.Dungeon;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.structure.StructureTemplateManager;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class Room implements Cloneable {
    private final Identifier structureName;
    private RoomSettings settings;
    private Box roomSize;
    private DefaultedList<Door> doors;

    public Room(Identifier structureName,Box roomSize,List<Door> doors,RoomSettings settings){
        this.structureName = structureName;
        this.roomSize = roomSize;
        this.doors = DefaultedList.ofSize(27, Door.EMPTY);
        for (int i = 0; i < doors.size(); i++) {
            this.doors.set(i,doors.get(i));
        }
        this.settings = settings;
    }

    public Room(Identifier structureName,Box roomSize,List<Door> doors,String group){
        this(structureName,roomSize,doors,new RoomSettings(true,group));
    }

    public Room(Identifier structureName,String group){
        this(structureName,new Box(0,0,0,0,0,0),DefaultedList.ofSize(27, Door.EMPTY),group);
    }

    //Room Size
    public Box getRoomSize() {
        return roomSize;
    }

    public void setRoomSize(Box box) {
        this.roomSize = box;
    }

    //Doors
    public DefaultedList<Door> getDoors() {
        return doors;
    }

    public DefaultedList<BlockPos> getDoorPosition(int index) {
        DefaultedList<Door> doors = this.getDoors();
        return doors.get(index).getBlocks();
    }

    public void setDoors(DefaultedList<Door> doors) {
        this.doors = doors;
    }

    //Structure Name
    public Identifier getStructureName() {
        return structureName;
    }

    //Settings
    public RoomSettings getSettings() {
        return settings;
    }

    public void setSettings(RoomSettings settings) {
        this.settings = settings;
    }

    public static Room protectedGenerate(Room room, World world, BlockPos pos, BlockRotation rotation, int forward){
        return protectedGenerate(room,world,null,new BBH(DefaultedList.of()),pos,rotation,forward);
    }

    public static Room protectedGenerate(Room room, World world, BBH bbh, BlockPos pos, BlockRotation rotation, int forward){
        return protectedGenerate(room,world,null,bbh,pos,rotation,forward);
    }

    public static Room protectedGenerate(Room room, World world, Dungeon dungeon, BBH bbh, BlockPos pos, BlockRotation rotation, int forward){
        return room.clone().generate(world,dungeon,bbh,pos,rotation,forward);
    }

    public Room generate(World world, Dungeon dungeon, BBH bbh, BlockPos pos, BlockRotation rotation, int forward){
        //Don't generate if forward is set to 0
        if (forward<=0){
            return this;
        }
        //Don't generate if world is null
        if (world == null){
            return this;
        }

        if (dungeon!=null){
            if (dungeon.rooms>dungeon.settings.maxRooms){
                return this;
            }
        }

        //Rotate room's bounding box
        BlockPos boxCenter = BlockPos.ofFloored(this.getRoomSize().getCenter());
        this.setRoomSize(rotateRoomSize(rotation, boxCenter));

        //Place room
        if (!world.isClient()) {
            StructurePlacementData structurePlacementData = new StructurePlacementData()
                    .setMirror(BlockMirror.NONE)
                    .setRotation(rotation)
                    .setIgnoreEntities(false);

            StructureTemplateManager structureTemplateManager = world.getServer().getStructureTemplateManager();
            Optional<StructureTemplate> optional;
            optional = structureTemplateManager.getTemplate(this.getStructureName());
            optional.get().place(
                    (ServerWorld)world,
                    StructureTemplate.transformAround(pos, BlockMirror.NONE, rotation, pos.add(boxCenter)),
                    pos.add(boxCenter),
                    structurePlacementData,
                    world.getRandom(),
                    2
            );
        }

        bbh.boxes.add(this.getRoomSize().offset(pos));

        //Go through each door
        if (this.getDoors()!=null && !this.getDoors().isEmpty()) {
            for(int ii = 0; ii < this.getDoors().size(); ++ii) {
                Door door = this.getDoors().get(ii);

                //Go to the next door if this door is empty
                if (door==Door.EMPTY) {
                    continue;
                }

                //Rotate the door
                door.centerBlock = StructureTemplate.transformAround(door.getCenterBlock(), BlockMirror.NONE, rotation, boxCenter);
                door.direction = rotation.rotate(door.getDirection());
                door.rotateBlocks(rotation);

                //Generate additional rooms
                if (forward-1>0) {
                    this.generateBranches(world, dungeon,bbh, pos, door,forward);
                } else {
                    if (dungeon!=null) {
                        dungeon.lastRooms.add(new LastRoom(this.getRoomSize().offset(pos),door,this.getStructureName()));
                    }
                    DeadEnd deadEnd = DeadEndRegistry.getType(this.getSettings().getDeadEnd().toIdentifier());
                    deadEnd.generate(world,door,pos);
                }

                this.getDoors().set(ii,door);
            }
        }

        return this;
    }

    public Box rotateRoomSize(BlockRotation rotation, BlockPos boxCenter) {
        BlockPos min = StructureTemplate.transformAround(BlockPos.ofFloored(this.getRoomSize().minX,this.getRoomSize().minY,this.getRoomSize().minZ),BlockMirror.NONE, rotation, boxCenter);
        BlockPos max = StructureTemplate.transformAround(BlockPos.ofFloored(this.getRoomSize().maxX,this.getRoomSize().maxY,this.getRoomSize().maxZ),BlockMirror.NONE, rotation, boxCenter);

        return new Box(min,max);
    }

    public void generateBranches(World world, Dungeon dungeon,BBH bbh, BlockPos pos, Door door, int forward) {
        //Find matching rooms
        List<MatchingRoom> matchingRooms = RoomRegistry.getWithMatchingDoor(door.clone());
        //remove if groups don't match
        matchingRooms.removeIf(matchingRoom -> !Objects.equals(matchingRoom.getRoom().getSettings().getGroup(), this.getSettings().getGroup()));
        //remove if intersects with any other rooms
        matchingRooms.removeIf(matchingRoom -> {

            BlockRotation randomRotation = matchingRoom.getRotation();
            Room randomRoom = matchingRoom.getRoom().clone();
            Door randomDoor = randomRoom.getDoors().get(matchingRoom.getMatchingDoorIndex());
            BlockPos centerRandomDoor = StructureTemplate.transformAround(randomDoor.getCenterBlock(), BlockMirror.NONE, randomRotation, BlockPos.ofFloored(randomRoom.getRoomSize().getCenter()));

            BlockPos roomPoint = getNewRoomOrigin(pos, door, centerRandomDoor, randomRoom);

            Box box = randomRoom.rotateRoomSize(matchingRoom.getRotation(),BlockPos.ofFloored(randomRoom.getRoomSize().getCenter()));
            box = box.offset(roomPoint);

            if (bbh.boxes != null && !bbh.boxes.isEmpty()) {
                for (Box generatedRoom : bbh.boxes) {
                    if (generatedRoom.equals(this.getRoomSize().offset(pos))) {
                        if ((this.getSettings().mergeDoors()&&randomRoom.getSettings().mergeDoors())){
                            continue;
                        }
                    }
                    if (HuskyMathHelper.intersects(box, generatedRoom)) {
                        return true;
                    }
                }
            }

            return bbh.checkInteractions(box);
        });

        if (!matchingRooms.isEmpty()){

            MatchingRoom matchingRoom = getRandomRoom(world.getRandom(), matchingRooms);

            BlockRotation randomRotation = matchingRoom.getRotation();
            Room randomRoom = matchingRoom.getRoom().clone();
            Door randomDoor = randomRoom.getDoors().get(matchingRoom.getMatchingDoorIndex());
            BlockPos centerRandomDoor = StructureTemplate.transformAround(randomDoor.getCenterBlock(), BlockMirror.NONE, randomRotation, BlockPos.ofFloored(randomRoom.getRoomSize().getCenter()));

            BlockPos roomPoint = getNewRoomOrigin(pos, door, centerRandomDoor, randomRoom);

            randomRoom.getDoors().set(matchingRoom.getMatchingDoorIndex(),Door.EMPTY);

            if (dungeon!=null) {
                dungeon.rooms++;
            }

            Room.protectedGenerate(randomRoom, world, dungeon, bbh,roomPoint,randomRotation, forward-1);
        } else {
            DeadEnd deadEnd = DeadEndRegistry.getType(this.getSettings().getDeadEnd().toIdentifier());
            deadEnd.generate(world,door,pos);
        }
    }

    public static MatchingRoom getRandomRoom(Random random, List<MatchingRoom> matchingRooms) {
        MatchingRoom matchingRoom = matchingRooms.get(0);

        //
        double totalWeight = 0;
        for (MatchingRoom room : matchingRooms) {
            totalWeight += room.getRoom().getSettings().getWeight();
        }

        double randomNumber = random.nextDouble() * totalWeight;

        double cumulativeWeight = 0;
        for (MatchingRoom room : matchingRooms) {
            cumulativeWeight += room.getRoom().getSettings().getWeight();
            if (randomNumber <= cumulativeWeight) {
                return room;
            }
        }
        return matchingRoom;
    }

    private BlockPos getNewRoomOrigin(BlockPos pos, Door door, BlockPos centerRandomDoor, Room randomRoom) {

        BlockPos roomPoint = door.getCenterBlock().add(pos);

        roomPoint = roomPoint.subtract(centerRandomDoor.add(pos));

        roomPoint = roomPoint.add(pos);

        if ((!this.getSettings().mergeDoors() || !randomRoom.getSettings().mergeDoors())){
            roomPoint = roomPoint.add(door.getDirection().getVector());
        }

        return roomPoint;
    }

    public void hasMatchingDoors(List<MatchingRoom> pairs,Door door){
        for(int i = 0; i < this.getDoors().size(); ++i) {
            Room room = this.clone();
            Pair<BlockRotation, Boolean> pair = room.getDoors().get(i).hasMatchingShape(door);
            if (pair.getRight()) {
                pairs.add(new MatchingRoom(room,pair.getLeft(),i));
                return;
            }
        }

    }


    @Override
    public Room clone() {
        try {
            Room room = (Room) super.clone();

            DefaultedList<Door> doors = DefaultedList.ofSize(27, Door.EMPTY);

            for (int i = 0; i < room.getDoors().size(); i++) {
                Door door = room.getDoors().get(i);
                if (door!=Door.EMPTY){
                    doors.set(i,door.clone());
                }
            }

            Room clone = new Room(room.getStructureName(),new Box(room.getRoomSize().minX,room.getRoomSize().minY,room.getRoomSize().minZ,room.getRoomSize().maxX,room.getRoomSize().maxY,room.getRoomSize().maxZ),doors,room.getSettings());
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    @Override
    public String toString() {
        return "Room{" +
                "structureName=" + structureName.toString() +
                ", settings=" + settings +
                ", roomSize=" + roomSize.toString() +
                ", doors=" + doors.toString() +
                '}';
    }
}
