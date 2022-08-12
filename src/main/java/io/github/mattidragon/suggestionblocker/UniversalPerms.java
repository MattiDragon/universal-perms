package io.github.mattidragon.suggestionblocker;

import com.google.common.collect.Iterables;
import com.mojang.brigadier.tree.CommandNode;
import io.github.mattidragon.suggestionblocker.mixin.CommandNodeAccess;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.Event;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class UniversalPerms implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger(UniversalPerms.class);

    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(
                    CommandManager.literal("suggestion-blocker")
                            .requires(Permissions.require("suggestionBlocker", 3))
                            .then(CommandManager.literal("reload")
                                    .requires(Permissions.require("suggestionBlocker.reload", 3))
                                    .executes((context -> {
                                        var success = Config.reload();
                                        if (success)
                                            context.getSource().sendFeedback(Text.literal("Suggestion Blocker reloaded successfully."), true);
                                        else
                                            context.getSource().sendError(Text.literal("Failed to reload, check logs."));
                                        return success ? 1 : 0;
                                    }))));
        });
        CommandRegistrationCallback.EVENT.addPhaseOrdering(Event.DEFAULT_PHASE, new Identifier("suggestion-blocker", "after"));
        CommandRegistrationCallback.EVENT.register(new Identifier("suggestion-blocker", "after"), (dispatcher, registryAccess, environment) -> {
            alterNode(dispatcher.getRoot(), new ArrayDeque<>(), new HashMap<>());
            LOGGER.info("Applied cursed permissions!");
        });
        Config.register();
    }

    private static void alterNode(CommandNode<ServerCommandSource> node, Deque<String> location, Map<CommandNode<ServerCommandSource>, String> visited) {
        var name = node.getName();
        if (!name.isEmpty())
            location.addLast(name);

        var nodeLocation = String.join(".", location);

        if (visited.containsKey(node)) {
            LOGGER.warn("Encountered same node a two different places, '{}' and '{}'. This shouldn't happen!", visited.get(node), nodeLocation);
            if (!name.isEmpty())
                location.removeLast();
            return;
        }

        var permission = createPermission("use", location);
        var requirement = node.getRequirement();
        ((CommandNodeAccess)node).setRequirement((ServerCommandSource source) -> Permissions.getPermissionValue(source, permission).orElseGet(() -> requirement.test(source)));

        node.getChildren().forEach(child -> alterNode(child, location, visited));

        if (!name.isEmpty())
            location.removeLast();
    }

    public static String createPermission(String type, Iterable<String> command) {
        return String.join(".", Iterables.concat(List.of("universal_perms", type), command));
    }
}
