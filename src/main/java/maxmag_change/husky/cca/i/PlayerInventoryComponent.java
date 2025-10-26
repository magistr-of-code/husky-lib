package maxmag_change.husky.cca.i;

import dev.onyxstudios.cca.api.v3.component.Component;
import net.minecraft.entity.player.PlayerInventory;

public interface PlayerInventoryComponent extends Component {
    void copy(PlayerInventory inventory);
    void putInventory(PlayerInventory inventory);
}

