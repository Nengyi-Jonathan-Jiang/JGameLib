package jGameLib.ui2d.utils;

import jGameLib.ui2d.rendering.BoundingBoxComponent;
import jGameLib.ui2d.rendering.UIRendererComponent;
import jGameLib.util.math.Vec2;

@SuppressWarnings("unused")
public class PositionAnimationComponent extends UIRendererComponent {
    private Vec2 startPosition;
    private Vec2 targetPosition;
    private double elapsedTime;
    private double animationDuration;

    @Override
    public void update() {
        if (elapsedTime >= animationDuration) return;
        elapsedTime += 1;

        Vec2 fullDistance = targetPosition.minus(startPosition);
        Vec2 progress = fullDistance.times(elapsedTime / animationDuration);

        entity.getComponent(BoundingBoxComponent.class).setRelativePosition(startPosition.plus(progress));
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