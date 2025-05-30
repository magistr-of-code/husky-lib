package maxmag_change.husky.utill;

import com.google.common.base.MoreObjects;
import net.minecraft.util.math.BlockPos;

public class Convertor {
    public static BlockPos StringToBlock(String string){
        String[] parts = string.substring(9, string.length() - 1).split(", ");

        int x = Integer.parseInt(parts[0].substring(2));
        int y = Integer.parseInt(parts[1].substring(2));
        int z = Integer.parseInt(parts[2].substring(2));

        return new BlockPos(x,y,z);
    }

    public static String BlockToString(BlockPos pos) {
        return MoreObjects.toStringHelper("BlockPos").add("x", pos.getX()).add("y", pos.getY()).add("z", pos.getZ()).toString();
    }
}
