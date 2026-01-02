package io.github.mattidragon.universalperms.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import io.github.mattidragon.universalperms.ModPermissions;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.command.EntitySelector;
import net.minecraft.server.command.ServerCommandSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntitySelector.class)
public class EntitySelectorMixin {
    @ModifyExpressionValue(
        method = "checkSourcePermission",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/command/permission/PermissionPredicate;hasPermission(Lnet/minecraft/command/permission/Permission;)Z"
        )
    )
    private boolean applySelectorPermission(boolean old, ServerCommandSource source) {
        return Permissions.getPermissionValue(source, ModPermissions.USE_SELECTOR).orElse(old);
    }
}
