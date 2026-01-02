package io.github.mattidragon.universalperms.mixin;

import io.github.mattidragon.universalperms.ModPermissions;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({BlockItem.class, SpawnEggItem.class})
public class AdminItemWarningMixin {
    @Inject(
        method = "shouldShowOperatorBlockWarnings",
        at = @At("HEAD"),
        cancellable = true
    )
    private void universal_perms$overrideWarnings(
        ItemStack stack,
        @Nullable PlayerEntity player,
        CallbackInfoReturnable<Boolean> cir
    ) {
        if (player == null || player.getEntityWorld().isClient()) return;
        if (Permissions.getPermissionValue(player, ModPermissions.USE_ADMIN_TOOLS) == TriState.FALSE) {
            cir.setReturnValue(false);
        }
    }
}
