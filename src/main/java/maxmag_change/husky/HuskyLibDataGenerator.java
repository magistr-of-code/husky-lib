package maxmag_change.husky;

import maxmag_change.husky.datagen.*;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class HuskyLibDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

		pack.addProvider(HuskyBlockTagProvider::new);
		pack.addProvider(HuskyItemTagProvider::new);
		pack.addProvider(HuskyLootTableProvider::new);
		pack.addProvider(HuskyModelProvider::new);
		pack.addProvider(HuskyRecipeProvider::new);
	}
}
