package maxmag_change.husky.mixin;


import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(BlockEntityRenderer.class)
public interface BlockEntity<T extends net.minecraft.block.entity.BlockEntity> {
    /**
     * @author yo56789
     * @reason sets view distance to number of chunks in render distance
     */
    @Overwrite
    default int getRenderDistance() {
        return MinecraftClient.getInstance().options.getViewDistance().getValue() * 16;
    }

    /**
     * @author yo56789
     * @reason makes it only factor x and z into calculation
     */
    @Overwrite
    default boolean isInRenderDistance(T blockEntity, Vec3d cameraPos) {
        return Vec3d.ofCenter(blockEntity.getPos()).isInRange(new Vec3d(cameraPos.getX(), blockEntity.getPos().getY(), cameraPos.getZ()), (double) this.getRenderDistance());
    }
}