package jGameLib.ui2d.utils;

import jGameLib.core.Component;
import jGameLib.core.Entity;
import jGameLib.ui2d.input.UserInputState;
import jGameLib.ui2d.rendering.BoundingBoxComponent;
import jGameLib.util.math.Vec2;
import org.jetbrains.annotations.NotNull;

/**
 * A component that detects whether a given GameObject is currently hovered
 */
public class HoverDetectionComponent extends Component {
    @SuppressWarnings("DataFlowIssue")
    protected @NotNull BoundingBoxComponent boundingBox = null;
    protected boolean isEnabled;

    public boolean isEnabled() {
        return isEnabled;
    }

    public void enable() {
        isEnabled = true;
    }

    public void disable() {
        isEnabled = false;
    }

    public HoverDetectionComponent() {
    }

    @Override
    protected void onSetEntity(Entity entity) {
        assertUniqueInEntity(HoverDetectionComponent.class);
        boundingBox = assertEntityHasComponent(BoundingBoxComponent.class);
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
        return isEnabled && contains(userInputState.getMousePosition());
    }
}