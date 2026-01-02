package io.github.mattidragon.universalperms.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.mojang.authlib.GameProfile;
import io.github.mattidragon.universalperms.ModPermissions;
import io.github.mattidragon.universalperms.UniversalPerms;
import me.lucko.fabric.api.permissions.v0.Options;
import net.minecraft.command.permission.LeveledPermissionPredicate;
import net.minecraft.command.permission.PermissionLevel;
import net.minecraft.command.permission.PermissionPredicate;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity {
    @Unique
    private boolean universal_perms$isCheckingPermission;

    public ServerPlayerEntityMixin(World world, GameProfile profile) {
        super(world, profile);
    }

    @ModifyReturnValue(method = "getPermissions", at = @At("RETURN"))
    private PermissionPredicate overridePermissionLevel(PermissionPredicate original) {
        if (universal_perms$isCheckingPermission) return original;
        universal_perms$isCheckingPermission = true;

        var result = Options.get(this, ModPermissions.PERMISSION_LEVEL).map(val -> {
            try {
                var numericPermission = Integer.parseInt(val);
                return (PermissionPredicate) LeveledPermissionPredicate.fromLevel(
                    PermissionLevel.fromLevel(numericPermission)
                );
            } catch (NumberFormatException e) {
                UniversalPerms.LOGGER.warn("Invalid permission level override for " + this);
                return null;
            }
        }).orElse(original);
        universal_perms$isCheckingPermission = false;
        return result;
    }
}
