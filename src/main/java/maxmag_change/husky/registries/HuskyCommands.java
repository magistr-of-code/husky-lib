package maxmag_change.husky.registries;

import com.google.gson.Gson;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import maxmag_change.husky.utill.logic.door.Door;
import maxmag_change.husky.utill.logic.room.DeserializedRoom;
import maxmag_change.husky.utill.logic.room.Room;
import maxmag_change.husky.utill.logic.room.RoomSettings;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.*;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;

import java.util.List;
import java.util.Objects;

public class HuskyCommands {
    public static void registerModCommands() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            //Generate rooms
            dispatcher.register(CommandManager.literal("rooms")
                    .then(CommandManager.literal("generate")
                            .then(CommandManager.argument("name", IdentifierArgumentType.identifier()).then(CommandManager.argument("forward", IntegerArgumentType.integer()).executes(HuskyCommands::generate)))));
            //Get group of room
            dispatcher.register(CommandManager.literal("rooms")
                    .then(CommandManager.literal("get")
                            .then(CommandManager.literal("group").then(CommandManager.argument("name", IdentifierArgumentType.identifier()).executes(HuskyCommands::getGroup)))));
            //Get json from room
            dispatcher.register(CommandManager.literal("rooms")
                    .then(CommandManager.literal("get")
                            .then(CommandManager.literal("json").then(CommandManager.argument("name", IdentifierArgumentType.identifier()).executes(HuskyCommands::getJson)))));
            //Get room from group
            dispatcher.register(CommandManager.literal("rooms")
                    .then(CommandManager.literal("get")
                            .then(CommandManager.literal("from").then(CommandManager.literal("group").then(CommandManager.argument("name", StringArgumentType.string()).executes(HuskyCommands::getByGroup))))));
        });
    }

    private static int getGroup(CommandContext<ServerCommandSource> context) {
        Identifier name = IdentifierArgumentType.getIdentifier(context,"name");
        Room room = RoomRegistry.getType(name);
        if (room!=null){
            context.getSource().sendFeedback(()->Text.literal("Found group: ").append(Text.literal(room.getSettings().getGroup()).styled(style -> style.withUnderline(true).withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD,room.getSettings().getGroup())).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,Text.of("Click to copy"))))),false);
            return 1;
        } else {
            context.getSource().sendError(Text.literal("Couldn't find room ").append(Text.literal(name.toString()).styled(style -> style.withUnderline(true).withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD,name.toString())).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,Text.of("Click to copy"))))));
            return 0;
        }
    }

    private static int getJson(CommandContext<ServerCommandSource> context) {
        Identifier name = IdentifierArgumentType.getIdentifier(context,"name");
        Room room = RoomRegistry.getType(name);

        if (room!=null){
            DefaultedList<Door> notEmptyDoors = DefaultedList.of();

            for (int i = 0; i < room.getDoors().size(); i++) {
                Door door = room.getDoors().get(i);
                if (!door.getBlocks().isEmpty()){
                    notEmptyDoors.add(i,door);
                }
            }

            DeserializedRoom deserializedRoom = new DeserializedRoom(room.getStructureName(),room.getRoomSize(),notEmptyDoors,room.getSettings());

            context.getSource().sendFeedback(()->Text.literal("Generated json contents (Click to copy)").styled(style -> style.withUnderline(true).withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD,new Gson().toJson(deserializedRoom))).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,Text.of("Click to copy")))),false);
            return 1;
        } else {
            context.getSource().sendError(Text.literal("Couldn't find room ").append(Text.literal(name.toString()).styled(style -> style.withUnderline(true).withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD,name.toString())).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,Text.of("Click to copy"))))));
            return 0;
        }
    }

    private static int getByGroup(CommandContext<ServerCommandSource> context) {
        List<Room> rooms = DefaultedList.of();
        String group = StringArgumentType.getString(context, "name");
        RoomRegistry.registrations.forEach(((identifier, room) -> {
            if (Objects.equals(room.getSettings().getGroup(), group)){
                rooms.add(room);
            }
        }));

        if (!rooms.isEmpty()){
            final MutableText[] text = {Text.literal("Found rooms ")};
            for (int i = 0; i < rooms.size(); i++) {
                Room room = rooms.get(i);
                text[0] = text[0].append(" " + (i + 1) +":").append(Text.literal(room.getStructureName().toString()).styled(style -> style.withUnderline(true).withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD,room.getStructureName().toString())).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,Text.of("Click to copy")))));
            }
            context.getSource().sendFeedback(()->text[0],false);
            return 1;
        } else {
            context.getSource().sendError(Text.literal("Couldn't find rooms with group ").append(Text.literal(group).styled(style -> style.withUnderline(true).withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD,group)).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,Text.of("Click to copy"))))));
            return 0;
        }
    }

    private static int generate(CommandContext<ServerCommandSource> context) {
        Identifier name = IdentifierArgumentType.getIdentifier(context,"name");

        int forward = IntegerArgumentType.getInteger(context, "forward");

        Room room = RoomRegistry.getType(name);
        if (room!=null) {
            Room.protectedGenerate(room,context.getSource().getWorld(), BlockPos.ofFloored(context.getSource().getPosition()), BlockRotation.NONE, forward);
            context.getSource().sendFeedback(()-> Text.literal("Generated Room"),false);
            return 1;
        } else {
            context.getSource().sendError(Text.literal("Couldn't find room ").append(Text.literal(name.toString()).styled(style -> style.withUnderline(true).withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD,name.toString())).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,Text.of("Click to copy"))))));
            return 0;
        }
    }
}
