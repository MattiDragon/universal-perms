package io.github.mattidragon.universalperms.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.github.mattidragon.universalperms.ModPermissions;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.EntityArgumentType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityArgumentType.class)
public class EntityArgumentTypeMixin {
    @ModifyExpressionValue(method = "listSuggestions", at = @At(value = "INVOKE", target = "net/minecraft/command/CommandSource.hasPermissionLevel(I)Z"))
    private boolean universal_perms$checkSelector(boolean old, CommandContext<?> context, SuggestionsBuilder builder) {
        var source = (CommandSource) context.getSource();
        return Permissions.getPermissionValue(source, ModPermissions.USE_SELECTOR).orElse(old);
    }
}
