package io.github.mattidragon.suggestionblocker;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;

public class Config {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path PATH = FabricLoader.getInstance().getConfigDir().resolve("suggestion-blocker.json");
    public static Config instance;

    public boolean blockByDefault = false;
    public LinkedHashMap<String, Filter> filters = new LinkedHashMap<>();

    public static boolean reload() {
        try {
            if (!Files.exists(PATH)) {
                instance = new Config();
                try (var out = Files.newBufferedWriter(PATH)) {
                    GSON.toJson(instance, out);
                }
            } else {
                try (var in = Files.newBufferedReader(PATH)) {
                    instance = GSON.fromJson(in, Config.class);
                }
            }
            return true;
        } catch (IOException | JsonParseException e) {
            if (instance == null)
                UniversalPerms.LOGGER.error("Error loading config, falling back to default!", e);
            else
                UniversalPerms.LOGGER.error("Error loading config, falling back to previous!", e);
            return false;
        }
    }

    /**
     * Dummy for static init
     */
    public static void register() {}

    static {
        reload();
    }

    public static class Filter {
        public String[] blacklist = new String[0];
        public String[] whitelist = new String[0];
    }
}
