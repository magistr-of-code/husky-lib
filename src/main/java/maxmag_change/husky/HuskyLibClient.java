package maxmag_change.husky;

import maxmag_change.husky.block.ModBlocks;
import maxmag_change.husky.block.entity.ModBlockEntities;
import maxmag_change.husky.block.entity.custom.RoomAnchorBlockEntity;
import maxmag_change.husky.block.entity.renderer.RoomAnchorEntityRenderer;
import maxmag_change.husky.particles.HuskyParticleRegistry;
import maxmag_change.husky.particles.RuneParticleType;
import maxmag_change.husky.particles.SmokeParticleType;
import maxmag_change.husky.particles.SweepParticleType;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientBlockEntityEvents;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerBlockEntityEvents;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.entity.EndGatewayBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.client.render.block.entity.EndGatewayBlockEntityRenderer;
import net.minecraft.nbt.NbtCompound;
import team.lodestar.lodestone.handlers.RenderHandler;
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

        BlockEntityRendererFactories.register(ModBlockEntities.ROOM_ANCHOR_BLOCK_ENTITY, RoomAnchorEntityRenderer::new);


        ServerBlockEntityEvents.BLOCK_ENTITY_LOAD.register((blockEntity, serverWorld) -> {
            if (blockEntity instanceof RoomAnchorBlockEntity roomAnchor){
                roomAnchor.toUpdatePacket();
            }
        });
    }
}
