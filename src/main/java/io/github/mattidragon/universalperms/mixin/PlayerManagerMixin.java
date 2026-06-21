package io.github.mattidragon.universalperms.mixin;

import io.github.mattidragon.universalperms.ModPermissions;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.permissions.LevelBasedPermissionSet;
import net.minecraft.server.permissions.PermissionLevel;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.Optional;

@SuppressWarnings("UnstableApiUsage")
@Mixin(PlayerList.class)
public abstract class PlayerManagerMixin {
    @ModifyArg(method = "sendPlayerPermissionLevel(Lnet/minecraft/server/level/ServerPlayer;)V",
            index = 1,
            at = @At(value = "INVOKE", target = "Lnet/minecraft/server/players/PlayerList;sendPlayerPermissionLevel(Lnet/minecraft/server/level/ServerPlayer;Lnet/minecraft/server/permissions/LevelBasedPermissionSet;)V"))
    private LevelBasedPermissionSet overridePermissionLevelForClient(ServerPlayer player, LevelBasedPermissionSet original) {
        return Optional.ofNullable(player.checkPermission(ModPermissions.PERMISSION_LEVEL))
                .map(val -> LevelBasedPermissionSet.forLevel(PermissionLevel.byId(val)))
                .orElse(original);
    }
}
