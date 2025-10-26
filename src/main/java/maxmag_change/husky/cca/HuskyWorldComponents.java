package maxmag_change.husky.cca;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.world.WorldComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.world.WorldComponentInitializer;
import maxmag_change.husky.HuskyLib;
import net.minecraft.util.Identifier;

public class HuskyWorldComponents implements WorldComponentInitializer {
    public static final ComponentKey<DungeonComponent> DUNGEONS = ComponentRegistry.getOrCreate(new Identifier(HuskyLib.MOD_ID, "dungeons"), DungeonComponent.class);

    public static void initialize() {

    }

    @Override
    public void registerWorldComponentFactories(WorldComponentFactoryRegistry registry) {
        registry.register(DUNGEONS,DungeonComponent::new);
    }
}
