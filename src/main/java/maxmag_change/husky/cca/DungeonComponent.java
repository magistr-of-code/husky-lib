package maxmag_change.husky.cca;

import dev.onyxstudios.cca.api.v3.component.Component;
import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import maxmag_change.husky.utill.logic.dungeon.Dungeon;
import maxmag_change.husky.utill.logic.dungeon.DungeonBox;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.PersistentState;
import net.minecraft.world.World;

import java.util.UUID;

public class DungeonComponent extends PersistentState implements Component {
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
        for (int i = 1;; i++) {
            int id = nbtCompound.getInt("int" + i);
            if (id==0){
                break;
            }
            Dungeon dungeon = Dungeon.readFromNbt(nbtCompound.getCompound("dungeon" + i));
            dungeon.id=id;
            idToDungeon.put(id,dungeon);
        }
    }

    @Override
    public void writeToNbt(NbtCompound nbtCompound) {
        final int[] values = {0};
        idToDungeon.forEach(((integer, dungeon) -> {
            nbtCompound.putInt("int" + values[0],integer);
            nbtCompound.put("dungeon" + values[0],dungeon.writeToNbt(new NbtCompound()));
            values[0]++;
        }));
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbtCompound) {
        writeToNbt(nbtCompound);
        return nbtCompound;
    }
}
