package maxmag_change.husky.item.custom;

import maxmag_change.husky.HuskyLib;
import maxmag_change.husky.block.entity.custom.RoomAnchorBlockEntity;
import maxmag_change.husky.utill.Convertor;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.block.entity.EndGatewayBlockEntityRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RoomSelectorItem extends Item {
    public RoomSelectorItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        if (world.getBlockEntity(context.getBlockPos()) instanceof RoomAnchorBlockEntity roomAnchor){
            if (giveData(roomAnchor,context.getStack())){
                return ActionResult.PASS;
            } else {
                return ActionResult.FAIL;
            }
        }

        NbtCompound nbt = context.getStack().getOrCreateNbt();
        if (nbt != null) {
            nbt.putString("firstPoint", Convertor.BlockToString(context.getBlockPos()));
        }

        return ActionResult.PASS;
    }

    public boolean giveData(RoomAnchorBlockEntity roomAnchor, ItemStack stack) {
        NbtCompound points =  new NbtCompound();
        String firstPoint = stack.getOrCreateNbt().getString("firstPoint");
        String secondPoint = stack.getOrCreateNbt().getString("secondPoint");

        if (!firstPoint.isEmpty() && !secondPoint.isEmpty()){

            BlockPos point1 = Convertor.StringToBlock(firstPoint).subtract(roomAnchor.getPos());
            BlockPos point2 = Convertor.StringToBlock(secondPoint).subtract(roomAnchor.getPos());

            points.putString("Point1",Convertor.BlockToString(point1));
            points.putString("Point2",Convertor.BlockToString(point2));
            NbtCompound nbt = new NbtCompound();
            nbt.put("Points", points);
            roomAnchor.readNbt(nbt);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean canMine(BlockState state, World world, BlockPos pos, PlayerEntity miner) {
        NbtCompound nbt = miner.getMainHandStack().getOrCreateNbt();

        if (nbt != null) {
            nbt.putString("secondPoint", Convertor.BlockToString(pos));
        }
        return false;
    }
}
