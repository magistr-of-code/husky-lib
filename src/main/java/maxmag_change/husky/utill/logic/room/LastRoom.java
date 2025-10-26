package maxmag_change.husky.utill.logic.room;

import maxmag_change.husky.utill.logic.door.Door;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public class LastRoom {
    public Box box;
    public Door door;
    public BlockPos pos;
    public Identifier identifier;

    public LastRoom(Box box, Door door, BlockPos pos, Identifier identifier){
        this.box=box;
        this.door=door;
        this.pos=pos;
        this.identifier=identifier;
    }

    public static LastRoom readFromNbt(NbtCompound compound) {
        NbtCompound box = compound.getCompound("box");

        Box box1 = new Box(
                box.getDouble("minX"),
                box.getDouble("minY"),
                box.getDouble("minZ"),
                box.getDouble("maxX"),
                box.getDouble("maxY"),
                box.getDouble("maxZ")
        );

        NbtCompound posC = compound.getCompound("pos");

        BlockPos pos = new BlockPos(
                posC.getInt("x"),
                posC.getInt("y"),
                posC.getInt("z")
        );

        return new LastRoom(box1,Door.readNbt(compound),pos,Identifier.tryParse(compound.getString("identifier")));
    }

    public void writeToNbt(NbtCompound compound, String key) {

        NbtCompound boxNBT = new NbtCompound();

        boxNBT.putDouble("minX",box.minX);
        boxNBT.putDouble("minY",box.minY);
        boxNBT.putDouble("minZ",box.minZ);
        boxNBT.putDouble("maxX",box.maxX);
        boxNBT.putDouble("maxY",box.maxY);
        boxNBT.putDouble("maxZ",box.maxZ);

        compound.put("box" ,boxNBT);

        NbtCompound posC = new NbtCompound();

        posC.putInt("x",pos.getX());
        posC.putInt("y",pos.getY());
        posC.putInt("z",pos.getZ());

        compound.put("pos",posC);

        door.writeNbt(compound);

        compound.putString("identifier",identifier.toString());
    }
}
