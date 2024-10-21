package jGameLib.ecs.ui2d.utils;

import jGameLib.ecs.Component;
import jGameLib.ecs.Entity;
import jGameLib.ecs.ui2d.input.UserInputState;
import jGameLib.ecs.ui2d.rendering.BoundingBoxComponent;
import jGameLib.math.Vec2;
import org.jetbrains.annotations.NotNull;

/**
 * A component that detects whether a given GameObject is currently hovered
 */
public class HoverDetectionComponent extends Component {
    @SuppressWarnings("DataFlowIssue")
    protected @NotNull BoundingBoxComponent boundingBox = null;

    public HoverDetectionComponent() {
    }

    @Override
    protected void onSetEntity(Entity entity) {
        boundingBox = entity.getComponent(BoundingBoxComponent.class);
    }

    /**
     * @param v The position in screen space of the mouse
     * @return Whether the given position is over the ecs.GameObject
     */
    public boolean contains(Vec2 v) {
        Vec2 offsetFromCenter = v.minus(boundingBox.getAbsolutePosition()).transform(Math::abs);
        Vec2 size = boundingBox.getSize();
        return offsetFromCenter.x * 2 <= size.x && offsetFromCenter.y * 2 <= size.y;
    }

    /**
     * @return Whether the current mouse position is over the ecs.GameObject
     */
    public final boolean isHovered(UserInputState userInputState) {
        return contains(userInputState.getMousePosition());
    }
}