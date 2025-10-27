package maxmag_change.husky.utill.logic.dungeon;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;

import java.util.List;
import java.util.Objects;

public class BBH extends DungeonBox {

    public BBH(List<Box> boxes){
        this.boxes=boxes;
        //this.recalculate();
    }

    public void recalculate(){
        if (box==null){
            box=boxes.get(0);
        }
        boxes.forEach(childBox->{
            box.union(childBox);
        });
        BBH.split(this);
    }

    public static BBH readFromNbt(NbtCompound compound) {

        List<Box> boxList = DefaultedList.of();

        for (int i = 0;; i++) {
            NbtCompound box = compound.getCompound("box" + i);
            if (Objects.equals(box, new NbtCompound())){
                break;
            }

            boxList.add(new Box(
                    box.getDouble("minX"),
                    box.getDouble("minY"),
                    box.getDouble("minZ"),
                    box.getDouble("maxX"),
                    box.getDouble("maxY"),
                    box.getDouble("maxZ")
            ));
        }

        BBH bbh = new BBH(boxList);

        NbtCompound box = compound.getCompound("box");
        if (!Objects.equals(box, new NbtCompound())) {

            bbh.box=new Box(
                    box.getDouble("minX"),
                    box.getDouble("minY"),
                    box.getDouble("minZ"),
                    box.getDouble("maxX"),
                    box.getDouble("maxY"),
                    box.getDouble("maxZ")
            );
        }

        return bbh;
    }

    public NbtCompound writeToNbt(NbtCompound compound){
        List<Box> boxList=boxes;

        if (boxList==null){
            boxList=DefaultedList.of();
        }

        if (childA!=null) {
            childA.addBoxes(boxList);
        }
        if (childB!=null) {
            childB.addBoxes(boxList);
        }

        for (int i = 0; i < boxList.size(); i++) {
            Box box = boxList.get(i);
            NbtCompound boxNBT = new NbtCompound();

            boxNBT.putDouble("minX",box.minX);
            boxNBT.putDouble("minY",box.minY);
            boxNBT.putDouble("minZ",box.minZ);
            boxNBT.putDouble("maxX",box.maxX);
            boxNBT.putDouble("maxY",box.maxY);
            boxNBT.putDouble("maxZ",box.maxZ);

            compound.put("box" + i,boxNBT);
        }

        if (box!=null) {
            NbtCompound boxNBT = new NbtCompound();

            boxNBT.putDouble("minX",box.minX);
            boxNBT.putDouble("minY",box.minY);
            boxNBT.putDouble("minZ",box.minZ);
            boxNBT.putDouble("maxX",box.maxX);
            boxNBT.putDouble("maxY",box.maxY);
            boxNBT.putDouble("maxZ",box.maxZ);

            compound.put("box",boxNBT);
        }

        return compound;
    }
}
