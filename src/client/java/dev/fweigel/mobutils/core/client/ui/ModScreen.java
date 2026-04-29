package dev.fweigel.mobutils.core.client.ui;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

/**
 * Base class for mod config screens.
 * Provides shared layout constants, card preview rendering, and non-pausing behaviour.
 */
public abstract class ModScreen extends Screen {

    // ── Button layout ──────────────────────────────────────────────────────────
    public static final int BUTTON_WIDTH      = 200;
    public static final int HALF_BUTTON_WIDTH = 96;
    public static final int BUTTON_HEIGHT     = 20;
    public static final int BUTTON_GAP        = 24;

    // ── Card preview layout ────────────────────────────────────────────────────
    /** Standard card preview width — matches HALF_BUTTON_WIDTH so button fits exactly below. */
    public static final int CARD_W       = 96;
    /** Standard card preview height — 16:9 of CARD_W. */
    public static final int CARD_PREV_H  = 54;
    /** Pixel gap between the preview image and the button below it. */
    public static final int CARD_BTN_GAP = 2;
    /** Pixel gap between two card columns. */
    public static final int COL_GAP      = 8;
    /** Pixel gap between card rows. */
    public static final int ROW_GAP      = 8;

    /** Total height of one card slot (preview + gap + button). */
    public static final int CARD_H = CARD_PREV_H + CARD_BTN_GAP + BUTTON_HEIGHT;

    // Source PNG dimensions all card preview images must match.
    private static final int CARD_SRC_W = 320;
    private static final int CARD_SRC_H = 180;

    protected ModScreen(Component title) {
        super(title);
    }

    // ── Static card-rendering API (usable from non-Screen contexts) ────────────

    /** Draws a standard 96×54 card preview image with a 1px black border. */
    public static void renderCard(GuiGraphicsExtractor g, int x, int y, Identifier texture) {
        renderCard(g, x, y, CARD_W, CARD_PREV_H, texture);
    }

    /** Draws a card preview image scaled to {@code cardW}×{@code cardH} with a 1px black border. */
    public static void renderCard(GuiGraphicsExtractor g, int x, int y,
                                  int cardW, int cardH, Identifier texture) {
        g.fill(x - 1, y - 1, x + cardW + 1, y + cardH + 1, 0xFF000000);
        float sx = (float) cardW / CARD_SRC_W;
        float sy = (float) cardH / CARD_SRC_H;
        g.pose().pushMatrix();
        g.pose().scale(sx, sy);
        g.blit(RenderPipelines.GUI_TEXTURED, texture,
                Math.round(x / sx), Math.round(y / sy),
                0f, 0f, CARD_SRC_W, CARD_SRC_H, CARD_SRC_W, CARD_SRC_H);
        g.pose().popMatrix();
    }

    // ── Protected instance helpers (delegate to statics) ──────────────────────

    protected void drawSeparator(GuiGraphicsExtractor graphics, int cx, int y) {
        int halfWidth = 80;
        graphics.fill(cx - halfWidth, y, cx + halfWidth, y + 1, 0x40FFFFFF);
    }

    protected void drawCardPreview(GuiGraphicsExtractor graphics, int x, int y, Identifier texture) {
        renderCard(graphics, x, y, texture);
    }

    protected void drawCardPreview(GuiGraphicsExtractor graphics, int x, int y,
                                   int cardW, int cardH, Identifier texture) {
        renderCard(graphics, x, y, cardW, cardH, texture);
    }

    protected void drawAnimatedCardPreview(GuiGraphicsExtractor graphics, int x, int y,
                                           Identifier[] frames, long frameIntervalMs) {
        int frame = (int) ((System.currentTimeMillis() / frameIntervalMs) % frames.length);
        renderCard(graphics, x, y, frames[frame]);
    }

    protected void drawAnimatedCardPreview(GuiGraphicsExtractor graphics, int x, int y,
                                           int cardW, int cardH,
                                           Identifier[] frames, long frameIntervalMs) {
        int frame = (int) ((System.currentTimeMillis() / frameIntervalMs) % frames.length);
        renderCard(graphics, x, y, cardW, cardH, frames[frame]);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
