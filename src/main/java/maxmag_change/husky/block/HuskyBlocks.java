package maxmag_change.husky.block;

import maxmag_change.husky.HuskyLib;
import maxmag_change.husky.block.custom.PlushBlock;
import maxmag_change.husky.block.custom.RoomAnchorBlock;
import maxmag_change.husky.item.HuskyItems;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.enums.Instrument;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

public class HuskyBlocks {
    public static final Block ROOM_ANCHOR = registerBlock("room_anchor",
            new RoomAnchorBlock(FabricBlockSettings.copyOf(Blocks.LODESTONE).sounds(BlockSoundGroup.AMETHYST_BLOCK).hardness(1)));
    public static final Block MAX_PLUSHIE = registerBlock("max_plush",
            new PlushBlock(FabricBlockSettings.create().sounds(BlockSoundGroup.WOOL).hardness(1).hardness(3)));

    private static Block registerBlockOnly(String name, Block block){
        return Registry.register(Registries.BLOCK,new Identifier(HuskyLib.MOD_ID, name),block);
    }

    private static Block registerBlock(String name, Block block){
        registerBlockItem(name,block);
        return Registry.register(Registries.BLOCK,new Identifier(HuskyLib.MOD_ID, name),block);
    }

    public static Item registerBlockItem(String name, Block block){
        return Registry.register(Registries.ITEM,new Identifier(HuskyLib.MOD_ID,name),
                new BlockItem(block,new FabricItemSettings()));
    }

    public static void registerModBlocks() {

    }
}
