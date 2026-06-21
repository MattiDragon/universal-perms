package io.github.mattidragon.universalperms.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import io.github.mattidragon.universalperms.ModPermissions;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@SuppressWarnings("UnstableApiUsage")
@Mixin(ServerGamePacketListenerImpl.class)
public class ServerGamePacketListenerImplMixin {
    @Shadow public ServerPlayer player;

    @ModifyExpressionValue(method = "handleEntityTagQuery", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/permissions/PermissionSet;hasPermission(Lnet/minecraft/server/permissions/Permission;)Z"))
    private boolean checkEntityNbtQueryPerms(boolean old) {
        return player.checkPermission(ModPermissions.QUERY_ENTITY_NBT, old);
    }

    @ModifyExpressionValue(method = "handleBlockEntityTagQuery", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/permissions/PermissionSet;hasPermission(Lnet/minecraft/server/permissions/Permission;)Z"))
    private boolean checkBlockNbtQueryPerms(boolean old) {
        return player.checkPermission(ModPermissions.QUERY_BLOCK_NBT, old);
    }

    @ModifyExpressionValue(method = "handleChangeDifficulty", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/permissions/PermissionSet;hasPermission(Lnet/minecraft/server/permissions/Permission;)Z"))
    private boolean checkDifficultyUpdatePerms(boolean old) {
        return player.checkPermission(ModPermissions.UPDATE_DIFFICULTY, old);
    }

    @ModifyExpressionValue(method = "handleLockDifficulty", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/permissions/PermissionSet;hasPermission(Lnet/minecraft/server/permissions/Permission;)Z"))
    private boolean checkDifficultyLockUpdatePerms(boolean old) {
        return player.checkPermission(ModPermissions.UPDATE_DIFFICULTY_LOCK, player.checkPermission(ModPermissions.UPDATE_DIFFICULTY, old));
    }

    @ModifyExpressionValue(method = "handleChangeGameMode", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/permissions/PermissionCheck;check(Lnet/minecraft/server/permissions/PermissionSet;)Z"))
    private boolean checkGameModeUpdatePerms(boolean old) {
        return player.checkPermission(ModPermissions.UPDATE_GAME_MODE, old);
    }
}
