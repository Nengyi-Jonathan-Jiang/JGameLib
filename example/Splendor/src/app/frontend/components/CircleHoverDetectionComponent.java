package app.frontend.components;

import jGameLib.ui2d.utils.HoverDetectionComponent;
import jGameLib.util.math.Vec2;

/**
 * ButtonComponent but culled to a circle shape
 */
public class CircleHoverDetectionComponent extends HoverDetectionComponent {
    public CircleHoverDetectionComponent() {
    }

    @Override
    public boolean contains(Vec2 v) {
        Vec2 d = v
            .minus(boundingBox.getAbsolutePosition())
            .transform(Math::abs)
            .times(
                boundingBox.getSize()
                    .transform(i -> 1 / i)
            );
        return d.magnitude() <= 0.5;
    }
}