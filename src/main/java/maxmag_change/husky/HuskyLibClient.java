package maxmag_change.husky;

import maxmag_change.husky.block.entity.HuskyBlockEntities;
import maxmag_change.husky.block.entity.custom.PlushBlockEntity;
import maxmag_change.husky.block.entity.custom.RoomAnchorBlockEntity;
import maxmag_change.husky.block.entity.renderer.PlushBlockEntityRenderer;
import maxmag_change.husky.block.entity.renderer.RoomAnchorEntityRenderer;
import maxmag_change.husky.entity.HuskyEntities;
import maxmag_change.husky.entity.renderer.ChairEntityRenderer;
import maxmag_change.husky.particles.HuskyParticleRegistry;
import maxmag_change.husky.particles.RuneParticleType;
import maxmag_change.husky.particles.SmokeParticleType;
import maxmag_change.husky.particles.SweepParticleType;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerBlockEntityEvents;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import team.lodestar.lodestone.systems.particle.world.type.LodestoneWorldParticleType;

public class HuskyLibClient implements ClientModInitializer {
    public static void registerParticleFactory() {
        ParticleFactoryRegistry.getInstance().register(HuskyParticleRegistry.RING_PARTICLE.get(), LodestoneWorldParticleType.Factory::new);
        ParticleFactoryRegistry.getInstance().register(HuskyParticleRegistry.PIXEL_PARTICLE.get(), LodestoneWorldParticleType.Factory::new);
        ParticleFactoryRegistry.getInstance().register(HuskyParticleRegistry.PENTAGRAM_PARTICLE.get(), LodestoneWorldParticleType.Factory::new);
        ParticleFactoryRegistry.getInstance().register(HuskyParticleRegistry.AURA_PARTICLE.get(), LodestoneWorldParticleType.Factory::new);
        ParticleFactoryRegistry.getInstance().register(HuskyParticleRegistry.SMOKE_PARTICLE.get(), SmokeParticleType.Factory::new);
        ParticleFactoryRegistry.getInstance().register(HuskyParticleRegistry.RUNE_PARTICLE.get(), RuneParticleType.Factory::new);
        ParticleFactoryRegistry.getInstance().register(HuskyParticleRegistry.SWEEP_PARTICLE.get(), SweepParticleType.Factory::new);
    }

    @Override
    public void onInitializeClient() {
        registerParticleFactory();

        BlockEntityRendererFactories.register(HuskyBlockEntities.ROOM_ANCHOR_BLOCK_ENTITY, RoomAnchorEntityRenderer::new);
        BlockEntityRendererFactories.register(HuskyBlockEntities.PLUSH_BLOCK_ENTITY, PlushBlockEntityRenderer::new);

        EntityRendererRegistry.register(HuskyEntities.CHAIR, ChairEntityRenderer::new);


        ServerBlockEntityEvents.BLOCK_ENTITY_LOAD.register((blockEntity, serverWorld) -> {
            if (blockEntity instanceof RoomAnchorBlockEntity roomAnchor){
                roomAnchor.toUpdatePacket();
            }
            if (blockEntity instanceof PlushBlockEntity plushBlock){
                plushBlock.toUpdatePacket();
            }
        });
    }
}
