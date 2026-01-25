package io.github.mattidragon.universalperms;

import me.lucko.fabric.api.permissions.v0.Options;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.commands.SharedSuggestionProvider;
import org.jetbrains.annotations.NotNull;

public class ModPermissions {
    public static final String USE_SELECTOR = "universal_perms.misc.selector";
    public static final String QUERY_BLOCK_NBT = "universal_perms.misc.query_block_nbt";
    public static final String QUERY_ENTITY_NBT = "universal_perms.misc.query_entity_nbt";
    public static final String UPDATE_DIFFICULTY = "universal_perms.misc.update_difficulty";
    public static final String UPDATE_DIFFICULTY_LOCK = "universal_perms.misc.update_difficulty_lock";
    public static final String USE_ADMIN_TOOLS = "universal_perms.misc.use_admin_blocks";
    public static final String UPDATE_GAME_MODE = "universal_perms.misc.gamemode_switcher";
    public static final String PERMISSION_LEVEL = "universal_perms.misc.forced_permission_level";

    public static void usePermissions(SharedSuggestionProvider commandSource) {
        Permissions.getPermissionValue(commandSource, USE_SELECTOR);
        Permissions.getPermissionValue(commandSource, QUERY_BLOCK_NBT);
        Permissions.getPermissionValue(commandSource, QUERY_ENTITY_NBT);
        Permissions.getPermissionValue(commandSource, UPDATE_DIFFICULTY);
        Permissions.getPermissionValue(commandSource, UPDATE_DIFFICULTY_LOCK);
        Permissions.getPermissionValue(commandSource, USE_ADMIN_TOOLS);
        Permissions.getPermissionValue(commandSource, UPDATE_GAME_MODE);
        Options.get(commandSource, PERMISSION_LEVEL);
    }
}
