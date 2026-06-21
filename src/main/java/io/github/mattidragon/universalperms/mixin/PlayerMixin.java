package io.github.mattidragon.universalperms.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import io.github.mattidragon.universalperms.ModPermissions;
import net.fabricmc.fabric.api.permission.v1.PermissionContextOwner;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@SuppressWarnings("UnstableApiUsage")
@Mixin(Player.class)
public abstract class PlayerMixin implements PermissionContextOwner {
    @ModifyExpressionValue(method = "canUseGameMasterBlocks", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/permissions/PermissionSet;hasPermission(Lnet/minecraft/server/permissions/Permission;)Z"))
    private boolean modifyAdminToolAccess(boolean original) {
        return checkPermission(ModPermissions.USE_ADMIN_TOOLS, original);
    }
}
