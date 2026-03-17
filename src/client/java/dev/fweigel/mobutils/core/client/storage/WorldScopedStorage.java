package dev.fweigel.mobutils.core.client.storage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

/**
 * Handles per-world GSON-based persistence for a mod.
 * Stores data in {@code <gameDir>/<folderName>/<worldId>.json}.
 *
 * <p>Usage:
 * <pre>{@code
 * private static final WorldScopedStorage<SaveData> STORAGE =
 *     new WorldScopedStorage<>("mymod", SaveData.class, MyMod.LOGGER);
 *
 * public static void loadForWorld(Minecraft client) {
 *     STORAGE.loadForWorld(client).ifPresentOrElse(
 *         data -> { /* apply data *\/ },
 *         MyConfig::reset
 *     );
 * }
 *
 * public static void save() {
 *     STORAGE.save(MyConfig.toSaveData());
 * }
 * }</pre>
 */
public class WorldScopedStorage<T> {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private final String folderName;
    private final Class<T> dataClass;
    private final Logger logger;
    private String currentWorldId;

    public WorldScopedStorage(String folderName, Class<T> dataClass, Logger logger) {
        this.folderName = folderName;
        this.dataClass = dataClass;
        this.logger = logger;
    }

    /**
     * Resolves the current world, then reads and returns the saved data if present.
     * Returns {@link Optional#empty()} if no save file exists yet.
     */
    public Optional<T> loadForWorld(Minecraft client) {
        currentWorldId = WorldIdentifierResolver.resolve(client);
        Path path = getSavePath();
        if (path == null || !Files.exists(path)) {
            return Optional.empty();
        }
        try {
            String json = Files.readString(path);
            return Optional.ofNullable(GSON.fromJson(json, dataClass));
        } catch (IOException e) {
            logger.error("Failed to load {} data for world {}", folderName, currentWorldId, e);
            return Optional.empty();
        }
    }

    /**
     * Serializes {@code data} and writes it to the current world's save file.
     */
    public void save(T data) {
        Path path = getSavePath();
        if (path == null || data == null) return;
        try {
            Files.createDirectories(path.getParent());
            Files.writeString(path, GSON.toJson(data));
        } catch (IOException e) {
            logger.error("Failed to save {} data for world {}", folderName, currentWorldId, e);
        }
    }

    /**
     * Returns the path to the current world's save file, or null if no world is loaded.
     * Only valid after {@link #loadForWorld} has been called.
     */
    public Path getActivePath() {
        return getSavePath();
    }

    private Path getSavePath() {
        if (currentWorldId == null) return null;
        return FabricLoader.getInstance().getGameDir()
                .resolve(folderName)
                .resolve(currentWorldId + ".json");
    }
}
