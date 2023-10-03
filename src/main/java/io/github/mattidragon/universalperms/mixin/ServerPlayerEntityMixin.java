package io.github.mattidragon.universalperms.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.mojang.authlib.GameProfile;
import io.github.mattidragon.universalperms.ModPermissions;
import io.github.mattidragon.universalperms.UniversalPerms;
import me.lucko.fabric.api.permissions.v0.Options;
import net.minecraft.entity.player.PlayerEntity;
// FIXME: import net.minecraft.network.encryption.PlayerPublicKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
// FIXME: import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity {
    @Unique
    private boolean universal_perms$is_checking_permission;

    // API call fails with publicKey --> Seems to have been removed from API
    // public ServerPlayerEntityMixin(World world, BlockPos pos, float yaw,
    // GameProfile gameProfile, @Nullable PlayerPublicKey publicKey) {
    public ServerPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
        // super(world, pos, yaw, gameProfile, publicKey);
        super(world, pos, yaw, gameProfile);
        throw new IllegalStateException();
    }

    @ModifyReturnValue(method = "getPermissionLevel", at = @At("RETURN"))
    private int universal_perms$override_permission_level(int old) {
        if (universal_perms$is_checking_permission)
            return old;
        universal_perms$is_checking_permission = true;
        var result = Options.get(this, ModPermissions.PERMISSION_LEVEL).map(val -> {
            try {
                return Integer.parseInt(val);
            } catch (NumberFormatException e) {
                UniversalPerms.LOGGER.warn("Invalid permission level override for " + this);
                return null;
            }
        }).orElse(old);
        universal_perms$is_checking_permission = true;
        return result;
    }
}
