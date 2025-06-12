package maxmag_change.husky.block.entity.custom;

import maxmag_change.husky.block.entity.HuskyBlockEntities;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.Registries;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlushBlockEntity extends BlockEntity {
    public PlushBlockEntity(BlockPos pos, BlockState state) {
        super(HuskyBlockEntities.PLUSH_BLOCK_ENTITY, pos, state);
    }

    public double squash;


    public void tick(World world, BlockPos pos, BlockState state,PlushBlockEntity spark) {
        if (spark.squash > 0.0) {
            spark.squash /= 3.0;
            if (spark.squash < 0.009999999776482582) {
                spark.squash = 0.0;
                if (world != null) {
                    world.updateListeners(pos, state,state, 2);
                }
            }
        }

    }

    public void squish(int squash) {
        this.squash += (double)squash;
        if (this.world != null) {
            this.world.updateListeners(this.getPos(), this.getCachedState(),this.getCachedState(), 2);
        }

        this.markDirty();
    }

    protected void writeNbt(@NotNull NbtCompound nbt) {
        nbt.putDouble("squash", this.squash);
    }

    public void readNbt(@NotNull NbtCompound nbt) {
        this.squash = nbt.getDouble("squash");
    }

    @Override
    public @Nullable Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return this.createNbt();
    }
}
