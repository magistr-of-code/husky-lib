package maxmag_change.husky.utill.logic.room;

public class RoomSettings implements Cloneable{
    boolean mergeDoors = false;
    String group = "group";
    double weight = 1.0;

    public RoomSettings(boolean mergeDoors, String group, double weight){
        this.mergeDoors=mergeDoors;
        this.group = group;
        this.weight = weight;
    }

    public RoomSettings(boolean mergeDoors, String group){
        this(mergeDoors,group,1.0);
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public boolean mergeDoors() {
        return mergeDoors;
    }

    public void setMergedDoors(boolean mergeDoors) {
        this.mergeDoors = mergeDoors;
    }

    @Override
    public RoomSettings clone() {
        try {
            return (RoomSettings) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
