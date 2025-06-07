package maxmag_change.husky.utill.logic;

import maxmag_change.husky.HuskyLib;
import net.minecraft.block.Blocks;
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
import net.minecraft.world.World;

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
        BlockPos min = StructureTemplate.transformAround(BlockPos.ofFloored(this.getRoomSize().minX,this.getRoomSize().minY,this.getRoomSize().minZ),BlockMirror.NONE,rotation,BlockPos.ORIGIN);
        BlockPos max = StructureTemplate.transformAround(BlockPos.ofFloored(this.getRoomSize().maxX,this.getRoomSize().maxY,this.getRoomSize().maxZ),BlockMirror.NONE,rotation,BlockPos.ORIGIN);

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
            optional.get().place((ServerWorld)world,pos,pos,structurePlacementData,world.getRandom(),2);
        }

        //Place room's corners
        world.setBlockState(min.add(pos), Blocks.GREEN_WOOL.getDefaultState());
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
                door.centerBlock = StructureTemplate.transformAround(door.getCenterBlock(), BlockMirror.NONE, rotation, BlockPos.ORIGIN);
                door.direction = rotation.rotate(door.getDirection());

                for(int i = 0; i < door.getBlocks().size(); ++i) {
                    BlockPos block = door.getBlocks().get(i);

                    door.blocks.set(i,StructureTemplate.transformAround(block,BlockMirror.NONE,rotation,BlockPos.ORIGIN));
                    //TODO stop placing wool
                    world.setBlockState(pos.add(door.getBlocks().get(i)).add(door.getCenterBlock()), Blocks.CYAN_WOOL.getDefaultState());
                }

                world.setBlockState(pos.add(door.getCenterBlock()), Blocks.YELLOW_WOOL.getDefaultState());

                //Generate additional rooms
                if (forward-1>0) {
                    //TODO make work
                    List<Pair<BlockRotation,Room>> matchingRooms = List.of(new Pair<>(BlockRotation.CLOCKWISE_90,new Room(new Identifier(HuskyLib.MOD_ID,"vanilla/crossroad1"))),new Pair<>(BlockRotation.CLOCKWISE_90,new Room(new Identifier(HuskyLib.MOD_ID,"vanilla/crossroad1"))));//RoomRegistry.getWithMatchingDoor(door.copy());

                    if (!matchingRooms.isEmpty()){
                        matchingRooms.forEach(matchingRoom->{
                            HuskyLib.LOGGER.error(matchingRoom.getRight().getStructureName().toString());
                        });
                        Pair<BlockRotation,Room> randomRoomPair = matchingRooms.get(world.getRandom().nextBetween(0,matchingRooms.size()-1));
                        BlockRotation randomRotation = randomRoomPair.getLeft();
                        Room randomRoom = randomRoomPair.getRight().clone();
                        BlockPos roomPoint = pos;
//                        roomPoint = roomPoint.add(door.direction.getVector());
                        roomPoint = roomPoint.add(door.getDirection().getVector().multiply(7));//(int) MathHelper.rotateBox(randomRoom.getRoomSize(),randomRotation).getZLength()));
                        //roomPoint = roomPoint.add(0,0, (int) randomRoom.roomSize.minZ);
                        randomRoom.generate(world, roomPoint,randomRotation,forward-1);
                        HuskyLib.LOGGER.error(roomPoint.toString());
                    } else {
                        HuskyLib.LOGGER.error("no matching rooms found");
                    }
                }

                this.getDoors().set(ii,door);
            }
        }
    }

    public void hasMatchingDoors(List<Pair<BlockRotation,Room>> pairs,Door door){
        Room room = this.clone();
        for(int i = 0; i < room.getDoors().size(); ++i) {
            Pair<BlockRotation, Boolean> pair = room.getDoors().get(i).hasMatchingShape(door);
            if (pair.getRight()) {
                room.getDoors().set(i,Door.EMPTY);
                pairs.add(new Pair<>(pair.getLeft(), room));
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
