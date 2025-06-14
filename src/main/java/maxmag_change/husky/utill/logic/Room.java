package maxmag_change.husky.utill.logic;

import maxmag_change.husky.HuskyLib;
import maxmag_change.husky.registries.RoomRegistry;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.JigsawBlockEntity;
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
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

import javax.swing.event.HyperlinkEvent;
import java.util.List;
import java.util.Optional;

public class Room implements Cloneable {
    private final Identifier structureName;
    private Box roomSize;
    private DefaultedList<Door> doors;

    public Room(Identifier structureName,Box roomSize,List<Door> doors){
        this.structureName = structureName;
        this.roomSize = roomSize;
        this.doors = DefaultedList.ofSize(27, Door.EMPTY);
        for (int i = 0; i < doors.size(); i++) {
            this.doors.set(i,doors.get(i));
        }
    }

    public Room(Identifier structureName){
        this(structureName,new Box(0,0,0,0,0,0),DefaultedList.ofSize(27, Door.EMPTY));
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

    public static void protectedGenerate(Room room, World world, BlockPos pos, BlockRotation rotation, int forward){
        Room room1 = room.clone();
        room1.generate(world,pos,rotation,forward);
    }

    public void generate(World world,BlockPos pos,BlockRotation rotation,int forward){
        //Don't generate if forward is set to 0
        if (forward<=0){
            return;
        }
        //Don't generate if world is null
        if (world == null){
            return;
        }

        //Rotate room's bounding box
        BlockPos boxCenter = BlockPos.ofFloored(this.getRoomSize().getCenter());

        BlockPos min = StructureTemplate.transformAround(BlockPos.ofFloored(this.getRoomSize().minX,this.getRoomSize().minY,this.getRoomSize().minZ),BlockMirror.NONE,rotation,boxCenter);
        BlockPos max = StructureTemplate.transformAround(BlockPos.ofFloored(this.getRoomSize().maxX,this.getRoomSize().maxY,this.getRoomSize().maxZ),BlockMirror.NONE,rotation,boxCenter);

        this.setRoomSize(new Box(min,max));

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

        //Place room's corners
        //TODO stop placing wool
        world.setBlockState(min.add(pos), Blocks.GREEN_WOOL.getDefaultState());
        world.setBlockState(pos.add(boxCenter), Blocks.PINK_WOOL.getDefaultState());
        world.setBlockState(max.add(pos), Blocks.RED_WOOL.getDefaultState());

        //Go through each door
        if (this.getDoors()!=null || this.getDoors().isEmpty()) {
            for(int ii = 0; ii < this.getDoors().size(); ++ii) {
                Door door = this.getDoors().get(ii);

                //Go to the next door if this door is empty
                if (door==Door.EMPTY) {
                    continue;
                }

                //Rotate the door
                door.centerBlock = StructureTemplate.transformAround(door.getCenterBlock(), BlockMirror.NONE, rotation, boxCenter);
                door.direction = rotation.rotate(door.getDirection());

                for(int i = 0; i < door.getBlocks().size(); ++i) {
                    BlockPos block = door.getBlocks().get(i);

                    door.blocks.set(i,StructureTemplate.transformAround(block,BlockMirror.NONE,rotation,BlockPos.ORIGIN));
                    //TODO stop placing wool
                    if (forward==1){
                        world.setBlockState(pos.add(door.getBlocks().get(i)).add(door.getCenterBlock()), Blocks.RED_WOOL.getDefaultState());
                    } else {
                        world.setBlockState(pos.add(door.getBlocks().get(i)).add(door.getCenterBlock()), Blocks.CYAN_WOOL.getDefaultState());
                    }
                }

                world.setBlockState(pos.add(door.getCenterBlock()), Blocks.YELLOW_WOOL.getDefaultState());

                //Generate additional rooms
                if (forward-1>0) {
                    List<MatchingRoom> matchingRooms = RoomRegistry.getWithMatchingDoor(door.clone()); //List.of(new Pair<>(BlockRotation.CLOCKWISE_90,new Room(new Identifier(HuskyLib.MOD_ID,"vanilla/crossroad1"))),new Pair<>(BlockRotation.CLOCKWISE_90,new Room(new Identifier(HuskyLib.MOD_ID,"vanilla/crossroad1"))));

                    if (!matchingRooms.isEmpty()){
                        MatchingRoom matchingRoom = matchingRooms.get(world.getRandom().nextBetween(0,matchingRooms.size()-1));
                        BlockRotation randomRotation = matchingRoom.getRotation();
                        Room randomRoom = matchingRoom.getRoom().clone();
                        Door randomDoor = randomRoom.getDoors().get(matchingRoom.getMatchingDoorIndex());
                        BlockPos centerRandomDoor = StructureTemplate.transformAround(randomDoor.getCenterBlock(), BlockMirror.NONE, randomRotation, BlockPos.ofFloored(randomRoom.getRoomSize().getCenter()));

                        BlockPos roomPoint = door.getCenterBlock().add(pos);

                        roomPoint = roomPoint.subtract(centerRandomDoor.add(pos));

                        roomPoint = roomPoint.add(pos);

                        randomRoom.getDoors().set(matchingRoom.getMatchingDoorIndex(),Door.EMPTY);

                        Room.protectedGenerate(randomRoom,world, roomPoint,randomRotation,forward-1);
                        //TODO stop logging
                    } else {
                        HuskyLib.LOGGER.error("no matching rooms found");
                    }
                }

                this.getDoors().set(ii,door);
            }
        }
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

            Room clone = new Room(room.getStructureName(),new Box(room.getRoomSize().minX,room.getRoomSize().minY,room.getRoomSize().minZ,room.getRoomSize().maxX,room.getRoomSize().maxY,room.getRoomSize().maxZ),doors);
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
