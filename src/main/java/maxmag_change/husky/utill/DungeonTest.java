package maxmag_change.husky.utill;

import maxmag_change.husky.HuskyLib;
import maxmag_change.husky.registries.RoomRegistry;
import maxmag_change.husky.utill.logic.Room;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.apache.commons.lang3.SerializationUtils;

import java.util.List;

public class DungeonTest {
    public static void cloneTest(){
        Room room = RoomRegistry.getType(new Identifier(HuskyLib.MOD_ID,"crossroad1"));
        HuskyLib.LOGGER.error("room 1:" + room.getDoors().get(0).getBlocks().get(0));
        HuskyLib.LOGGER.error("room 2:" + room.clone().getDoors().get(0).getBlocks().get(0));
    }

    public static void multipleTest(MinecraftServer minecraftServer,BlockPos pos,BlockRotation rotation){
        pos = pos.add(0,10,0);
        Room.protectedGenerate(RoomRegistry.getType(new Identifier(HuskyLib.MOD_ID,"crossroad1")),minecraftServer.getOverworld(),pos, rotation,1);
        pos = pos.add(0,10,0);
        Room.protectedGenerate(RoomRegistry.getType(new Identifier(HuskyLib.MOD_ID,"crossroad2")),minecraftServer.getOverworld(),pos, rotation,1);
        pos = pos.add(0,10,0);
        Room.protectedGenerate(RoomRegistry.getType(new Identifier(HuskyLib.MOD_ID,"corridor1")),minecraftServer.getOverworld(),pos, rotation,1);
    }

    public static void clusterTest(MinecraftServer minecraftServer,BlockPos pos,BlockRotation rotation){
        for (int i = 0; i < 5; i++) {
            pos = pos.add(0,10,0);
            Room.protectedGenerate(RoomRegistry.getType(new Identifier(HuskyLib.MOD_ID,"crossroad1")),minecraftServer.getOverworld(),pos, rotation,1);
        }
    }
}
