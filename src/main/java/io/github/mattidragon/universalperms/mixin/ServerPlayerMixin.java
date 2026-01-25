package io.github.mattidragon.universalperms.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import io.github.mattidragon.universalperms.ModPermissions;
import io.github.mattidragon.universalperms.UniversalPerms;
import io.github.mattidragon.universalperms.VanillaPermsWrapper;
import me.lucko.fabric.api.permissions.v0.Options;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.permissions.LevelBasedPermissionSet;
import net.minecraft.server.permissions.PermissionLevel;
import net.minecraft.server.permissions.PermissionSet;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Optional;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin extends Player {
    @Unique
    private boolean universal_perms$isCheckingPermission;

    @SuppressWarnings("DataFlowIssue")
    public ServerPlayerMixin(Level level) {
        super(level, null);
    }

    @ModifyReturnValue(method = "permissions", at = @At("RETURN"))
    private PermissionSet wrapVanillaPermissions(PermissionSet original) {
        return new VanillaPermsWrapper(getOverridePermissions().orElse(original), (ServerPlayer) (Object) this);
    }

    @Unique
    private Optional<PermissionSet> getOverridePermissions() {
        if (universal_perms$isCheckingPermission)
            return Optional.empty();

        universal_perms$isCheckingPermission = true;
        var result = Options.get(this, ModPermissions.PERMISSION_LEVEL).<PermissionSet>map(val -> {
            try {
                return LevelBasedPermissionSet.forLevel(PermissionLevel.byId(Integer.parseInt(val)));
            } catch (NumberFormatException e) {
                UniversalPerms.LOGGER.warn("Invalid permission level override for {}", this);
                return null;
            }
        });
        universal_perms$isCheckingPermission = false;
        return result;
    }
}
