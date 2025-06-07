package maxmag_change.husky.registries;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import maxmag_change.husky.HuskyLib;
import maxmag_change.husky.utill.logic.DeserializedRoom;
import maxmag_change.husky.utill.logic.Door;
import maxmag_change.husky.utill.logic.Room;
import net.minecraft.resource.ResourceManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.shape.VoxelShape;
import org.apache.logging.log4j.util.BiConsumer;

import javax.swing.*;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoomRegistry {
    static Map<Identifier, Room> registrations = new HashMap<>();

    public static void register(Identifier identifier, Room room) {
        registrations.put(identifier, room);
    }

    public static void register(Identifier identifier, Box roomSize, List<Door> doors) {
        register(identifier,new Room(identifier,roomSize,doors));
    }

    public static Room getType(Identifier identifier) {
        return registrations.get(identifier);
    }

    public static List<Pair<BlockRotation,Room>> getWithMatchingDoor(Door door){
        List<Pair<BlockRotation,Room>> rooms = new java.util.ArrayList<>(List.of());
        Map<Identifier, Room> roomMap = registrations;
        roomMap.forEach(((identifier, room) -> {
            room.hasMatchingDoors(rooms,door);
        }));

        return rooms;
    }

//    static {
//        register(new Identifier(HuskyLib.MOD_ID,"vanilla/corridor1"),new Room(new Identifier(HuskyLib.MOD_ID,"vanilla/corridor1")));
//    }

    public static void loadRooms(ResourceManager resourceManager) {
        var gson = new Gson();
        Map<Identifier, Room> containers = new HashMap();
        // Reading all attribute files
        for (var entry : resourceManager.findResources("rooms", fileName -> fileName.getPath().endsWith(".json")).entrySet()) {
            var identifier = entry.getKey();
            var resource = entry.getValue();
            try {
                HuskyLib.LOGGER.debug("Checking resource: " + identifier);
                JsonReader reader = new JsonReader(new InputStreamReader(resource.getInputStream()));
                Room container = decode(reader).toRoom();
                var id = identifier
                        .toString().replace("rooms/", "");
                id = id.substring(0, id.lastIndexOf('.'));
                containers.put(new Identifier(id), container);
            } catch (Exception e) {
                System.err.println("Failed to parse: " + identifier);
                e.printStackTrace();
            }
        }
        RoomRegistry.registrations = containers;
    }

    private static Type attributesContainerFileFormat = new TypeToken<DeserializedRoom>() {}.getType();

    public static DeserializedRoom decode(JsonReader json) {
        var gson = new Gson();
        DeserializedRoom container = gson.fromJson(json, attributesContainerFileFormat);
        return container;
    }
}
