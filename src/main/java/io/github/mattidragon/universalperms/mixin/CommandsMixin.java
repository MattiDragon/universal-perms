package io.github.mattidragon.universalperms.mixin;

import com.google.common.collect.Iterables;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.brigadier.tree.CommandNode;
import io.github.mattidragon.universalperms.UniversalPerms;
import net.fabricmc.fabric.api.permission.v1.PermissionContextOwner;
import net.minecraft.commands.Commands;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Map;

@SuppressWarnings("UnstableApiUsage")
@Mixin(Commands.class)
public abstract class CommandsMixin {
    @Unique
    private static final ThreadLocal<Deque<String>> universal_perms$stack = ThreadLocal.withInitial(ArrayDeque::new);

    @Inject(method = "fillUsableCommands", at = @At("HEAD"))
    private static <S> void pushNode(CommandNode<S> root, CommandNode<S> newRoot, S source, Map<CommandNode<S>, CommandNode<S>> nodes, CallbackInfo ci) {
        var name = root.getName();
        if (!name.isEmpty())
            universal_perms$stack.get().addLast(name);
    }

    @WrapOperation(method = "fillUsableCommands", at = @At(value = "INVOKE", target = "Lcom/mojang/brigadier/tree/CommandNode;canUse(Ljava/lang/Object;)Z", remap = false))
    private static <S> boolean check(CommandNode<S> instance, S source, Operation<Boolean> original) {
        var originalValue = original.call(instance, source);
        if (!(source instanceof PermissionContextOwner commandSource)) {
            return originalValue;
        }
        var nodePath = Iterables.concat(universal_perms$stack.get(), List.of(instance.getName()));
        return commandSource.checkPermission(UniversalPerms.createPermission("view", nodePath), originalValue);
    }

    @Inject(method = "fillUsableCommands", at = @At("RETURN"))
    private static <S> void popNode(CommandNode<S> root, CommandNode<S> newRoot, S source, Map<CommandNode<S>, CommandNode<S>> nodes, CallbackInfo ci) {
        universal_perms$stack.get().pollLast();
    }
}
