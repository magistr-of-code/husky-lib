package maxmag_change.husky.registries;

import com.google.gson.Gson;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import maxmag_change.husky.HuskyLib;
import maxmag_change.husky.cca.HuskyWorldComponents;
import maxmag_change.husky.utill.logic.door.Door;
import maxmag_change.husky.utill.logic.dungeon.BBH;
import maxmag_change.husky.utill.logic.dungeon.Dungeon;
import maxmag_change.husky.utill.logic.dungeon.DungeonSettings;
import maxmag_change.husky.utill.logic.room.*;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.*;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

import java.util.List;
import java.util.Objects;

public class HuskyCommands {
    public static void registerModCommands() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            //Reload Rooms
            dispatcher.register(CommandManager.literal("rooms")
                    .requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(2))
                    .then(CommandManager.literal("reload")
                            .executes(commandContext -> {RoomRegistry.loadRooms(commandContext.getSource().getServer().getResourceManager()); return 1;})));

            //Generate rooms
            dispatcher.register(CommandManager.literal("rooms")
                    .requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(2))
                    .then(CommandManager.literal("generate")
                            .then(CommandManager.argument("name", IdentifierArgumentType.identifier()).then(CommandManager.argument("forward", IntegerArgumentType.integer()).executes(HuskyCommands::generate)))));
            //Get group of room
            dispatcher.register(CommandManager.literal("rooms")
                    .requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(2))
                    .then(CommandManager.literal("get")
                            .then(CommandManager.literal("group").then(CommandManager.argument("name", IdentifierArgumentType.identifier()).executes(HuskyCommands::getGroup)))));
            //Get json from room
            dispatcher.register(CommandManager.literal("rooms")
                    .requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(2))
                    .then(CommandManager.literal("get")
                            .then(CommandManager.literal("json").then(CommandManager.argument("name", IdentifierArgumentType.identifier()).executes(HuskyCommands::getJson)))));
            //Get room from group
            dispatcher.register(CommandManager.literal("rooms")
                    .requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(2))
                    .then(CommandManager.literal("get")
                            .then(CommandManager.literal("from").then(CommandManager.literal("group").then(CommandManager.argument("name", StringArgumentType.string()).executes(HuskyCommands::getByGroup))))));
            //Dungeon creation
            dispatcher.register(CommandManager.literal("dungeon")
                    .requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(2))
                    .then(CommandManager.literal("create")
                            .then(CommandManager.argument("startingRoom", IdentifierArgumentType.identifier())
                                    .then(CommandManager.argument("group", StringArgumentType.string())
                                            .then(CommandManager.argument("maxRooms", IntegerArgumentType.integer())
                                                    .executes(HuskyCommands::createDungeon))))));
            //Dungeon updates
            dispatcher.register(CommandManager.literal("dungeon")
                    .requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(2))
                    .then(CommandManager.literal("update")
                            .executes(HuskyCommands::updateDungeons)));
            dispatcher.register(CommandManager.literal("dungeon")
                    .requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(2))
                    .then(CommandManager.literal("get")
                            .then(CommandManager.literal("all")
                                    .executes(HuskyCommands::getDungeons))));
            dispatcher.register(CommandManager.literal("dungeon")
                    .requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(2))
                    .then(CommandManager.literal("get")
                            .then(CommandManager.argument("id", IntegerArgumentType.integer())
                                    .executes(HuskyCommands::getDungeon))));
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

            DeserializedRoom deserializedRoom = new DeserializedRoom(new CustomIdentifier(room.getStructureName().getNamespace(),room.getStructureName().getPath()), new RoomBox(room.getRoomSize()),notEmptyDoors,room.getSettings());

            context.getSource().sendFeedback(()->Text.literal("Generated json contents (Click to copy)").styled(style -> style.withUnderline(true).withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD,new Gson().toJson(deserializedRoom))).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,Text.of("Click to copy")))),false);
            return 1;
        } else {
            context.getSource().sendError(Text.literal("Couldn't find room ").append(Text.literal(name.toString()).styled(style -> style.withUnderline(true).withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD,name.toString())).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,Text.of("Click to copy"))))));
            return 0;
        }
    }

    private static int getByGroup(CommandContext<ServerCommandSource> context) {
        List<Identifier> rooms = DefaultedList.of();
        String group = StringArgumentType.getString(context, "name");
        RoomRegistry.registrations.forEach(((identifier, room) -> {
            if (Objects.equals(room.getSettings().getGroup(), group)){
                rooms.add(identifier);
            }
        }));

        if (!rooms.isEmpty()){
            final MutableText[] text = {Text.literal("Found rooms ")};
            for (int i = 0; i < rooms.size(); i++) {
                Identifier room = rooms.get(i);
                text[0] = text[0].append(" " + (i + 1) +":").append(Text.literal(room.toString()).styled(style -> style.withUnderline(true).withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD,room.toString())).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,Text.of("Click to copy")))));
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
            BBH bbh = new BBH(DefaultedList.of());
            Room.protectedGenerate(room,context.getSource().getWorld(),bbh, BlockPos.ofFloored(context.getSource().getPosition()), BlockRotation.NONE, forward);

            if (forward==1){
                context.getSource().sendFeedback(()-> Text.literal("Generated Room"),true);
            } else {
                context.getSource().sendFeedback(()-> Text.literal("Generated " + bbh.boxes.size() + " Rooms"),true);
            }
            return 1;
        } else {
            context.getSource().sendError(Text.literal("Couldn't find room ").append(Text.literal(name.toString()).styled(style -> style.withUnderline(true).withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD,name.toString())).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,Text.of("Click to copy"))))));
            return 0;
        }
    }

    private static int createDungeon(CommandContext<ServerCommandSource> context) {
        Identifier startingRoom = IdentifierArgumentType.getIdentifier(context,"startingRoom");
        String group = StringArgumentType.getString(context,"group");

        int maxRooms = IntegerArgumentType.getInteger(context, "maxRooms");

        Room room = RoomRegistry.getType(startingRoom);
        if (room!=null) {
            Dungeon dungeon = new Dungeon(new CustomIdentifier(startingRoom.getNamespace(),startingRoom.getPath()),group,maxRooms);

            dungeon.createDungeon(context.getSource().getWorld(),BlockPos.ofFloored(context.getSource().getPosition()));
            return 1;
        } else {
            context.getSource().sendError(Text.literal("Couldn't find room ").append(Text.literal(startingRoom.toString()).styled(style -> style.withUnderline(true).withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD,startingRoom.toString())).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,Text.of("Click to copy"))))));
            return 0;
        }
    }

    private static int updateDungeons(CommandContext<ServerCommandSource> context){

        ServerWorld world = context.getSource().getWorld();

        HuskyWorldComponents.DUNGEONS.get(world).idToDungeon.forEach((integer, dungeon) -> {
            if (dungeon.lastRooms.isEmpty() || dungeon.rooms>dungeon.settings.maxRooms){
                HuskyWorldComponents.DUNGEONS.get(world).idToDungeon.remove(dungeon.id,dungeon);
            } else {
                List<LastRoom> roomList = List.copyOf(dungeon.lastRooms);
                roomList.forEach(lastRoom -> {
                    dungeon.generateBranch(world,lastRoom);
                    dungeon.lastRooms.remove(lastRoom);
                });
            }
        });

        return 1;
    }

    private static int getDungeons(CommandContext<ServerCommandSource> context){
        ServerWorld world = context.getSource().getWorld();

        final MutableText[] text = {Text.literal("Found " + HuskyWorldComponents.DUNGEONS.get(world).idToDungeon.size() + " dungeons:")};

        HuskyWorldComponents.DUNGEONS.get(world).idToDungeon.forEach((integer, dungeon) -> {
            text[0] = text[0].append(" " + (integer) +":").styled(style -> style.withUnderline(true).withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/dungeon get " + integer)).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,Text.of("Click to know more"))));
        });

        context.getSource().sendFeedback(()->text[0],false);

        return 1;
    }

    private static int getDungeon(CommandContext<ServerCommandSource> context){
        ServerWorld world = context.getSource().getWorld();

        int id = context.getArgument("id", Integer.class);

        Dungeon dungeon = HuskyWorldComponents.DUNGEONS.get(world).idToDungeon.get(id);

        if (dungeon!=null){
            MutableText feedback = Text.literal("Found dungeon with id " + id);
            //feedback = feedback.append(Text.literal("BBH: " + dungeon.bbh.box.toString()));
            MutableText text = Text.literal("\n Last Rooms: ");
            List<LastRoom> lastRooms = dungeon.lastRooms;
            for (int i = 0; i < lastRooms.size(); i++) {
                LastRoom lastRoom = lastRooms.get(i);
                text = text.append(Text.literal(lastRoom.toString()));
            }
            feedback = feedback.append(text);
            feedback = feedback.append(Text.literal("\n Settings: " + dungeon.settings.toString()));
            feedback = feedback.append(Text.literal("\n Rooms: " + dungeon.rooms));
            context.getSource().sendMessage(feedback);
            MutableText finalFeedback = feedback;
            context.getSource().sendFeedback(()-> finalFeedback,false);
            return 1;
        } else {
            context.getSource().sendError(Text.literal("Couldn't find a dungeon with id " + id));
            return 0;
        }
    }
}
