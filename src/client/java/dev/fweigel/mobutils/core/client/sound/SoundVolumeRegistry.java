package dev.fweigel.mobutils.core.client.sound;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public final class SoundVolumeRegistry {

    private static final Map<String, Supplier<Float>> ENTRIES = new ConcurrentHashMap<>();

    public static void register(String soundPrefix, Supplier<Float> volumeProvider) {
        ENTRIES.put(soundPrefix, volumeProvider);
    }

    public static float getVolume(String soundPath) {
        for (Map.Entry<String, Supplier<Float>> entry : ENTRIES.entrySet()) {
            if (soundPath.startsWith(entry.getKey())) {
                return entry.getValue().get();
            }
        }
        return 1.0f;
    }

    private SoundVolumeRegistry() {}
}
