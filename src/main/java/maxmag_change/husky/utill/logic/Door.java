package maxmag_change.husky.utill.logic;

import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class Door {
    List<Vec3d> blocks;

    public static final Door EMPTY = new Door();

    public List<Vec3d> getBlocks() {
        return blocks;
    }
}
