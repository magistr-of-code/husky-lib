package maxmag_change.husky.utill.logic.dead_end;

import maxmag_change.husky.utill.logic.door.Door;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public abstract class DeadEnd {

    public abstract void generate(World world, Door door, BlockPos pos);
}
