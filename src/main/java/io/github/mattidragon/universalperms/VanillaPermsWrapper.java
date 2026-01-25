package io.github.mattidragon.universalperms;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.permissions.Permission;
import net.minecraft.server.permissions.PermissionSet;
import net.minecraft.server.permissions.Permissions;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public class VanillaPermsWrapper implements PermissionSet {
    private final PermissionSet delegate;
    private final SharedSuggestionProvider context;

    public VanillaPermsWrapper(PermissionSet delegate, ServerPlayer player) {
        this.delegate = delegate;
        this.context = new CommandSourceStack(
                player.commandSource(),
                Vec3.ZERO,
                Vec2.ZERO,
                player.level(),
                delegate,
                player.getPlainTextName(),
                player.getDisplayName(),
                player.level().getServer(),
                player
        );
    }

    @Override
    public boolean hasPermission(Permission permission) {
        var original = delegate.hasPermission(permission);
        if (permission == Permissions.COMMANDS_ENTITY_SELECTORS) {
            return me.lucko.fabric.api.permissions.v0.Permissions.getPermissionValue(context, ModPermissions.USE_SELECTOR)
                    .orElse(original);
        }

        return original;
    }
}
