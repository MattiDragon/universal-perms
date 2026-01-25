package io.github.mattidragon.universalperms.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import io.github.mattidragon.universalperms.ModPermissions;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.selector.EntitySelector;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntitySelector.class)
public class EntitySelectorMixin {
    @ModifyExpressionValue(method = "checkPermissions", at = @At(value = "INVOKE", target = "Lnet/minecraft/commands/CommandSourceStack;allowsSelectors()Z"))
    private boolean applySelectorPermission(boolean old, CommandSourceStack source) {
        return Permissions.getPermissionValue(source, ModPermissions.USE_SELECTOR).orElse(old);
    }
}
