package dev.fweigel.mobutils.core.client.util;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;

/**
 * Helper for registering a mod config key binding.
 *
 * <p>Usage:
 * <pre>{@code
 * configKey = ConfigKeyHelper.register(MyMod.MOD_ID, "key.mymod.config", GLFW.GLFW_KEY_U);
 * }</pre>
 */
public final class ConfigKeyHelper {

    private ConfigKeyHelper() {}

    public static KeyMapping register(String modId, String translationKey, int glfwKey) {
        KeyMapping.Category keyCategory = KeyMapping.Category.register(
                Identifier.fromNamespaceAndPath(modId, "general"));
        return KeyBindingHelper.registerKeyBinding(
                new KeyMapping(translationKey, glfwKey, keyCategory));
    }
}
