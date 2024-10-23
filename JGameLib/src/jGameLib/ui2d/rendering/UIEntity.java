package jGameLib.ui2d.rendering;

import jGameLib.core.Entity;
import jGameLib.core.GameState;
import jGameLib.util.math.Vec2;

import java.util.function.Consumer;

@SuppressWarnings("UnusedReturnValue")
public class UIEntity extends Entity implements HasBoundingBox {
    public final BoundingBoxComponent boundingBox;

    public UIEntity(GameState state) {
        this(state, Vec2.zero);
    }

    public UIEntity(GameState state, Vec2 size) {
        this(state, size, Vec2.zero);
    }

    public UIEntity(GameState state, Vec2 size, Vec2 position) {
        super(state);
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
