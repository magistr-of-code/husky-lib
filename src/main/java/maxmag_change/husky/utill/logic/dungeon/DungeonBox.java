package maxmag_change.husky.utill.logic.dungeon;

import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Box;

import java.util.List;

public class DungeonBox {
    public Box box;
    public DungeonBox childA,childB;
    public List<Box> boxes = DefaultedList.of();

    public static void split(DungeonBox parent){
        if (parent.boxes.size()<=1) return;

        parent.childA = new DungeonBox();
        parent.childB = new DungeonBox();

        for (int i = 0; i < parent.boxes.size(); i++) {
            Box box1 = parent.boxes.get(i);
            boolean inA = box1.getCenter().getX() < parent.box.getCenter().getX();
            DungeonBox child = inA ? parent.childA : parent.childB;
            child.boxes.add(box1);
            child.box.union(box1);
        }

        parent.boxes = null;

        split(parent.childA);
        split(parent.childB);
    }

    public boolean checkInteractions(Box box){
        int i = 0;

        if (childA!=null) {
            if (box.intersects(childA.box)){
                if(childA.checkInteractions(box)) i++;
            }
        }
        if (childB!=null) {
            if (box.intersects(childB.box)){
                if(childB.checkInteractions(box)) i++;
            }
        }

        return i>0;
    }

    public void addBoxes(List<Box> boxes){
        if (boxes!=null){
            boxes.addAll(this.boxes);
        }

        childA.addBoxes(boxes);
        childB.addBoxes(boxes);
    }
}
