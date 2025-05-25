package maxmag_change.husky;

import maxmag_change.husky.particles.HuskyParticleRegistry;
import maxmag_change.husky.particles.SmokeParticleType;
import maxmag_change.husky.particles.SweepParticleType;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import team.lodestar.lodestone.systems.particle.world.type.LodestoneWorldParticleType;

public class HuskyLibClient implements ClientModInitializer {
    public static void registerParticleFactory() {
        ParticleFactoryRegistry.getInstance().register(HuskyParticleRegistry.RING_PARTICLE.get(), LodestoneWorldParticleType.Factory::new);
        ParticleFactoryRegistry.getInstance().register(HuskyParticleRegistry.PIXEL_PARTICLE.get(), LodestoneWorldParticleType.Factory::new);
        ParticleFactoryRegistry.getInstance().register(HuskyParticleRegistry.AURA_PARTICLE.get(), LodestoneWorldParticleType.Factory::new);
        ParticleFactoryRegistry.getInstance().register(HuskyParticleRegistry.SMOKE_PARTICLE.get(), SmokeParticleType.Factory::new);
        ParticleFactoryRegistry.getInstance().register(HuskyParticleRegistry.SWEEP_PARTICLE.get(), SweepParticleType.Factory::new);
    }

    @Override
    public void onInitializeClient() {
        registerParticleFactory();
    }
}
