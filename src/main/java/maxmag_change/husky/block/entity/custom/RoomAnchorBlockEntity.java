package maxmag_change.husky.block.entity.custom;

import com.google.common.base.MoreObjects;
import com.google.gson.Gson;
import maxmag_change.husky.HuskyLib;
import maxmag_change.husky.block.entity.ModBlockEntities;
import maxmag_change.husky.utill.Convertor;
import maxmag_change.husky.utill.logic.Door;
import maxmag_change.husky.utill.logic.Room;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import team.lodestar.lodestone.registry.common.particle.LodestoneParticleRegistry;
import team.lodestar.lodestone.systems.easing.Easing;
import team.lodestar.lodestone.systems.particle.builder.WorldParticleBuilder;
import team.lodestar.lodestone.systems.particle.data.GenericParticleData;
import team.lodestar.lodestone.systems.particle.data.color.ColorParticleData;
import team.lodestar.lodestone.systems.particle.data.spin.SpinParticleData;

import java.awt.*;
import java.util.List;

public class RoomAnchorBlockEntity extends BlockEntity {
    private DefaultedList<Door> doors;
    private Box roomSize;

    public RoomAnchorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ROOM_ANCHOR_BLOCK_ENTITY, pos, state);
        this.doors = DefaultedList.ofSize(27, Door.EMPTY);
        this.roomSize = new Box(Vec3d.ZERO,Vec3d.ZERO);
    }

    public void tick(World world, BlockPos pos, BlockState state) {

    }

    public void onUse(PlayerEntity user, World world, BlockPos pos, BlockState state) {

        DefaultedList<Door> notEmptyDoors = DefaultedList.of();

        for (int i = 0; i < this.getDoors().size(); i++) {
            Door door = this.doors.get(i);
            if (!door.getBlocks().isEmpty()){
                notEmptyDoors.add(i,door);
            }
        }

        user.sendMessage(Text.literal(encode(new Room(new Identifier("modId","roomPath"),this.getRoomSize(),this.getDoors()))));
    }

    public static String encode(Room container) {
        var gson = new Gson();
        return gson.toJson(container);
    }

    public Box getRoomSize() {
        return roomSize;
    }

    public DefaultedList<Door> getDoors() {
        return doors;
    }

    public int size() {
        return 27;
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        //Doors

        NbtList nbtList = nbt.getList("Doors", NbtElement.COMPOUND_TYPE);
        if (!nbtList.isEmpty()){
            //this.doors = DefaultedList.ofSize(this.size(), Door.EMPTY);
            for(int i = 0; i < nbtList.size(); ++i) {
                NbtCompound nbtCompound = nbtList.getCompound(i);
                int j = nbtCompound.getByte("Door") & 255;
                if (j >= 0 && j < this.doors.size()) {
                    Door door = new Door();
                    door.readNbt(nbtCompound);
                    this.doors.set(j, door);
                }
            }
        }

        //Room Size
        NbtCompound points = nbt.getCompound("Points");

        if (!points.isEmpty()){
            //this.roomSize = new Box(Vec3d.ZERO,Vec3d.ZERO);
            this.roomSize = new Box(
                    Convertor.StringToBlock(points.getString("Point1")),
                    Convertor.StringToBlock(points.getString("Point2"))
            );
        }

    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);

        //Doors
        DefaultedList<Door> doors = this.doors;

        NbtList nbtList = new NbtList();

        for(int i = 0; i < doors.size(); ++i) {
            Door door = (Door)doors.get(i);
            NbtCompound nbtCompound = new NbtCompound();
            nbtCompound.putByte("Door", (byte)i);
            door.writeNbt(nbtCompound);
            nbtList.add(nbtCompound);
        }

        nbt.put("Doors", nbtList);


        //Room Size
        Box roomSize = this.roomSize;

        NbtCompound nbtCompound = new NbtCompound();
        nbtCompound.putString("Point1", Convertor.BlockToString(BlockPos.ofFloored(new Vec3d(roomSize.minX,roomSize.minY,roomSize.minZ))));
        nbtCompound.putString("Point2", Convertor.BlockToString(BlockPos.ofFloored(new Vec3d(roomSize.maxX,roomSize.maxY,roomSize.maxZ))));

        nbt.put("Points", nbtCompound);

    }

    @Override
    public @Nullable Packet<ClientPlayPacketListener> toUpdatePacket() {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeBlockPos(this.getPos());
        buf.writeRegistryValue(Registries.BLOCK_ENTITY_TYPE,this.getType());
        buf.writeNbt(this.getCustomData());
        Packet<ClientPlayPacketListener> packet = new BlockEntityUpdateS2CPacket(buf);
        return packet;
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        NbtCompound nbt = new NbtCompound();
        writeNbt(nbt); // Same as in writeNbt
        return nbt;
    }
}