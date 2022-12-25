package io.github.mattidragon.universalperms.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import io.github.mattidragon.universalperms.ModPermissions;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {
    @Shadow public abstract PlayerAbilities getAbilities();

    @ModifyReturnValue(method = "isCreativeLevelTwoOp", at = @At("RETURN"))
    private boolean universal_perms$modify_admin_tool_access(boolean old) {
        return Permissions.getPermissionValue((PlayerEntity)(Object)this, ModPermissions.USE_ADMIN_TOOLS).map(value -> value && this.getAbilities().creativeMode).orElse(old);
    }
}
