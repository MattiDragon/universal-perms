package io.github.mattidragon.universalperms;

import com.google.common.collect.Iterables;
import com.mojang.brigadier.tree.CommandNode;
import io.github.mattidragon.universalperms.mixin.CommandNodeAccess;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.Event;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class UniversalPerms implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger(UniversalPerms.class);

    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.addPhaseOrdering(Event.DEFAULT_PHASE, new Identifier("universal_perms", "after"));
        CommandRegistrationCallback.EVENT.register(new Identifier("universal_perms", "after"), (dispatcher, registryAccess, environment) -> {
            alterNode(dispatcher.getRoot(), new ArrayDeque<>(), new HashMap<>());
            LOGGER.info("Applied cursed permissions!");
        });
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
