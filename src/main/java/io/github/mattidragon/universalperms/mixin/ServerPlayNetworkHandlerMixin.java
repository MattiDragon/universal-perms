package io.github.mattidragon.universalperms.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import io.github.mattidragon.universalperms.ModPermissions;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin {
    @Shadow public ServerPlayerEntity player;

    @ModifyExpressionValue(method = "onQueryEntityNbt", at = @At(value = "INVOKE", target = "net/minecraft/server/network/ServerPlayerEntity.hasPermissionLevel(I)Z"))
    private boolean universal_perms$check_entity_nbt_query_perms(boolean old) {
        return Permissions.getPermissionValue(this.player, ModPermissions.QUERY_ENTITY_NBT).orElse(old);
    }

    @ModifyExpressionValue(method = "onQueryBlockNbt", at = @At(value = "INVOKE", target = "net/minecraft/server/network/ServerPlayerEntity.hasPermissionLevel(I)Z"))
    private boolean universal_perms$check_block_nbt_query_perms(boolean old) {
        return Permissions.getPermissionValue(this.player, ModPermissions.QUERY_BLOCK_NBT).orElse(old);
    }

    @ModifyExpressionValue(method = "onUpdateDifficulty", at = @At(value = "INVOKE", target = "net/minecraft/server/network/ServerPlayerEntity.hasPermissionLevel(I)Z"))
    private boolean universal_perms$check_difficulty_update_perms(boolean old) {
        return Permissions.getPermissionValue(this.player, ModPermissions.UPDATE_DIFFICULTY).orElse(old);
    }

    @ModifyExpressionValue(method = "onUpdateDifficultyLock", at = @At(value = "INVOKE", target = "net/minecraft/server/network/ServerPlayerEntity.hasPermissionLevel(I)Z"))
    private boolean universal_perms$check_difficulty_lock_update_perms(boolean old) {
        return Permissions.getPermissionValue(this.player, ModPermissions.UPDATE_DIFFICULTY_LOCK).orElse(Permissions.getPermissionValue(this.player, ModPermissions.UPDATE_DIFFICULTY).orElse(old));
    }
}
