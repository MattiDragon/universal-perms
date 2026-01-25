package io.github.mattidragon.universalperms.mixin;

import io.github.mattidragon.universalperms.ModPermissions;
import io.github.mattidragon.universalperms.UniversalPerms;
import me.lucko.fabric.api.permissions.v0.Options;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.permissions.LevelBasedPermissionSet;
import net.minecraft.server.permissions.PermissionLevel;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(PlayerList.class)
public abstract class PlayerManagerMixin {
    @ModifyArg(method = "sendPlayerPermissionLevel(Lnet/minecraft/server/level/ServerPlayer;)V",
            index = 1,
            at = @At(value = "INVOKE", target = "Lnet/minecraft/server/players/PlayerList;sendPlayerPermissionLevel(Lnet/minecraft/server/level/ServerPlayer;Lnet/minecraft/server/permissions/LevelBasedPermissionSet;)V"))
    private LevelBasedPermissionSet overridePermissionLevelForClient(ServerPlayer player, LevelBasedPermissionSet original) {
        return Options.get(player, ModPermissions.PERMISSION_LEVEL).map(val -> {
            try {
                return LevelBasedPermissionSet.forLevel(PermissionLevel.byId(Integer.parseInt(val)));
            } catch (NumberFormatException e) {
                UniversalPerms.LOGGER.warn("Invalid permission level override for {}", this);
                return null;
            }
        }).orElse(original);
    }
}
