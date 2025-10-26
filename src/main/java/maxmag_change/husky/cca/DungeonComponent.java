package maxmag_change.husky.cca;

import dev.onyxstudios.cca.api.v3.component.Component;
import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import maxmag_change.husky.utill.logic.dungeon.Dungeon;
import maxmag_change.husky.utill.logic.dungeon.DungeonBox;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

import java.util.UUID;

public class DungeonComponent implements Component {
    public World world;
    public Int2ObjectMap<Dungeon> idToDungeon = new Int2ObjectLinkedOpenHashMap<>();

    public DungeonComponent(World world){
        this.world=world;
    }

    public void addDungeon(Dungeon dungeon){
        idToDungeon.put(idToDungeon.size()+1,dungeon);
    }

    @Override
    public void readFromNbt(NbtCompound nbtCompound) {
        Int2ObjectMap<Dungeon> int2ObjectMap = new Int2ObjectLinkedOpenHashMap<>();
        for (int i = 1; ; i++) {
            int id = nbtCompound.getInt("int" + i);
            if (id==0){
                break;
            }
            Dungeon dungeon = Dungeon.readFromNbt(nbtCompound,"key" + i);
            dungeon.id=id;
            idToDungeon.put(id,dungeon);
        }
    }

    @Override
    public void writeToNbt(NbtCompound nbtCompound) {
        final int[] values = {0};
        idToDungeon.forEach(((integer, dungeon) -> {
            nbtCompound.putInt("int" + values[0],integer);
            dungeon.writeToNbt(nbtCompound,"dungeon" + values[0]);
            values[0]++;
        }));
    }
}
