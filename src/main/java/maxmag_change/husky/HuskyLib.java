package maxmag_change.husky;

import maxmag_change.husky.block.ModBlocks;
import maxmag_change.husky.block.entity.ModBlockEntities;
import maxmag_change.husky.item.ModItems;
import maxmag_change.husky.particles.HuskyParticleRegistry;
import maxmag_change.husky.registries.RoomRegistry;
import maxmag_change.husky.utill.logic.Room;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerBlockEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HuskyLib implements ModInitializer {
	public static final String MOD_ID = "husky";


	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		HuskyParticleRegistry.PARTICLES.register();
		ModBlocks.registerModBlocks();
		ModItems.registerModItems();
		ModBlockEntities.registerBlockEntities();

		ServerLifecycleEvents.SERVER_STARTING.register((minecraftServer) -> {
			RoomRegistry.loadRooms(minecraftServer.getResourceManager());
		});

		ServerLifecycleEvents.SERVER_STARTED.register(new ServerLifecycleEvents.ServerStarted() {
            @Override
            public void onServerStarted(MinecraftServer minecraftServer) {

            }
        });

		LOGGER.info("Hello Fabric world!");
	}
}