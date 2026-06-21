package io.github.mattidragon.universalperms.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import io.github.mattidragon.universalperms.ModPermissions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@SuppressWarnings("UnstableApiUsage")
@Mixin({BlockItem.class, SpawnEggItem.class})
public class AdminItemWarningMixin {
    @ModifyExpressionValue(method = "shouldPrintOpWarning", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/permissions/PermissionSet;hasPermission(Lnet/minecraft/server/permissions/Permission;)Z"))
    private boolean modifyLevelForWarning(boolean original, ItemStack stack, Player player) {
        if (player.level().isClientSide()) return original;

        return player.checkPermission(ModPermissions.USE_ADMIN_TOOLS, original);
    }
}
