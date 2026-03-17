package dev.fweigel.mobutils.core.client.ui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

/**
 * Base class for mod config screens.
 * Provides shared layout constants, a section separator, and non-pausing behaviour.
 */
public abstract class ModScreen extends Screen {

    public static final int BUTTON_WIDTH = 200;
    public static final int HALF_BUTTON_WIDTH = 96;
    public static final int BUTTON_HEIGHT = 20;
    public static final int BUTTON_GAP = 24;

    protected ModScreen(Component title) {
        super(title);
    }

    /** Draws a thin horizontal separator line centred on {@code cx} at height {@code y}. */
    protected void drawSeparator(GuiGraphics graphics, int cx, int y) {
        int halfWidth = 80;
        graphics.fill(cx - halfWidth, y, cx + halfWidth, y + 1, 0x40FFFFFF);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
