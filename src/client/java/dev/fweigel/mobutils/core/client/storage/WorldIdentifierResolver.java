package dev.fweigel.mobutils.core.client.storage;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.world.level.storage.LevelResource;

import java.nio.file.Path;

public final class WorldIdentifierResolver {

    private WorldIdentifierResolver() {}

    /**
     * Returns a sanitized, filesystem-safe identifier for the current world.
     * For singleplayer: the world folder name.
     * For multiplayer: "Multiplayer_<sanitized_ip>".
     * Returns null if no world is loaded.
     */
    public static String resolve(Minecraft client) {
        if (client.getSingleplayerServer() != null) {
            Path worldDir = client.getSingleplayerServer()
                    .getWorldPath(LevelResource.ROOT)
                    .normalize();
            Path folder = worldDir.getFileName();
            if (folder == null || folder.toString().isBlank() || ".".equals(folder.toString())) {
                Path parent = worldDir.getParent();
                folder = parent != null ? parent.getFileName() : null;
            }
            if (folder == null) return null;
            return sanitize(folder.toString());
        }

        ServerData server = client.getCurrentServer();
        if (server != null) {
            return "Multiplayer_" + sanitize(server.ip);
        }

        return null;
    }

    private static String sanitize(String name) {
        if (name == null) return "world";
        String s = name.trim().replaceAll("[^a-zA-Z0-9._-]", "_");
        return s.isEmpty() ? "world" : s;
    }
}
