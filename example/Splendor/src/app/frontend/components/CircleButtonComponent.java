package app.frontend.components;

import jGameLib.ecs.ui2d.utils.HoverDetectionComponent;
import jGameLib.math.Vec2;

/**
 * ButtonComponent but culled to a hexagon shape
 */
public class CircleButtonComponent extends HoverDetectionComponent {
    public CircleButtonComponent() {
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