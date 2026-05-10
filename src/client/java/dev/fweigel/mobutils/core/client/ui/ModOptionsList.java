package dev.fweigel.mobutils.core.client.ui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import java.util.List;
import java.util.function.Supplier;

public class ModOptionsList extends ContainerObjectSelectionList<ModOptionsList.Entry> {

    public static final int ITEM_HEIGHT = 24;

    public ModOptionsList(Minecraft mc, int width, int y, int height) {
        super(mc, width, height, y, ITEM_HEIGHT);
        this.centerListVertically = false;
    }

    @Override
    public int getRowWidth() {
        return ModScreen.BUTTON_WIDTH;
    }

    @Override
    public int getRowLeft() {
        return this.getX() + this.width / 2 - this.getRowWidth() / 2;
    }

    // ── Entry-addition API ─────────────────────────────────────────────────────

    public void addWide(AbstractWidget widget) {
        addEntry(new WideEntry(widget));
    }

    public void addSplit(AbstractWidget left, AbstractWidget right) {
        addEntry(new SplitEntry(left, right));
    }

    public void addCustom(Entry entry, int height) {
        addEntry(entry, height);
    }

    /** Two side-by-side card previews, each with a button below. */
    public void addSplitCard(CardSpec leftSpec, AbstractWidget leftBtn,
                             CardSpec rightSpec, AbstractWidget rightBtn) {
        addEntry(new SplitCardEntry(leftSpec, leftBtn, rightSpec, rightBtn),
                ModScreen.CARD_H + ModScreen.ROW_GAP);
    }

    /** One centered card preview with a button below (any button width). */
    public void addSingleCard(CardSpec spec, AbstractWidget btn) {
        addEntry(new SingleCardEntry(spec, btn), ModScreen.CARD_H + ModScreen.ROW_GAP);
    }

    // ── Abstract base entry ────────────────────────────────────────────────────

    public abstract static class Entry extends ContainerObjectSelectionList.Entry<Entry> {}

    // ── Wide button entry ──────────────────────────────────────────────────────

    public static class WideEntry extends Entry {
        private final AbstractWidget widget;

        public WideEntry(AbstractWidget widget) {
            this.widget = widget;
        }

        @Override
        public void extractContent(GuiGraphicsExtractor g, int mx, int my, boolean hovered, float delta) {
            int cx = getX() + getWidth() / 2;
            widget.setPosition(cx - widget.getWidth() / 2,
                    getY() + (getHeight() - widget.getHeight()) / 2);
            widget.extractRenderState(g, mx, my, delta);
        }

        @Override
        public List<? extends GuiEventListener> children() {
            return List.of(widget);
        }

        @Override
        public List<? extends NarratableEntry> narratables() {
            return List.of(widget);
        }
    }

    // ── Split (two-column) button entry ───────────────────────────────────────

    public static class SplitEntry extends Entry {
        private final AbstractWidget left;
        private final AbstractWidget right;

        public SplitEntry(AbstractWidget left, AbstractWidget right) {
            this.left = left;
            this.right = right;
        }

        @Override
        public void extractContent(GuiGraphicsExtractor g, int mx, int my, boolean hovered, float delta) {
            int rowLeft = getX() + getWidth() / 2 - ModScreen.BUTTON_WIDTH / 2;
            int btnY    = getY() + (getHeight() - left.getHeight()) / 2;
            left.setPosition(rowLeft, btnY);
            right.setPosition(rowLeft + ModScreen.CARD_W + ModScreen.COL_GAP, btnY);
            left.extractRenderState(g, mx, my, delta);
            right.extractRenderState(g, mx, my, delta);
        }

        @Override
        public List<? extends GuiEventListener> children() {
            return List.of(left, right);
        }

        @Override
        public List<? extends NarratableEntry> narratables() {
            return List.of(left, right);
        }
    }

    // ── Split card entry (two cards + two buttons) ─────────────────────────────

    public static class SplitCardEntry extends Entry {
        private final CardSpec leftSpec;
        private final AbstractWidget leftBtn;
        private final CardSpec rightSpec;
        private final AbstractWidget rightBtn;

        public SplitCardEntry(CardSpec leftSpec, AbstractWidget leftBtn,
                              CardSpec rightSpec, AbstractWidget rightBtn) {
            this.leftSpec  = leftSpec;
            this.leftBtn   = leftBtn;
            this.rightSpec = rightSpec;
            this.rightBtn  = rightBtn;
        }

        @Override
        public void extractContent(GuiGraphicsExtractor g, int mx, int my, boolean hovered, float delta) {
            int rowLeft = getX() + getWidth() / 2 - ModScreen.BUTTON_WIDTH / 2;
            int rightX  = rowLeft + ModScreen.CARD_W + ModScreen.COL_GAP;
            int cardY   = getY();
            int btnY    = cardY + ModScreen.CARD_PREV_H + ModScreen.CARD_BTN_GAP;

            ModScreen.renderCard(g, rowLeft, cardY, leftSpec.frame());
            ModScreen.renderCard(g, rightX,  cardY, rightSpec.frame());
            drawPauseIndicator(g, mx, my, leftSpec,  rowLeft, cardY);
            drawPauseIndicator(g, mx, my, rightSpec, rightX,  cardY);

            leftBtn.setPosition(rowLeft, btnY);
            rightBtn.setPosition(rightX, btnY);
            leftBtn.extractRenderState(g, mx, my, delta);
            rightBtn.extractRenderState(g, mx, my, delta);
        }

        @Override
        public boolean mouseClicked(MouseButtonEvent event, boolean consumed) {
            if (!consumed && event.button() == 0) {
                int rowLeft = getX() + getWidth() / 2 - ModScreen.BUTTON_WIDTH / 2;
                int rightX  = rowLeft + ModScreen.CARD_W + ModScreen.COL_GAP;
                int cardY   = getY();
                if (isCardClick(event, rowLeft, cardY)) { leftSpec.click();  return true; }
                if (isCardClick(event, rightX,  cardY)) { rightSpec.click(); return true; }
            }
            return super.mouseClicked(event, consumed);
        }

        @Override
        public List<? extends GuiEventListener> children() {
            return List.of(leftBtn, rightBtn);
        }

        @Override
        public List<? extends NarratableEntry> narratables() {
            return List.of(leftBtn, rightBtn);
        }
    }

    // ── Single card entry (one centered card + one button) ─────────────────────

    public static class SingleCardEntry extends Entry {
        private final CardSpec spec;
        private final AbstractWidget btn;

        public SingleCardEntry(CardSpec spec, AbstractWidget btn) {
            this.spec = spec;
            this.btn  = btn;
        }

        @Override
        public void extractContent(GuiGraphicsExtractor g, int mx, int my, boolean hovered, float delta) {
            int cardX = getX() + getWidth() / 2 - ModScreen.CARD_W / 2;
            int cardY = getY();
            int btnY  = cardY + ModScreen.CARD_PREV_H + ModScreen.CARD_BTN_GAP;

            ModScreen.renderCard(g, cardX, cardY, spec.frame());
            drawPauseIndicator(g, mx, my, spec, cardX, cardY);

            btn.setPosition(getX() + getWidth() / 2 - btn.getWidth() / 2, btnY);
            btn.extractRenderState(g, mx, my, delta);
        }

        @Override
        public boolean mouseClicked(MouseButtonEvent event, boolean consumed) {
            if (!consumed && event.button() == 0) {
                int cardX = getX() + getWidth() / 2 - ModScreen.CARD_W / 2;
                if (isCardClick(event, cardX, getY())) { spec.click(); return true; }
            }
            return super.mouseClicked(event, consumed);
        }

        @Override
        public List<? extends GuiEventListener> children() {
            return List.of(btn);
        }

        @Override
        public List<? extends NarratableEntry> narratables() {
            return List.of(btn);
        }
    }

    // ── CardSpec ───────────────────────────────────────────────────────────────

    public abstract static class CardSpec {

        public static CardSpec image(Supplier<Identifier> imageSupplier) {
            return new ImageSpec(imageSupplier);
        }

        public static CardSpec animated(Supplier<Identifier[]> framesSupplier, long frameMs) {
            return new AnimSpec(framesSupplier, frameMs, false);
        }

        public static CardSpec animated(Supplier<Identifier[]> framesSupplier, long frameMs, boolean pausable) {
            return new AnimSpec(framesSupplier, frameMs, pausable);
        }

        /** Returns the identifier to display right now. */
        public abstract Identifier frame();

        public boolean isPausable() { return false; }
        public boolean isPaused()   { return false; }
        public void    click()      {}

        /** Whether to show the pause/play icon at the given mouse position. */
        public boolean showIndicator(int mx, int my, int cardX, int cardY) { return false; }

        // ── ImageSpec ──────────────────────────────────────────────────────────

        private static final class ImageSpec extends CardSpec {
            private final Supplier<Identifier> src;
            ImageSpec(Supplier<Identifier> src) { this.src = src; }
            @Override public Identifier frame() { return src.get(); }
        }

        // ── AnimSpec ───────────────────────────────────────────────────────────

        private static final class AnimSpec extends CardSpec {
            private final Supplier<Identifier[]> src;
            private final long    frameMs;
            private final boolean pausable;
            private boolean paused   = false;
            private long    frozenAt = 0;

            AnimSpec(Supplier<Identifier[]> src, long frameMs, boolean pausable) {
                this.src      = src;
                this.frameMs  = frameMs;
                this.pausable = pausable;
            }

            @Override
            public Identifier frame() {
                Identifier[] frames = src.get();
                long t = paused ? frozenAt : System.currentTimeMillis();
                return frames[(int) ((t / frameMs) % frames.length)];
            }

            @Override public boolean isPausable() { return pausable; }
            @Override public boolean isPaused()   { return paused; }

            @Override
            public void click() {
                if (!pausable) return;
                paused = !paused;
                if (paused) frozenAt = System.currentTimeMillis();
            }

            @Override
            public boolean showIndicator(int mx, int my, int cardX, int cardY) {
                if (!pausable) return false;
                return paused
                    || (mx >= cardX && mx < cardX + ModScreen.CARD_W
                     && my >= cardY && my < cardY + ModScreen.CARD_PREV_H);
            }
        }
    }

    // ── Shared helpers ─────────────────────────────────────────────────────────

    private static boolean isCardClick(MouseButtonEvent event, int cardX, int cardY) {
        return event.x() >= cardX && event.x() < cardX + ModScreen.CARD_W
            && event.y() >= cardY && event.y() < cardY + ModScreen.CARD_PREV_H;
    }

    private static void drawPauseIndicator(GuiGraphicsExtractor g, int mx, int my,
                                           CardSpec spec, int cardX, int cardY) {
        if (!spec.showIndicator(mx, my, cardX, cardY)) return;
        int ix = cardX + ModScreen.CARD_W - 18;
        int iy = cardY + 2;
        g.fill(ix, iy, ix + 16, iy + 16, 0x99000000);
        g.centeredText(Minecraft.getInstance().font,
                Component.literal(spec.isPaused() ? "▶" : "⏸"), ix + 8, iy + 4, 0xFFFFFFFF);
    }
}
