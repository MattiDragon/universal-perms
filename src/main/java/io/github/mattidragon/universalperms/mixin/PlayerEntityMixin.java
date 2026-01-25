package io.github.mattidragon.universalperms.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import io.github.mattidragon.universalperms.ModPermissions;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Player.class)
public abstract class PlayerEntityMixin {
    @Shadow public abstract Abilities getAbilities();

    @ModifyReturnValue(method = "canUseGameMasterBlocks", at = @At("RETURN"))
    private boolean modifyAdminToolAccess(boolean old) {
        return Permissions.getPermissionValue((Player)(Object)this, ModPermissions.USE_ADMIN_TOOLS)
                .map(value -> value && this.getAbilities().instabuild).orElse(old);
    }
}
