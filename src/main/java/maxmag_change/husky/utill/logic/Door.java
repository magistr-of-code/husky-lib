package maxmag_change.husky.utill.logic;

import com.google.common.base.MoreObjects;
import maxmag_change.husky.utill.Convertor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class Door {
    DefaultedList<BlockPos> blocks = DefaultedList.of();

    public static final Door EMPTY = new Door();

//    Door(){}
//    Door(List<BlockPos> blocks){
//        this.blocks = blocks;
//    }

    public DefaultedList<BlockPos> getBlocks() {
        return blocks;
    }

    public void setBlocks(DefaultedList<BlockPos> blocks) {
        this.blocks = blocks;
    }

    public void readNbt(NbtCompound nbtCompound){
        NbtList blocks = nbtCompound.getList("Blocks",NbtElement.COMPOUND_TYPE);
        for(int ii = 0; ii < blocks.size(); ++ii) {
            NbtCompound block = blocks.getCompound(ii);
            this.blocks.add(ii,Convertor.StringToBlock(block.getString("BlockPos")));
        }
    }

    public void writeNbt(NbtCompound nbt) {

        NbtList nbtList = new NbtList();

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
