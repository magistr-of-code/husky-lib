package maxmag_change.husky.block.entity.renderer;

import maxmag_change.husky.block.entity.custom.PlushBlockEntity;
import maxmag_change.husky.block.entity.custom.RoomAnchorBlockEntity;
import maxmag_change.husky.mixin.client.BlockRenderManagerAccessor;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.NotNull;

public class PlushBlockEntityRenderer implements BlockEntityRenderer<PlushBlockEntity> {
    private final BlockRenderManager renderManager;

    public PlushBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        this.renderManager = ctx.getRenderManager();
    }

    @Override
    public void render(PlushBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        matrices.push();

        double squish = entity.squash;
        double lastSquish = squish * 3.0;
        float squash = (float)Math.pow(1.0 - 1.0 / (1.0 + MathHelper.clamp(tickDelta, lastSquish, squish)), 2.0);
        matrices.scale(1.0F, 1.0F - squash, 1.0F);
        matrices.translate(0.5, 0.0, 0.5);
        matrices.scale(1.0F + squash / 2.0F, 1.0F, 1.0F + squash / 2.0F);
        matrices.translate(-0.5, 0.0, -0.5);
        BlockState state = entity.getWorld().getBlockState(entity.getPos());
        BakedModel bakedModel = this.renderManager.getModel(state);
        ((BlockRenderManagerAccessor)this.renderManager).getModelRenderer().render(matrices.peek(), vertexConsumers.getBuffer(RenderLayers.getEntityBlockLayer(state, false)), state, bakedModel, 255.0F, 255.0F, 255.0F, light, overlay);
        matrices.pop();
    }
}
