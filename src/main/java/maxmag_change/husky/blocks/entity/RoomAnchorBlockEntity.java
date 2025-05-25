package maxmag_change.husky.blocks.entity;

import maxmag_change.husky.utill.logic.Door;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class RoomAnchorBlockEntity extends BlockEntity {
    private DefaultedList<Door> inventory;
    private Box roomSize;

    public RoomAnchorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.PORTAL_CORE_BLOCK_ENTITY, pos, state);
    }

    public void tick(World world, BlockPos pos, BlockState state) {

    }

    public int size() {
        return 27;
    }

    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.inventory = DefaultedList.ofSize(this.size(), Door.EMPTY);
        Inventories.readNbt(nbt, this.inventory);

    }

    protected void writeNbt(NbtCompound nbt) {
        nbt.put

        super.writeNbt(nbt);

    }
}