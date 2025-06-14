package maxmag_change.husky.entity;

import maxmag_change.husky.HuskyLib;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class HuskyEntities {
    public static EntityType<ChairEntity> CHAIR = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(HuskyLib.MOD_ID, "chair"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, ChairEntity::new).dimensions(EntityDimensions.changing(0f, 0f)).disableSummon().fireImmune().build());

    public static void registerAttribute(EntityType<? extends LivingEntity> entityType, DefaultAttributeContainer.Builder builder) {
        FabricDefaultAttributeRegistry.register(entityType, builder);
    }

    public static void registerModEntities() {
        registerAttribute(CHAIR, ChairEntity.createMobAttributes());

        HuskyLib.LOGGER.info("Registering entities for " + HuskyLib.MOD_ID);
    }
}
