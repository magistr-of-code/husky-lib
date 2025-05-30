package maxmag_change.husky.block.entity;

import maxmag_change.husky.HuskyLib;
import maxmag_change.husky.block.ModBlocks;
import maxmag_change.husky.block.entity.custom.RoomAnchorBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlockEntities {
    public static final BlockEntityType<RoomAnchorBlockEntity> ROOM_ANCHOR_BLOCK_ENTITY =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(HuskyLib.MOD_ID,"room_anchor_be"),
                    FabricBlockEntityTypeBuilder.create(RoomAnchorBlockEntity::new, ModBlocks.ROOM_ANCHOR).build());

    public static void registerBlockEntities(){
        HuskyLib.LOGGER.info("Registering Block Entities for " + HuskyLib.MOD_ID);
    }
}
