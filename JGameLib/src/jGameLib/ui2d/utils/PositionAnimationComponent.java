package jGameLib.ui2d.utils;

import jGameLib.ui2d.rendering.BoundingBoxComponent;
import jGameLib.ui2d.rendering.UIRendererComponent;
import jGameLib.util.math.Vec2;

@SuppressWarnings("unused")
public class PositionAnimationComponent extends UIRendererComponent {
    private Vec2 startPosition;
    private Vec2 targetPosition;
    private double elapsedTime = 0;
    private double animationDuration = 0;

    @Override
    public void update() {
        if (!isAnimating()) return;
        elapsedTime += 1;

        double t = elapsedTime / animationDuration;
        entity.getComponent(BoundingBoxComponent.class).setRelativePosition(
            Vec2.smoothInterpolate(
                startPosition,
                targetPosition,
                t
            )
        );
    }

    public boolean isAnimating() {
        return elapsedTime < animationDuration;
    }

    public void moveTo(Vec2 targetPosition, double animationDurationInFrames) {
        startPosition = this.entity.getComponent(BoundingBoxComponent.class).getRelativePosition();
        this.targetPosition = targetPosition;
        this.animationDuration = animationDurationInFrames;
        elapsedTime = 0;
    }

    public void stopAnimation() {
        startPosition = targetPosition = entity.getComponent(BoundingBoxComponent.class).getRelativePosition();
    }
}