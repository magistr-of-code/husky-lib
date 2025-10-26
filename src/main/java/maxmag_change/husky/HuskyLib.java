package maxmag_change.husky;

import maxmag_change.husky.block.HuskyBlocks;
import maxmag_change.husky.block.entity.HuskyBlockEntities;
import maxmag_change.husky.cca.HuskyWorldComponents;
import maxmag_change.husky.entity.HuskyEntities;
import maxmag_change.husky.item.HuskyItems;
import maxmag_change.husky.particles.HuskyParticleRegistry;
import maxmag_change.husky.registries.HuskyCommands;
import maxmag_change.husky.registries.HuskySounds;
import maxmag_change.husky.registries.RoomRegistry;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.LifecycledResourceManager;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.core.jmx.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

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
		HuskyCommands.registerModCommands();
		HuskyWorldComponents.initialize();

		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
			@Override
			public Identifier getFabricId() {
				return new Identifier(MOD_ID, "rooms");
			}

			@Override
			public void reload(ResourceManager manager) {
				// Clear Caches Here

				RoomRegistry.loadRooms(manager);
			}
		});

		ServerLifecycleEvents.SERVER_STARTED.register((minecraftServer) -> {
			RoomRegistry.loadRooms(minecraftServer.getResourceManager());
		});

		LOGGER.info("Hello Fabric world!");
	}
}