package maxmag_change.husky.item.custom;

import maxmag_change.husky.HuskyLib;
import maxmag_change.husky.registries.RoomRegistry;
import maxmag_change.husky.utill.logic.dungeon.Dungeon;
import maxmag_change.husky.utill.logic.room.CustomIdentifier;
import maxmag_change.husky.utill.logic.room.Room;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DungeonGeneratorItem extends Item {
    public DungeonGeneratorItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();

        if (!world.isClient()) {
            //Room room = RoomRegistry.getType(new Identifier(HuskyLib.MOD_ID,"crossroad2"));
            Identifier startingRoom = new Identifier(HuskyLib.MOD_ID,"vanilla/corridor1");
            Room room = RoomRegistry.getType(startingRoom);
            if (room!=null) {
                HuskyLib.LOGGER.error(room.toString());

                Dungeon dungeon = new Dungeon(new CustomIdentifier(startingRoom.getNamespace(),startingRoom.getPath()),"vanilla",10000);

                dungeon.createDungeon((ServerWorld) world,context.getBlockPos());
            }
        }

        return ActionResult.PASS;
    }
}
