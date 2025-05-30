package maxmag_change.husky;

import maxmag_change.husky.block.ModBlocks;
import maxmag_change.husky.block.entity.ModBlockEntities;
import maxmag_change.husky.item.ModItems;
import maxmag_change.husky.particles.HuskyParticleRegistry;
import net.fabricmc.api.ModInitializer;

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

		LOGGER.info("Hello Fabric world!");
	}
}