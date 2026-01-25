package io.github.mattidragon.universalperms.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.mojang.authlib.GameProfile;
import io.github.mattidragon.universalperms.ModPermissions;
import io.github.mattidragon.universalperms.UniversalPerms;
import me.lucko.fabric.api.permissions.v0.Options;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerEntityMixin extends Player {
    @Unique
    private boolean universal_perms$isCheckingPermission;

    public ServerPlayerEntityMixin(Level world, GameProfile profile) {
        super(world, profile);
    }

    @ModifyReturnValue(method = "getPermissionLevel", at = @At("RETURN"))
    private int overridePermissionLevel(int old) {
        if (universal_perms$isCheckingPermission)
            return old;
        universal_perms$isCheckingPermission = true;
        var result = Options.get(this, ModPermissions.PERMISSION_LEVEL).map(val -> {
            try {
                return Integer.parseInt(val);
            } catch (NumberFormatException e) {
                UniversalPerms.LOGGER.warn("Invalid permission level override for " + this);
                return null;
            }
        }).orElse(old);
        universal_perms$isCheckingPermission = true;
        return result;
    }
}
