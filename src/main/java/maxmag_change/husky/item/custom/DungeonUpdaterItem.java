package maxmag_change.husky.item.custom;

import maxmag_change.husky.HuskyLib;
import maxmag_change.husky.cca.HuskyWorldComponents;
import maxmag_change.husky.registries.RoomRegistry;
import maxmag_change.husky.utill.logic.dungeon.Dungeon;
import maxmag_change.husky.utill.logic.room.CustomIdentifier;
import maxmag_change.husky.utill.logic.room.LastRoom;
import maxmag_change.husky.utill.logic.room.Room;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.List;

public class DungeonUpdaterItem extends Item {
    public DungeonUpdaterItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World CWorld = context.getWorld();

        if (!CWorld.isClient()) {
            //Room room = RoomRegistry.getType(new Identifier(HuskyLib.MOD_ID,"crossroad2"));
            ServerWorld world = (ServerWorld) CWorld;

            HuskyWorldComponents.DUNGEONS.get(world).idToDungeon.forEach((integer, dungeon) -> {
                if (dungeon.lastRooms.isEmpty() || dungeon.rooms>dungeon.settings.maxRooms){
                    HuskyWorldComponents.DUNGEONS.get(world).idToDungeon.remove(dungeon.id,dungeon);
                } else {
                    List<LastRoom> roomList = List.copyOf(dungeon.lastRooms);
                    roomList.forEach(lastRoom -> {
                        dungeon.generateBranch(world,lastRoom);
                        dungeon.lastRooms.remove(lastRoom);
                    });
                }
            });
        }

        return ActionResult.PASS;
    }
}
