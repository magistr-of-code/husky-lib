package maxmag_change.husky.item.custom;

import maxmag_change.husky.HuskyLib;
import maxmag_change.husky.registries.RoomRegistry;
import maxmag_change.husky.utill.logic.Room;
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
                int number = context.getStack().getOrCreateNbt().getInt("forward")/2-1;
                if (number==0){
                    Room.protectedGenerate(room,world, context.getBlockPos(), BlockRotation.NONE, 3);
                } else if (number==1){
                    Room.protectedGenerate(room,world, context.getBlockPos(), BlockRotation.CLOCKWISE_90, 3);
                } else if (number==2){
                    Room.protectedGenerate(room,world, context.getBlockPos(), BlockRotation.COUNTERCLOCKWISE_90, 3);
                } else {
                    Room.protectedGenerate(room,world, context.getBlockPos(), BlockRotation.CLOCKWISE_180, 3);
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
                int count = nbt.getInt("forward");
                if (count>=8){
                    nbt.putInt("forward", 1);
                } else {
                    nbt.putInt("forward", count+1);
                }
                int number = nbt.getInt("forward")/2-1;
                miner.sendMessage(Text.literal(String.valueOf(number)),true);
            }
        }
        return false;
    }
}
