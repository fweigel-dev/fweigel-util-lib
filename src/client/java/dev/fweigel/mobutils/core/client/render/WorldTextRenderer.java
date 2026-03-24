package dev.fweigel.mobutils.core.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderContext;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec3;

public final class WorldTextRenderer {

    public static void renderFloatingText(LevelRenderContext context, String text, double x, double y, double z, int color) {
        CameraRenderState camera = context.levelState().cameraRenderState;

        PoseStack poseStack = context.poseStack();
        if (poseStack == null) {
            return;
        }

        SubmitNodeCollector collector = context.submitNodeCollector();
        if (collector == null) {
            return;
        }

        Component component = Component.literal(text).withColor(color);
        double distSq = camera.pos.distanceToSqr(x, y, z);

        poseStack.pushPose();
        poseStack.translate(x - camera.pos.x, y - camera.pos.y, z - camera.pos.z);

        collector.submitNameTag(poseStack, Vec3.ZERO, 0, component, true, 0xF000F0, distSq, camera);

        poseStack.popPose();
    }

    private WorldTextRenderer() {}
}
