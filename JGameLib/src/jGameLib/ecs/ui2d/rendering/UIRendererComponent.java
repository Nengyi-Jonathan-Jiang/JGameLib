package jGameLib.ecs.ui2d.rendering;

import jGameLib.ecs.Component;
import jGameLib.ecs.Entity;
import jGameLib.ecs.ui2d.GameCanvas;
import jGameLib.ecs.ui2d.JGraphics;

public abstract class UIRendererComponent extends Component {
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
}
