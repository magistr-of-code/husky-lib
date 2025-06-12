package maxmag_change.husky.registries;

import maxmag_change.husky.HuskyLib;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class HuskySounds {
    private HuskySounds() {}

    // and is called in the mod to use the custom sound
    public static final SoundEvent MAX_PLUSHIE_SQUISH = registerSound("max_squish");
    public static final SoundEvent PARRY = registerSound("parry");

    // actual registration of all the custom SoundEvents
    private static SoundEvent registerSound(String id) {
        Identifier identifier = new Identifier(HuskyLib.MOD_ID, id);
        return Registry.register(Registries.SOUND_EVENT, identifier, SoundEvent.of(identifier));
    }

    // This static method starts class initialization, which then initializes
    public static void initialize() {
        HuskyLib.LOGGER.info("Registering sounds for " + HuskyLib.MOD_ID);
    }
}
