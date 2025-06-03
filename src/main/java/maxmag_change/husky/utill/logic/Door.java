package maxmag_change.husky.utill.logic;

import com.mojang.datafixers.kinds.IdF;
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

public class Door {
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

    public List<BlockPos> getShape(){
        List<BlockPos> blocks = this.getBlocks();
        BlockPos center = this.getCenterBlock();

        for(int i = 0; i < blocks.size(); ++i) {
            blocks.set(i,blocks.get(i).subtract(center));
        }

        return blocks;
    }

    public Pair<BlockRotation, Boolean> hasMatchingShape(List<BlockPos> blocks){
        boolean matches = false;
        BlockRotation rotation = BlockRotation.NONE;

        if (this.hasMatchingShapeWithRotation(blocks,BlockRotation.NONE)){
            matches=true;
            rotation=BlockRotation.NONE;
        }
        if (this.hasMatchingShapeWithRotation(blocks,BlockRotation.CLOCKWISE_90)){
            matches=true;
            rotation=BlockRotation.CLOCKWISE_90;
        }
        if (this.hasMatchingShapeWithRotation(blocks,BlockRotation.COUNTERCLOCKWISE_90)){
            matches=true;
            rotation=BlockRotation.COUNTERCLOCKWISE_90;
        }
        if (this.hasMatchingShapeWithRotation(blocks,BlockRotation.CLOCKWISE_180)){
            matches=true;
            rotation=BlockRotation.CLOCKWISE_180;
        }

        return new Pair<>(rotation,matches);
    }

    public boolean hasMatchingShapeWithRotation(List<BlockPos> shape, BlockRotation rotation){

        List<BlockPos> blocks = this.getShape();

        blocks.replaceAll(pos -> StructureTemplate.transformAround(pos, BlockMirror.NONE, rotation, BlockPos.ORIGIN));

        return shape == blocks;

//        voxelShape.forEachBox(new VoxelShapes.BoxConsumer() {
//            @Override
//            public void consume(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
//                Box box = new Box(minX,minY,minZ,maxX,maxY,maxZ);
//                Vec3d min = StructureTemplate.transformAround(new Vec3d(minX,minY,minZ),BlockMirror.NONE, BlockRotation.NONE, BlockPos.ofFloored(voxelShape.getBoundingBox().getCenter()));
//                Vec3d max = StructureTemplate.transformAround(new Vec3d(maxX,maxY,maxZ),BlockMirror.NONE, BlockRotation.NONE, BlockPos.ofFloored(voxelShape.getBoundingBox().getCenter()));
//                box = new Box(min,max);
//                newVoxelShape[0] = VoxelShapes.union(newVoxelShape[0],VoxelShapes.cuboid(box));
//            }
//        });
//
//        return voxelShape1.equals(newVoxelShape[0]);

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
}
