package jGameLib.ecs.ui2d.rendering;

import jGameLib.ecs.Entity;
import jGameLib.math.Vec2;

import java.util.function.Consumer;

@SuppressWarnings("UnusedReturnValue")
public class UIEntity extends Entity implements HasBoundingBox {
    public final BoundingBoxComponent boundingBox;

    public UIEntity() {
        this(Vec2.zero);
    }

    public UIEntity(Vec2 size) {
        this(Vec2.zero, size);
    }

    public UIEntity(Vec2 position, Vec2 size) {
        super();
        addComponent(boundingBox = new BoundingBoxComponent(position, size));
    }

    @Override
    public final BoundingBoxComponent getBoundingBox() {
        return boundingBox;
    }

    @Override
    public final UIEntity withBoundingBox(Consumer<BoundingBoxComponent> action) {
        return (UIEntity) HasBoundingBox.super.withBoundingBox(action);
    }
}
