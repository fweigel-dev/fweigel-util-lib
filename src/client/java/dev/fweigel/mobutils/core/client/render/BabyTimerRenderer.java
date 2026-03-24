package dev.fweigel.mobutils.core.client.render;

import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderContext;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;

import java.util.function.Predicate;

public final class BabyTimerRenderer {

    public static void render(LevelRenderContext context, Predicate<Entity> entityFilter) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.player == null) {
            return;
        }

        for (var entry : BabyAgeTracker.getAll().entrySet()) {
            int entityId = entry.getKey();
            int age = entry.getValue();

            if (age >= 0) {
                continue;
            }

            Entity entity = mc.level.getEntity(entityId);
            if (entity == null || !entity.isAlive() || !entityFilter.test(entity)) {
                continue;
            }

            int remainingTicks = Math.abs(age);
            int totalSeconds = remainingTicks / 20;
            int minutes = totalSeconds / 60;
            int seconds = totalSeconds % 60;
            String text = String.format("%d:%02d", minutes, seconds);

            int color;
            if (totalSeconds > 5 * 60) {
                color = 0xFF00FF00;
            } else if (totalSeconds > 60) {
                color = 0xFFFFFF00;
            } else {
                color = 0xFFFFAA00;
            }

            double renderY = entity.getY() + entity.getBbHeight() + 0.5;
            WorldTextRenderer.renderFloatingText(context, text,
                    entity.getX(), renderY, entity.getZ(), color);
        }
    }

    private BabyTimerRenderer() {}
}
