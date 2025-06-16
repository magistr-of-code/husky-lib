package maxmag_change.husky.item.custom;

import maxmag_change.husky.HuskyLib;
import maxmag_change.husky.registries.RoomRegistry;
import maxmag_change.husky.utill.logic.room.Room;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RoomGeneratorItem extends Item {
    public RoomGeneratorItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();

        if (!world.isClient()) {
            //Room room = RoomRegistry.getType(new Identifier(HuskyLib.MOD_ID,"crossroad2"));
            Room room = RoomRegistry.getType(new Identifier(HuskyLib.MOD_ID,"vanilla/corridor1"));
            if (room!=null) {
                int forward = context.getStack().getOrCreateNbt().getInt("forward")/2-1;
                int rotation = context.getStack().getOrCreateNbt().getInt("rotation")/2-1;
                if (rotation==0){
                    Room.protectedGenerate(room,world, context.getBlockPos(), BlockRotation.NONE, forward);
                } else if (rotation==1){
                    Room.protectedGenerate(room,world, context.getBlockPos(), BlockRotation.CLOCKWISE_90, forward);
                } else if (rotation==2){
                    Room.protectedGenerate(room,world, context.getBlockPos(), BlockRotation.COUNTERCLOCKWISE_90, forward);
                } else {
                    Room.protectedGenerate(room,world, context.getBlockPos(), BlockRotation.CLOCKWISE_180, forward);
                }
            }
        }

        return ActionResult.PASS;
    }


    @Override
    public boolean canMine(BlockState state, World world, BlockPos pos, PlayerEntity miner) {
        if (!world.isClient()) {
            NbtCompound nbt = miner.getMainHandStack().getOrCreateNbt();

            if (nbt != null) {
                if (miner.isSneaking()){
                    int count = nbt.getInt("forward");
                    if (count>=200){
                        nbt.putInt("forward", 1);
                    } else {
                        nbt.putInt("forward", count+1);
                    }
                    int forward = nbt.getInt("forward")/2-1;
                    miner.sendMessage(Text.literal("forward:" + forward),true);
                } else {
                    int count = nbt.getInt("rotation");
                    if (count>=8){
                        nbt.putInt("rotation", 1);
                    } else {
                        nbt.putInt("rotation", count+1);
                    }
                    int rotation = nbt.getInt("rotation")/2-1;
                    miner.sendMessage(Text.literal("rotation:" + rotation),true);
                }
            }
        }
        return false;
    }
}
