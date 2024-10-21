package app.frontend;

import app.backend.Gem;
import app.frontend.components.CircleButtonComponent;
import app.frontend.components.CircleRendererBehavior;
import jGameLib.ecs.Entity;
import jGameLib.ecs.ui2d.rendering.BoundingBoxComponent;
import jGameLib.ecs.ui2d.utils.PositionAnimationComponent;

import java.awt.*;
import java.util.function.Consumer;

public class GemEntity extends Entity {
    public GemEntity(Gem gem) {
        addComponent(new CircleRendererBehavior(Color.BLACK, switch (gem) {
            case RED -> Color.RED;
            case GREEN -> Color.GREEN;
            case BLUE -> Color.BLUE;
            case BLACK -> Color.BLACK;
            case WHITE -> Color.WHITE;
        }));
        addComponent(new CircleButtonComponent());
        addComponent(new PositionAnimationComponent());
        getBoundingBox().setSize(100, 100);
    }

    public final BoundingBoxComponent getBoundingBox() {
        return this.getComponent(BoundingBoxComponent.class);
    }

    public final Entity withBoundingBox(Consumer<BoundingBoxComponent> action) {
        action.accept(getBoundingBox());
        return this;
    }
}