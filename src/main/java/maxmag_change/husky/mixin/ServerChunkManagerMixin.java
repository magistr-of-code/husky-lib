package maxmag_change.husky.mixin;

import com.mojang.datafixers.util.Either;
import maxmag_change.husky.HuskyLib;
import maxmag_change.husky.cca.HuskyWorldComponents;
import maxmag_change.husky.utill.logic.room.LastRoom;
import net.minecraft.server.world.ChunkHolder;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkStatus;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.CompletableFuture;

@Mixin(ServerChunkManager.class)
public class ServerChunkManagerMixin {
    @Shadow @Final
    ServerWorld world;

    @Inject(method = "getChunkFuture(IILnet/minecraft/world/chunk/ChunkStatus;Z)Ljava/util/concurrent/CompletableFuture;",at=@At("HEAD"))
    private void getChunkFuture(int chunkX, int chunkZ, ChunkStatus leastStatus, boolean create, CallbackInfoReturnable<CompletableFuture<Either<Chunk, ChunkHolder.Unloaded>>> cir) {
        final Box chunkBox = new Box(chunkX*16-16,world.getBottomY(),chunkZ*16-16,chunkX*16,world.getTopY(),chunkZ*16);
        HuskyWorldComponents.DUNGEONS.get(world).idToDungeon.forEach((integer, dungeon) -> {
            if (dungeon.bbh.box.intersects(chunkBox)){
                for (LastRoom lastRoom : dungeon.lastRooms) {
                    if (lastRoom.box.intersects(chunkBox)){
                        HuskyLib.LOGGER.error("hai");
                        dungeon.generateBranch(world,lastRoom);
                        dungeon.lastRooms.remove(lastRoom);
                    }
                }
            }
        });
    }
}
