package maxmag_change.husky.mixin;

import maxmag_change.husky.HuskyLib;
import maxmag_change.husky.registries.RoomRegistry;
import maxmag_change.husky.utill.DungeonTest;
import maxmag_change.husky.utill.logic.Room;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class ExampleMixin {
	@Inject(at = @At("TAIL"), method = "loadWorld")
	private void init(CallbackInfo info) {
		MinecraftServer minecraftServer = (MinecraftServer) (Object) this;

		Room room = RoomRegistry.getType(new Identifier(HuskyLib.MOD_ID,"crossroad1"));
		if (room!=null) {
			HuskyLib.LOGGER.error("generating...");
			HuskyLib.LOGGER.error(String.valueOf(room.getRoomSize()));
			DungeonTest.clusterTest(minecraftServer,new BlockPos(500,100,500),BlockRotation.CLOCKWISE_90);
		} else {
			HuskyLib.LOGGER.error("failed to find room :(");
		}
    }
}