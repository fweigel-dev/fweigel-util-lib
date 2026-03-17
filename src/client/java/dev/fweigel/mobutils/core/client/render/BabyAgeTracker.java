package dev.fweigel.mobutils.core.client.render;

import java.util.HashMap;
import java.util.Map;

public final class BabyAgeTracker {

    private static final Map<Integer, Integer> ages = new HashMap<>();

    public static void update(int entityId, int age) {
        ages.put(entityId, age);
    }

    public static Integer getAge(int entityId) {
        return ages.get(entityId);
    }

    public static Map<Integer, Integer> getAll() {
        return ages;
    }

    public static void clear() {
        ages.clear();
    }

    private BabyAgeTracker() {}
}
