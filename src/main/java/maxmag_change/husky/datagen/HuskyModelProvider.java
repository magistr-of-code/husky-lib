package maxmag_change.husky.datagen;

import maxmag_change.husky.item.HuskyItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Models;

public class HuskyModelProvider extends FabricModelProvider {
    public HuskyModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {

    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
            itemModelGenerator.register(HuskyItems.DOOR_SELECTOR, Models.GENERATED);
            itemModelGenerator.register(HuskyItems.ROOM_SELECTOR, Models.GENERATED);
            itemModelGenerator.register(HuskyItems.ROOM_GENERATOR, Models.GENERATED);
    }
}
