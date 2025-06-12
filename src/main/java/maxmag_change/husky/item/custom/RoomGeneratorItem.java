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
            Room room = RoomRegistry.getType(new Identifier(HuskyLib.MOD_ID,"crossroad1"));
            if (room!=null) {
                HuskyLib.LOGGER.error("generating...");
                Room.protectedGenerate(room,world, context.getBlockPos(), BlockRotation.NONE, context.getStack().getOrCreateNbt().getInt("forward"));
            }
        }

        return ActionResult.PASS;
    }


    @Override
    public boolean canMine(BlockState state, World world, BlockPos pos, PlayerEntity miner) {
        NbtCompound nbt = miner.getMainHandStack().getOrCreateNbt();

        if (nbt != null) {
            int count = nbt.getInt("forward");
            if (count>=3){
                nbt.putInt("forward", 0);
            } else {
                nbt.putInt("forward", count+1);
            }
            miner.sendMessage(Text.literal(String.valueOf(nbt.getInt("forward"))),true);
        }
        return false;
    }
}
