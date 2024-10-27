package jGameLib.ui2d.rendering;

import jGameLib.core.Component;
import jGameLib.core.Entity;
import jGameLib.ui2d.JGraphics;

import java.util.function.Consumer;

public abstract class UIRendererComponent extends Component implements HasBoundingBox {
    private boolean isEnabled = true;
    protected BoundingBoxComponent boundingBox;

    public boolean isEnabled() {
        return isEnabled;
    }

    public void enable() {
        this.isEnabled = true;
    }

    public void disable() {
        this.isEnabled = false;
    }

    @Override
    protected void onSetEntity(Entity entity) {
        boundingBox = entity.getComponent(BoundingBoxComponent.class);
    }

    /**
     * Can be overridden.
     */
    public void update() {

    }

    /**
     * Should be overridden.
     */
    public void draw(JGraphics graphics) {

    }

    @Override
    public final BoundingBoxComponent getBoundingBox() {
        return boundingBox;
    }

    @Override
    public UIRendererComponent withBoundingBox(Consumer<BoundingBoxComponent> action) {
        return (UIRendererComponent) HasBoundingBox.super.withBoundingBox(action);
    }
}
