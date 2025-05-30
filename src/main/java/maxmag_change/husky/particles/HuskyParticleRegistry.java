package maxmag_change.husky.particles;

import io.github.fabricators_of_create.porting_lib.util.LazyRegistrar;
import io.github.fabricators_of_create.porting_lib.util.RegistryObject;
import maxmag_change.husky.HuskyLib;
import net.minecraft.particle.ParticleType;
import net.minecraft.registry.Registries;
import team.lodestar.lodestone.systems.particle.world.type.LodestoneWorldParticleType;

public class HuskyParticleRegistry {
    public static LazyRegistrar<ParticleType<?>> PARTICLES = LazyRegistrar.create(Registries.PARTICLE_TYPE, HuskyLib.MOD_ID);


    public static final RegistryObject<LodestoneWorldParticleType> RING_PARTICLE = PARTICLES.register("ring", LodestoneWorldParticleType::new);
    public static final RegistryObject<LodestoneWorldParticleType> AURA_PARTICLE = PARTICLES.register("aura", LodestoneWorldParticleType::new);
    public static final RegistryObject<LodestoneWorldParticleType> PIXEL_PARTICLE = PARTICLES.register("pixel", LodestoneWorldParticleType::new);
    public static final RegistryObject<LodestoneWorldParticleType> PENTAGRAM_PARTICLE = PARTICLES.register("pentagram", LodestoneWorldParticleType::new);
    public static final RegistryObject<SmokeParticleType> SMOKE_PARTICLE = PARTICLES.register("smoke", SmokeParticleType::new);
    public static final RegistryObject<RuneParticleType> RUNE_PARTICLE = PARTICLES.register("rune", RuneParticleType::new);
    public static final RegistryObject<SweepParticleType> SWEEP_PARTICLE = PARTICLES.register("sweep", SweepParticleType::new);
}

