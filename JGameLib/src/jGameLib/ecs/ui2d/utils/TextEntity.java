package jGameLib.ecs.ui2d.utils;

import jGameLib.ecs.Entity;
import jGameLib.ecs.ui2d.rendering.BoundingBoxComponent;

import java.util.function.Consumer;

public class TextEntity extends Entity {
    private final TextRendererBehavior textRenderer;

    public TextEntity(String text, TextStyle textStyle) {
        addComponent(textRenderer = new TextRendererBehavior(text, textStyle));
    }

    public TextEntity resizeToFitH(double padding) {
        textRenderer.resizeWidthToFit(padding);
        return this;
    }

    public TextEntity resizeToFitV(double padding) {
        textRenderer.resizeHeightToFit(padding);
        return this;
    }

    public TextEntity resizeToFit(int padding) {
        textRenderer.resizeToFit(padding);
        return this;
    }

    public TextEntity setUnderlined(boolean underline) {
        textRenderer.setUnderlined(underline);
        return this;
    }

    public TextEntity setText(String text) {
        textRenderer.setText(text);
        return this;
    }

    public final BoundingBoxComponent getBoundingBox() {
        return this.getComponent(BoundingBoxComponent.class);
    }

    public final Entity withBoundingBox(Consumer<BoundingBoxComponent> action) {
        action.accept(getBoundingBox());
        return this;
    }
}
