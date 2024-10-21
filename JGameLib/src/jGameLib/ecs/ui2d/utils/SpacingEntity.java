package jGameLib.ecs.ui2d.utils;

import jGameLib.ecs.ui2d.rendering.UIEntity;
import jGameLib.math.Vec2;

public class SpacingEntity extends UIEntity {
    public SpacingEntity(Vec2 size) {
        super(size);
    }

    public SpacingEntity(double size) {
        super(new Vec2(size));
    }
}