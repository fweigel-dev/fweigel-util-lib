package dev.fweigel.mobutils.core.client.ui;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

import java.util.function.Supplier;

/**
 * Base class for mod settings screens.
 * Provides the standard layout (title at top, scrollable list, Done button at bottom).
 * Subclasses only need to implement {@link #addOptions(ModOptionsList)}.
 */
public abstract class ModSettingsScreen extends ModScreen {

    private static final int TITLE_Y      = 8;
    private static final int LIST_TOP     = 32;
    private static final int DONE_RESERVE = 36;

    protected ModSettingsScreen(Component title) {
        super(title);
    }

    /** Add all settings entries to the list. Called once per {@link #init()}. */
    protected abstract void addOptions(ModOptionsList list);

    @Override
    protected void init() {
        int listHeight = this.height - LIST_TOP - DONE_RESERVE;
        ModOptionsList list = new ModOptionsList(this.minecraft, this.width, LIST_TOP, listHeight);
        addOptions(list);
        addRenderableWidget(list);
        addRenderableWidget(Button.builder(
                Component.translatable("gui.done"), b -> this.onClose()
        ).bounds(this.width / 2 - BUTTON_WIDTH / 2, this.height - DONE_RESERVE + 8,
                 BUTTON_WIDTH, BUTTON_HEIGHT).build());
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
        super.extractRenderState(graphics, mouseX, mouseY, delta);
        graphics.centeredText(this.font, this.title, this.width / 2, TITLE_Y, 0xFFFFFFFF);
    }

    // ── Button builder helpers ─────────────────────────────────────────────────

    /** Builds a self-refreshing half-width (96 px) button. */
    protected Button buildHalfButton(Supplier<Component> label, Runnable action) {
        Button[] ref = new Button[1];
        ref[0] = Button.builder(label.get(), b -> {
            action.run();
            ref[0].setMessage(label.get());
        }).bounds(0, 0, CARD_W, BUTTON_HEIGHT).build();
        return ref[0];
    }

    /** Builds a self-refreshing full-width (200 px) button. */
    protected Button buildWideButton(Supplier<Component> label, Runnable action) {
        Button[] ref = new Button[1];
        ref[0] = Button.builder(label.get(), b -> {
            action.run();
            ref[0].setMessage(label.get());
        }).bounds(0, 0, BUTTON_WIDTH, BUTTON_HEIGHT).build();
        return ref[0];
    }
}
