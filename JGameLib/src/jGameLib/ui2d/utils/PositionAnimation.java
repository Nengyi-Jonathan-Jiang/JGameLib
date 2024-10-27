package jGameLib.ui2d.utils;

import jGameLib.core.Entity;
import jGameLib.ui2d.rendering.BoundingBoxComponent;
import jGameLib.animation.Animation;
import jGameLib.util.math.Vec2;

public class PositionAnimation extends Animation<Entity> {
    private Vec2 startPosition;
    private final Vec2 endPosition;
    private final boolean useAbsoluteEndPosition;

    public PositionAnimation(Vec2 endPosition, int duration, boolean useAbsoluteEndPosition) {
        super(duration);
        this.endPosition = endPosition;
        this.useAbsoluteEndPosition = useAbsoluteEndPosition;
    }

    @Override
    public void onAnimationStart(Entity entity) {
        BoundingBoxComponent boundingBox = entity.getComponent(BoundingBoxComponent.class);

        if (useAbsoluteEndPosition) {
            startPosition = boundingBox.getAbsolutePosition();
        } else {
            startPosition = boundingBox.getRelativePosition();
        }
    }

    @Override
    public void updateAnimation(Entity entity, double progress) {
        setEntityPosition(
            entity.getComponent(BoundingBoxComponent.class),
            Vec2.smoothInterpolate(
                startPosition,
                endPosition,
                progress
            )
        );
    }

    private void setEntityPosition(BoundingBoxComponent boundingBoxComponent, Vec2 animationPosition) {
        if (useAbsoluteEndPosition) {
            boundingBoxComponent.setAbsolutePosition(animationPosition);
        } else {
            boundingBoxComponent.setRelativePosition(animationPosition);
        }
    }

    @Override
    public void onAnimationEnd(Entity entity) {
        setEntityPosition(entity.getComponent(BoundingBoxComponent.class), endPosition);
    }
}
