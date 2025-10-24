package io.github.mattidragon.universalperms.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import io.github.mattidragon.universalperms.ModPermissions;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({BlockItem.class, SpawnEggItem.class})
public class AdminItemWarningMixin {
    @ModifyExpressionValue(method = "shouldShowOperatorBlockWarnings", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;getPermissionLevel()I"))
    private int modifyLevelForWarning(int original, ItemStack stack, PlayerEntity player) {
        if (player.getEntityWorld().isClient()) return original;

        return Permissions.getPermissionValue(player, ModPermissions.USE_ADMIN_TOOLS)
                .map(hasPermission -> hasPermission ? 1000 : -1000)
                .orElse(original);
    }
}
