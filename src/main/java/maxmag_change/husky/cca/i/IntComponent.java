package maxmag_change.husky.cca.i;


import dev.onyxstudios.cca.api.v3.component.Component;
import net.minecraft.server.network.ServerPlayerEntity;

public interface IntComponent extends Component {
    int getValue();
    void setValue(int v);
    void syncValue(ServerPlayerEntity player, int v);
}