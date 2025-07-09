package io.github.mattidragon.universalperms.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import io.github.mattidragon.universalperms.ModPermissions;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.command.CommandSource;
import net.minecraft.command.EntitySelectorReader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntitySelectorReader.class)
public class EntitySelectorReaderMixin {
    @ModifyReturnValue(method = "shouldAllowAtSelectors", at = @At("RETURN"))
    private static boolean applySelectorPermission(boolean original, Object source) {
        if (source instanceof CommandSource commandSource) {
            return Permissions.getPermissionValue(commandSource, ModPermissions.USE_SELECTOR).orElse(original);
        }
        return original;
    }
}
