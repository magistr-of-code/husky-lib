package maxmag_change.husky.registries;

import maxmag_change.husky.HuskyLib;
import maxmag_change.husky.utill.logic.dead_end.BlockedDeadEnd;
import maxmag_change.husky.utill.logic.dead_end.DeadEnd;
import maxmag_change.husky.utill.logic.dead_end.EmptyDeadEnd;
import maxmag_change.husky.utill.logic.dead_end.vanilla.DungeonDeadEnd;
import net.minecraft.block.Blocks;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public class DeadEndRegistry {
    static Map<Identifier, DeadEnd> registrations = new HashMap<>();

    public static void register(Identifier identifier, DeadEnd room) {
        registrations.put(identifier, room);
    }

    public static DeadEnd getType(Identifier identifier) {
        DeadEnd end = registrations.get(identifier);
        if (end==null){
            end = new EmptyDeadEnd();
        }

        return end;
    }

    static {
        register(new Identifier(HuskyLib.MOD_ID, "cobbled"), new BlockedDeadEnd(Blocks.COBBLESTONE.getDefaultState()));
        register(new Identifier(HuskyLib.MOD_ID, "vanilla/dungeon"), new DungeonDeadEnd());
        register(new Identifier(HuskyLib.MOD_ID, "empty"), new EmptyDeadEnd());
    }
}
