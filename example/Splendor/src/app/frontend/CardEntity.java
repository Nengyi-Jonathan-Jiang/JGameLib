package app.frontend;

import jGameLib.ecs.Entity;
import jGameLib.ecs.ui2d.rendering.BoundingBoxComponent;

import java.util.function.Consumer;

public class CardEntity extends Entity {
    public final BoundingBoxComponent getBoundingBox() {
        return this.getComponent(BoundingBoxComponent.class);
    }

    public final Entity withBoundingBox(Consumer<BoundingBoxComponent> action) {
        action.accept(getBoundingBox());
        return this;
    }
}
