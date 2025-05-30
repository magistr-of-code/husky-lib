package maxmag_change.husky.block.entity.renderer;

import maxmag_change.husky.HuskyLib;
import maxmag_change.husky.block.entity.custom.RoomAnchorBlockEntity;
import maxmag_change.husky.utill.logic.Door;
import net.minecraft.block.enums.StructureBlockMode;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.block.entity.StructureBlockBlockEntityRenderer;
import net.minecraft.client.render.debug.DebugRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public class RoomAnchorEntityRenderer implements BlockEntityRenderer<RoomAnchorBlockEntity> {
    public RoomAnchorEntityRenderer(BlockEntityRendererFactory.Context context) {
    }


    @Override
    public void render(RoomAnchorBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getLines());
        for(Door door : entity.getDoors()) {
            if (!door.getBlocks().isEmpty()) {
                for (BlockPos block : door.getBlocks()){
                    WorldRenderer.drawBox(matrices, vertexConsumer, block.getX(),block.getY(),block.getZ(),block.getX()+1,block.getY()+1,block.getZ()+1, 0.9F, 0.9F, 0.9F, 1.0F, 0.5F, 0.5F, 0.5F);
                }
            }
        }
        Box box = entity.getRoomSize();
        WorldRenderer.drawBox(matrices, vertexConsumer, box.minX,box.minY,box.minZ,box.maxX+1,box.maxY+1,box.maxZ+1, 0.9F, 0.9F, 0.9F, 1.0F, 0.5F, 0.5F, 0.5F);
    }
}
