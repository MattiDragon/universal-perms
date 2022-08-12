package io.github.mattidragon.suggestionblocker.mixin;

import com.google.common.collect.Iterables;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.brigadier.tree.CommandNode;
import io.github.mattidragon.suggestionblocker.UniversalPerms;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.command.CommandSource;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;

@Mixin(CommandManager.class)
public abstract class CommandManagerMixin {
    @Unique
    private final Deque<String> suggestionBlocker$stack = new ArrayDeque<>();
    @Unique
    private String suggestionBlocker$current = null;

    @ModifyVariable(method = "makeTreeForSource", ordinal = 2, at = @At("STORE"))
    private CommandNode<ServerCommandSource> suggestionBlocker$pushNode(CommandNode<ServerCommandSource> value) {
        suggestionBlocker$current = value.getName();
        return value;
    }

    @Inject(method = "makeTreeForSource", at = @At("HEAD"))
    private void suggestionBlocker$pushNode(CommandNode<ServerCommandSource> tree, CommandNode<CommandSource> result, ServerCommandSource source, Map<CommandNode<ServerCommandSource>, CommandNode<CommandSource>> resultNodes, CallbackInfo ci) {
        var name = tree.getName();
        if (!name.isEmpty())
            suggestionBlocker$stack.addLast(name);
    }

    @ModifyExpressionValue(method = "makeTreeForSource", at = @At(value = "INVOKE", target = "Lcom/mojang/brigadier/tree/CommandNode;canUse(Ljava/lang/Object;)Z", remap = false))
    private boolean suggestionBlocker$check(boolean isAllowed, CommandNode<ServerCommandSource> tree, CommandNode<CommandSource> result, ServerCommandSource source, Map<CommandNode<ServerCommandSource>, CommandNode<CommandSource>> resultNodes) {
        var location = Iterables.concat(suggestionBlocker$stack, List.of(suggestionBlocker$current));
        return Permissions.getPermissionValue(source, UniversalPerms.createPermission("view", location))
                .orElse(isAllowed);
    }

    @Inject(method = "makeTreeForSource", at = @At("RETURN"))
    private void suggestionBlocker$popNode(CommandNode<ServerCommandSource> tree, CommandNode<CommandSource> result, ServerCommandSource source, Map<CommandNode<ServerCommandSource>, CommandNode<CommandSource>> resultNodes, CallbackInfo ci) {
        suggestionBlocker$stack.pollLast();
    }
}
