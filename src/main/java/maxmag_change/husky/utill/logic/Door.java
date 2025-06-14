package maxmag_change.husky.utill.logic;

import com.mojang.datafixers.kinds.IdF;
import maxmag_change.husky.HuskyLib;
import maxmag_change.husky.utill.Convertor;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Pair;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;

import java.util.List;

public class Door implements Cloneable{
    DefaultedList<BlockPos> blocks = DefaultedList.of();
    BlockPos centerBlock = BlockPos.ORIGIN;
    Direction direction = Direction.EAST;

    public static final Door EMPTY = new Door();

    public Door(){}

    Door(DefaultedList<BlockPos> blocks,BlockPos centerBlock,Direction direction){
        this.blocks = blocks;
        this.centerBlock = centerBlock;
        this.direction = direction;
    }

    Door(List<BlockPos> blockList, BlockPos centerBlock, Direction direction){
        for (int i = 0; i < blockList.size(); i++) {
            this.blocks.add(i,blockList.get(i));
        }
        this.centerBlock = centerBlock;
        this.direction = direction;
    }

    public Pair<BlockRotation, Boolean> hasMatchingShape(Door door){
        boolean matches = false;
        BlockRotation rotation = BlockRotation.NONE;

        if (this.hasMatchingShapeWithRotation(door,BlockRotation.NONE)) {
            matches = true;
            rotation = BlockRotation.NONE;
        } else if (this.hasMatchingShapeWithRotation(door,BlockRotation.CLOCKWISE_90)) {
            matches = true;
            rotation = BlockRotation.CLOCKWISE_90;
        }
//        } else if (this.hasMatchingShapeWithRotation(door,BlockRotation.CLOCKWISE_180)){
//            matches=true;
//            rotation=BlockRotation.CLOCKWISE_180;

//        else if (this.hasMatchingShapeWithRotation(door,BlockRotation.COUNTERCLOCKWISE_90)){
//            matches=true;
//            rotation=BlockRotation.COUNTERCLOCKWISE_90;
//        }


        return new Pair<>(rotation,matches);
    }

    public boolean hasMatchingShapeWithRotation(Door door, BlockRotation rotation){

        List<BlockPos> blocks = this.clone().getBlocks();
        List<BlockPos> shape = door.clone().getBlocks();

        shape.replaceAll(pos -> StructureTemplate.transformAround(pos, BlockMirror.NONE, rotation, BlockPos.ORIGIN));

        if (blocks.size()!=shape.size()){
            return false;
        }

        boolean test1 = rotation.rotate(this.getDirection()) == door.getDirection().getOpposite();
        boolean test2 = hasAllElements(blocks,shape);
        boolean test3 = hasAllElements(shape,blocks);

        if (!test1){
            return false;
        }

        if (!test2){
            return false;
        }

        if (!test3){
            return false;
        }

        return true;
//        return true;

    }

    public static boolean hasAllElements(List<BlockPos> blocks2,List<BlockPos> shape2){
        List<BlockPos> blocks = new java.util.ArrayList<>(List.copyOf(blocks2));
        List<BlockPos> shape = new java.util.ArrayList<>(List.copyOf(shape2));

        //blocks.forEach(shape::remove);
        for(int i = 0; i < blocks.size(); ++i) {
            shape.remove(blocks.get(i));
        }

        return shape.isEmpty();
    }

    public DefaultedList<BlockPos> getBlocks() {
        return blocks;
    }

    public BlockPos getCenterBlock() {
        return centerBlock;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setBlocks(DefaultedList<BlockPos> blocks) {
        this.blocks = blocks;
    }

    public void readNbt(NbtCompound nbtCompound){
        this.direction = Direction.byName(nbtCompound.getString("Direction"));
        this.centerBlock = Convertor.StringToBlock(nbtCompound.getString("CenterBlock"));

        //blocks
        NbtList blocks = nbtCompound.getList("Blocks",NbtElement.COMPOUND_TYPE);
        for(int ii = 0; ii < blocks.size(); ++ii) {
            NbtCompound block = blocks.getCompound(ii);
            this.blocks.add(ii,Convertor.StringToBlock(block.getString("BlockPos")));
        }
    }

    public void writeNbt(NbtCompound nbt) {

        NbtList nbtList = new NbtList();

        nbt.putString("CenterBlock",Convertor.BlockToString(this.centerBlock));
        nbt.putString("Direction",this.direction.toString());

        //blocks
        if (this.getBlocks()!=null){
            for(int i = 0; i < this.getBlocks().size(); ++i) {
                BlockPos block = this.getBlocks().get(i);
                NbtCompound nbtCompound = new NbtCompound();
                nbtCompound.putByte("Block", (byte)i);
                nbtCompound.putString("BlockPos", Convertor.BlockToString(block));
                nbtList.add(nbtCompound);
            }

            nbt.put("Blocks", nbtList);
        }
    }

    @Override
    public Door clone() {
        try {
            Door door = (Door) super.clone();

            DefaultedList<BlockPos> blocks = DefaultedList.of();

            for (int i = 0; i < door.getBlocks().size(); i++) {
                BlockPos blockPos = door.getBlocks().get(i);
                if (blockPos!=BlockPos.ORIGIN){
                    blocks.add(i,new BlockPos(door.getBlocks().get(i)));
                }
            }

            return new Door(blocks,new BlockPos(this.getCenterBlock()),door.getDirection());
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
