package jGameLib.ecs.ui2d.input;

import jGameLib.ecs.Entity;
import jGameLib.ecs.ui2d.rendering.BoundingBoxComponent;

import java.util.function.Consumer;

public final class UserInputHandlerEntity extends Entity {
    public UserInputHandlerEntity(UserInputHandlerComponent component) {
        addComponent(component);
    }

    public BoundingBoxComponent getBoundingBox() {
        return this.getComponent(BoundingBoxComponent.class);
    }

    public Entity withBoundingBox(Consumer<BoundingBoxComponent> action) {
        action.accept(getBoundingBox());
        return this;
    }
}
