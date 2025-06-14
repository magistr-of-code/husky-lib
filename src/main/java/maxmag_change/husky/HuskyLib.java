package maxmag_change.husky;

import maxmag_change.husky.block.HuskyBlocks;
import maxmag_change.husky.block.entity.HuskyBlockEntities;
import maxmag_change.husky.entity.HuskyEntities;
import maxmag_change.husky.item.HuskyItems;
import maxmag_change.husky.particles.HuskyParticleRegistry;
import maxmag_change.husky.registries.HuskySounds;
import maxmag_change.husky.registries.RoomRegistry;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HuskyLib implements ModInitializer {
	public static final String MOD_ID = "husky";


	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		HuskyParticleRegistry.PARTICLES.register();
		HuskySounds.initialize();
		HuskyBlocks.registerModBlocks();
		HuskyEntities.registerModEntities();
		HuskyItems.registerModItems();
		HuskyBlockEntities.registerBlockEntities();

		ServerLifecycleEvents.SERVER_STARTING.register((minecraftServer) -> {
			RoomRegistry.loadRooms(minecraftServer.getResourceManager());
		});

		LOGGER.info("Hello Fabric world!");
	}
}