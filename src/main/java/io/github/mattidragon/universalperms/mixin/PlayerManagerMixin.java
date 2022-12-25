package io.github.mattidragon.universalperms.mixin;

import io.github.mattidragon.universalperms.ModPermissions;
import io.github.mattidragon.universalperms.UniversalPerms;
import me.lucko.fabric.api.permissions.v0.Options;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(PlayerManager.class)
public abstract class PlayerManagerMixin {
    @ModifyArg(method = "sendCommandTree(Lnet/minecraft/server/network/ServerPlayerEntity;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/server/PlayerManager;sendCommandTree(Lnet/minecraft/server/network/ServerPlayerEntity;I)V"))
    private int universal_perms$override_permission_level(ServerPlayerEntity player, int old) {
        return Options.get(player, ModPermissions.PERMISSION_LEVEL).map(val -> {
            try {
                return Integer.parseInt(val);
            } catch (NumberFormatException e) {
                UniversalPerms.LOGGER.warn("Invalid permission level override for " + this);
                return null;
            }
        }).orElse(old);
    }
}
