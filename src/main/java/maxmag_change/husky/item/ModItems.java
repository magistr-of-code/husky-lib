package maxmag_change.husky.item;

import maxmag_change.husky.HuskyLib;
import maxmag_change.husky.item.custom.DoorSelectorItem;
import maxmag_change.husky.item.custom.RoomGeneratorItem;
import maxmag_change.husky.item.custom.RoomSelectorItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

public class ModItems {
    public static final Item DOOR_SELECTOR = registerItem("door_selector",new DoorSelectorItem(new FabricItemSettings().maxCount(1).rarity(Rarity.EPIC)));
    public static final Item ROOM_SELECTOR = registerItem("room_selector",new RoomSelectorItem(new FabricItemSettings().maxCount(1).rarity(Rarity.EPIC)));
    public static final Item ROOM_GENERATOR = registerItem("room_generator",new RoomGeneratorItem(new FabricItemSettings().maxCount(1).rarity(Rarity.EPIC)));

    private static void addItemsToIngredientItemGroup(FabricItemGroupEntries entries) {
        //entries.add(DREAMER);
    }

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(HuskyLib.MOD_ID, name), item);
    }



    public static void registerModItems() {

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(ModItems::addItemsToIngredientItemGroup);

        HuskyLib.LOGGER.info("Registering mod items for " + HuskyLib.MOD_ID);
    }
}
