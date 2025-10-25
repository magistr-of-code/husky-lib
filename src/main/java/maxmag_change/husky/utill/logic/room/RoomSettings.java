package maxmag_change.husky.utill.logic.room;

import maxmag_change.husky.HuskyLib;

public class RoomSettings implements Cloneable{
    boolean mergeDoors = false;
    String group = "group";
    CustomIdentifier deadEnd;
    double weight = 1.0;

    public RoomSettings(boolean mergeDoors, String group, CustomIdentifier deadEnd, double weight){
        this.mergeDoors=mergeDoors;
        this.group = group;
        this.deadEnd = deadEnd;
        this.weight = weight;
    }

    public RoomSettings(boolean mergeDoors, String group){
        this(mergeDoors,group,new CustomIdentifier(HuskyLib.MOD_ID,"empty"),1.0);
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

    public CustomIdentifier getDeadEnd() {
        return deadEnd;
    }

    public void setDeadEnd(CustomIdentifier deadEnd) {
        this.deadEnd = deadEnd;
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

    @Override
    public String toString() {
        return "RoomSettings{" +
                "mergeDoors=" + mergeDoors +
                ", group='" + group  +
                ", deadEnd=" + deadEnd.toString() +
                ", weight=" + weight +
                '}';
    }
}
