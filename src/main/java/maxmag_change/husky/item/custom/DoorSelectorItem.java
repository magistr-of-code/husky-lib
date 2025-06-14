package maxmag_change.husky.item.custom;

import maxmag_change.husky.block.entity.custom.RoomAnchorBlockEntity;
import maxmag_change.husky.utill.Convertor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Objects;

public class DoorSelectorItem extends Item{
    public DoorSelectorItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        NbtCompound nbt = context.getStack().getOrCreateNbt();
        NbtList nbtList = nbt.getList("Blocks", NbtElement.COMPOUND_TYPE);

        String blockSTR = Convertor.BlockToString(context.getBlockPos());

        World world = context.getWorld();
        if (world.getBlockEntity(context.getBlockPos()) instanceof RoomAnchorBlockEntity roomAnchor){
            if (!nbtList.isEmpty() && !nbt.getString("CenterBlock").isEmpty() && !nbt.getString("Direction").isEmpty()) {

                giveData(roomAnchor,context.getStack());
                nbt.put("Blocks",new NbtList());
                nbt.putString("CenterBlock","");
                nbt.putString("Direction","");
            }
            return ActionResult.SUCCESS;
        }

        if (hasMatching(nbtList,blockSTR)){
            nbt.putString("CenterBlock",blockSTR);
            nbt.putString("Direction",context.getSide().getOpposite().toString());
        } else {
            NbtCompound block = new NbtCompound();
            block.putByte("Block", (byte) (nbtList.size()+1));
            block.putString("BlockPos", blockSTR);
            nbtList.add(block);
            nbt.put("Blocks",nbtList);
        }

        return ActionResult.SUCCESS;
    }

    public boolean hasMatching(NbtList nbtList,String string){
        boolean value = false;
        for(int i = 0; i < nbtList.size(); ++i) {
            NbtCompound compound = nbtList.getCompound(i);
            if (Objects.equals(compound.getString("BlockPos"), string)){
                value=true;
            }
        }
        return value;
    }

    public void giveData(RoomAnchorBlockEntity roomAnchor, ItemStack stack) {
        NbtList nbtList = new NbtList();
        NbtCompound compound = new NbtCompound();

        int j = 0;
        for(;j < roomAnchor.getDoors().size() && !roomAnchor.getDoors().get(j).getBlocks().isEmpty(); ++j);

        compound.putByte("Door", (byte) j);

        BlockPos centralPos = Convertor.StringToBlock(stack.getOrCreateNbt().getString("CenterBlock")).subtract(roomAnchor.getPos());
        compound.putString("CenterBlock",Convertor.BlockToString(centralPos));
        compound.putString("Direction",stack.getOrCreateNbt().getString("Direction"));

        //blocks
        NbtList blocks = stack.getOrCreateNbt().getList("Blocks", NbtElement.COMPOUND_TYPE);
        //Normalize BlockPos
        for(int i = 0; i < blocks.size(); ++i) {
            NbtCompound block = blocks.getCompound(i);
            BlockPos blockPos = Convertor.StringToBlock(block.getString("BlockPos")).subtract(Convertor.StringToBlock(stack.getOrCreateNbt().getString("CenterBlock")));
            block.putString("BlockPos",Convertor.BlockToString(blockPos));
            blocks.set(i,block);
        }
        compound.put("Blocks",blocks);
        nbtList.add(compound);

        NbtCompound nbt = new NbtCompound();
        nbt.put("Doors", nbtList);
        roomAnchor.readNbt(nbt);
    }
}
