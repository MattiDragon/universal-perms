package io.github.mattidragon.universalperms;

import com.mojang.brigadier.tree.CommandNode;
import io.github.mattidragon.universalperms.mixin.CommandNodeAccess;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@SuppressWarnings("UnstableApiUsage")
public class UniversalPerms implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger(UniversalPerms.class);

    @Override
    public void onInitialize() {
        var phaseId = Identifier.fromNamespaceAndPath("universal_perms", "after");
        CommandRegistrationCallback.EVENT.addPhaseOrdering(Event.DEFAULT_PHASE, phaseId);
        CommandRegistrationCallback.EVENT.register(phaseId, (dispatcher, _, _) -> {
            alterNode(dispatcher.getRoot(), new ArrayDeque<>(), new HashMap<>());
            LOGGER.info("Applied cursed permissions!");
        });
        // Use every misc permission once, so that luckperms knows about them for the editor
        ServerLifecycleEvents.SERVER_STARTED.register(server -> ModPermissions.usePermissions(server.createCommandSourceStack()));
    }

    private static void alterNode(CommandNode<CommandSourceStack> node, Deque<String> location, Map<CommandNode<CommandSourceStack>, String> visited) {
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
        ((CommandNodeAccess)node).setRequirement((CommandSourceStack source) -> (source.getEntity() == null ? TriState.DEFAULT : source.checkPermission(permission)).orElseGet(() -> requirement.test(source)));

        node.getChildren().forEach(child -> alterNode(child, location, visited));

        if (!name.isEmpty())
            location.removeLast();
    }

    public static Identifier createPermission(String type, Iterable<String> command) {
        return Identifier.fromNamespaceAndPath("universal_perms", type + "/" + StreamSupport.stream(command.spliterator(), false)
                .map(UniversalPerms::cleanForId)
                .collect(Collectors.joining(".")));
    }

    private static String cleanForId(String s) {
        return s.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9_-]", "_");
    }
}
