package maxmag_change.husky.utill.logic.room;

import maxmag_change.husky.utill.logic.door.Door;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;

public class LastRoom {
    public Box box;
    public Door door;
    public Identifier identifier;

    public LastRoom(Box box, Door door, Identifier identifier){
        this.box=box;
        this.door=door;
        this.identifier=identifier;
    }

    public static LastRoom readFromNbt(NbtCompound compound) {
        Box box1 = new Box(
                compound.getDouble("minX"),
                compound.getDouble("minY"),
                compound.getDouble("minZ"),
                compound.getDouble("maxX"),
                compound.getDouble("maxY"),
                compound.getDouble("maxZ")
        );

        return new LastRoom(box1,Door.readNbt(compound),Identifier.tryParse(compound.getString("identifier")));
    }

    public void writeToNbt(NbtCompound compound, String key) {
        NbtCompound lastRoomCompound = new NbtCompound();

        NbtCompound boxNBT = new NbtCompound();

        boxNBT.putDouble("minX",box.minX);
        boxNBT.putDouble("minY",box.minY);
        boxNBT.putDouble("minZ",box.minZ);
        boxNBT.putDouble("maxX",box.maxX);
        boxNBT.putDouble("maxY",box.maxY);
        boxNBT.putDouble("maxZ",box.maxZ);

        lastRoomCompound.put("box" ,boxNBT);

        door.writeNbt(compound);

        lastRoomCompound.putString("identifier",identifier.toString());
    }
}
