package io.github.mattidragon.universalperms.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import io.github.mattidragon.universalperms.ModPermissions;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({BlockItem.class, SpawnEggItem.class})
public class AdminItemWarningMixin {
    @ModifyExpressionValue(method = "shouldPrintOpWarning", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;getPermissionLevel()I"))
    private int modifyLevelForWarning(int original, ItemStack stack, Player player) {
        if (player.level().isClientSide) return original;

        return Permissions.getPermissionValue(player, ModPermissions.USE_ADMIN_TOOLS)
                .map(hasPermission -> hasPermission ? 1000 : -1000)
                .orElse(original);
    }
}
