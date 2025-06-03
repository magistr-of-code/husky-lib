package maxmag_change.husky.utill.logic;

import maxmag_change.husky.HuskyLib;
import maxmag_change.husky.registries.RoomRegistry;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.StructureBlockBlockEntity;
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
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;
import java.util.Optional;

public class Room {
    Identifier structureName;
    Box roomSize = new Box(0,0,0,0,0,0);
    DefaultedList<Door> doors = DefaultedList.ofSize(27, Door.EMPTY);

    public Room(Identifier structureName){
        this.structureName = structureName;
    }

    public Room(Identifier structureName,Box roomSize,List<Door> doors){
        this.structureName = structureName;
        this.roomSize = roomSize;
        for (int i = 0; i < doors.size(); i++) {
            this.doors.set(i,doors.get(i));
        }
    }

    public void generate(World world,BlockPos pos,BlockRotation rotation){

        //rotate room bounding box
        BlockPos min = StructureTemplate.transformAround(BlockPos.ofFloored(roomSize.minX,roomSize.minY,roomSize.minZ),BlockMirror.NONE,rotation,pos);
        BlockPos max = StructureTemplate.transformAround(BlockPos.ofFloored(roomSize.maxX,roomSize.maxY,roomSize.maxZ),BlockMirror.NONE,rotation,pos);

        roomSize = new Box(min,max);

        //place build
        if (world != null && !world.isClient()) {
            StructurePlacementData structurePlacementData = new StructurePlacementData()
                    .setMirror(BlockMirror.NONE)
                    .setRotation(rotation)
                    .setIgnoreEntities(false);

            StructureTemplateManager structureTemplateManager = world.getServer().getStructureTemplateManager();
            Optional<StructureTemplate> optional;
            optional = structureTemplateManager.getTemplate(this.getStructureName());
            optional.get().place((ServerWorld)world,pos, pos,structurePlacementData,world.getRandom(),2);
        }

        //rotate doors and generate more rooms
        if (this.doors!=null) {
            for (Door door : this.doors){
                if (door!=Door.EMPTY) {
                    door.centerBlock = StructureTemplate.transformAround(door.getCenterBlock(),BlockMirror.NONE,rotation,pos);
                    door.direction = rotation.rotate(door.direction);

                    for(int i = 0; i < door.getBlocks().size(); ++i) {
                        door.blocks.set(i,StructureTemplate.transformAround(door.getBlocks().get(i),BlockMirror.NONE,rotation,pos));
                        //TODO stop placing wool
                        world.setBlockState(pos.add(door.blocks.get(i)), Blocks.RED_WOOL.getDefaultState());
                    }

                    List<Pair<BlockRotation,Room>> matchingRooms = RoomRegistry.getWithMatchingDoorShapes(door.getShape());

                    if (!matchingRooms.isEmpty()){
                        Pair<BlockRotation,Room> randomRoomPair = matchingRooms.get(world.getRandom().nextBetween(0,matchingRooms.size()-1));
                        BlockPos roomPoint = door.getCenterBlock().add(door.getDirection().getVector()).add(pos);
                        BlockRotation randomRotation = randomRoomPair.getLeft();
                        Room randomRoom = randomRoomPair.getRight();
                        roomPoint = roomPoint.add(0,0, (int) randomRoom.roomSize.minZ);
                        //randomRoom.generateWithoutNeighbors(world, roomPoint,randomRotation);
                        HuskyLib.LOGGER.error(roomPoint.toString());
                    } else {
                        HuskyLib.LOGGER.error("no matching rooms found");
                    }
                }
            }
        }
    }

    public void generateWithoutNeighbors(World world,BlockPos pos,BlockRotation rotation){

        //place build
        if (world != null && !world.isClient()) {
            StructurePlacementData structurePlacementData = new StructurePlacementData()
                    .setMirror(BlockMirror.NONE)
                    .setRotation(rotation)
                    .setIgnoreEntities(false);

            StructureTemplateManager structureTemplateManager = world.getServer().getStructureTemplateManager();
            Optional<StructureTemplate> optional;
            optional = structureTemplateManager.getTemplate(this.getStructureName());
            optional.get().place((ServerWorld)world,pos, pos,structurePlacementData,world.getRandom(),2);
        }

        //rotate room bounding box
        BlockPos min = StructureTemplate.transformAround(BlockPos.ofFloored(roomSize.minX,roomSize.minY,roomSize.minZ),BlockMirror.NONE,rotation,pos);
        BlockPos max = StructureTemplate.transformAround(BlockPos.ofFloored(roomSize.maxX,roomSize.maxY,roomSize.maxZ),BlockMirror.NONE,rotation,pos);

        roomSize = new Box(min,max);

        //rotate doors and generate more rooms
        if (this.doors!=null) {
            for (Door door : this.doors){
                if (door!=Door.EMPTY) {
                    door.centerBlock = StructureTemplate.transformAround(door.getCenterBlock(),BlockMirror.NONE,rotation,pos);
                    door.direction = rotation.rotate(door.direction);

                    for(int i = 0; i < door.getBlocks().size(); ++i) {
                        door.blocks.set(i,StructureTemplate.transformAround(door.getBlocks().get(i),BlockMirror.NONE,rotation,pos));
                        //TODO stop placing wool
                        world.setBlockState(pos.add(door.blocks.get(i)), Blocks.RED_WOOL.getDefaultState());
                    }
                }
            }
        }
    }

    public Box getRoomSize() {
        return roomSize;
    }

    public DefaultedList<Door> getDoors() {
        return doors;
    }

    public DefaultedList<BlockPos> getDoorPosition(int index) {
        DefaultedList<Door> doors = this.getDoors();
        return doors.get(index).getBlocks();
    }

    public Identifier getStructureName() {
        return structureName;
    }
}
