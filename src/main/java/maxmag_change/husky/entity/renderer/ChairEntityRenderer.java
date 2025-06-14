package maxmag_change.husky.entity.renderer;

import maxmag_change.husky.entity.ChairEntity;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;

public class ChairEntityRenderer extends EntityRenderer<ChairEntity> {
    public ChairEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Override
    public Identifier getTexture(ChairEntity entity) {
        return null;
    }
}