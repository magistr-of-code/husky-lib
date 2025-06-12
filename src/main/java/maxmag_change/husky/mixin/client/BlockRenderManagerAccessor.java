package maxmag_change.husky.mixin.client;

import net.minecraft.client.render.block.BlockModelRenderer;
import net.minecraft.client.render.block.BlockRenderManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({BlockRenderManager.class})
public interface BlockRenderManagerAccessor {
    @Accessor("blockModelRenderer")
    BlockModelRenderer getModelRenderer();
}
