package io.github.mattidragon.universalperms;

import net.fabricmc.fabric.api.permission.v1.PermissionNode;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.resources.Identifier;

@SuppressWarnings("UnstableApiUsage")
public class ModPermissions {
    public static final Identifier QUERY_BLOCK_NBT = Identifier.fromNamespaceAndPath("universal_perms", "misc/query_block_nbt");
    public static final Identifier QUERY_ENTITY_NBT = Identifier.fromNamespaceAndPath("universal_perms", "misc/query_entity_nbt");
    public static final Identifier UPDATE_DIFFICULTY = Identifier.fromNamespaceAndPath("universal_perms", "misc/update_difficulty");
    public static final Identifier UPDATE_DIFFICULTY_LOCK = Identifier.fromNamespaceAndPath("universal_perms", "misc/update_difficulty_lock");
    public static final Identifier USE_ADMIN_TOOLS = Identifier.fromNamespaceAndPath("universal_perms", "misc/use_admin_blocks");
    public static final Identifier UPDATE_GAME_MODE = Identifier.fromNamespaceAndPath("universal_perms", "misc/gamemode_switcher");
    public static final PermissionNode<Integer> PERMISSION_LEVEL = PermissionNode.ofInteger("universal_perms", "misc/forced_permission_level");

    public static void usePermissions(CommandSourceStack commandSource) {
        commandSource.checkPermission(QUERY_BLOCK_NBT);
        commandSource.checkPermission(QUERY_ENTITY_NBT);
        commandSource.checkPermission(UPDATE_DIFFICULTY);
        commandSource.checkPermission(UPDATE_DIFFICULTY_LOCK);
        commandSource.checkPermission(USE_ADMIN_TOOLS);
        commandSource.checkPermission(UPDATE_GAME_MODE);
        commandSource.checkPermission(PERMISSION_LEVEL);
    }
}
