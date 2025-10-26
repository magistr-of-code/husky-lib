package maxmag_change.husky.cca.i;

import dev.onyxstudios.cca.api.v3.component.Component;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;

public interface ItemArrayComponent extends Component {
    void addItem(ItemStack stack);
    void putItem(ItemStack stack, int slot);
    void copy(PlayerInventory inventory);
}
