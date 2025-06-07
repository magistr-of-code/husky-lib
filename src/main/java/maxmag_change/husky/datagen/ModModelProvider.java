package maxmag_change.husky.datagen;

import maxmag_change.husky.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Models;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {

    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
            itemModelGenerator.register(ModItems.DOOR_SELECTOR, Models.GENERATED);
            itemModelGenerator.register(ModItems.ROOM_SELECTOR, Models.GENERATED);
            itemModelGenerator.register(ModItems.ROOM_GENERATOR, Models.GENERATED);
    }
}
