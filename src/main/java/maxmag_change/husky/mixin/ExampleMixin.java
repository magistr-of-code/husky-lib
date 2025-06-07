package maxmag_change.husky.mixin;

import maxmag_change.husky.HuskyLib;
import maxmag_change.husky.utill.DungeonTest;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockRotation;
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

		DungeonTest.cloneTest();

		HuskyLib.LOGGER.error("generating...");
		DungeonTest.clusterTest(minecraftServer,new BlockPos(500,100,500),BlockRotation.CLOCKWISE_90);
    }
}