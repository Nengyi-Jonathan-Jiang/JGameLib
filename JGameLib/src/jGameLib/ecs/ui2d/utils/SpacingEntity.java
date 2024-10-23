package jGameLib.ecs.ui2d.utils;

import jGameLib.ecs.GameState;
import jGameLib.ecs.ui2d.rendering.UIEntity;
import jGameLib.math.Vec2;

public class SpacingEntity extends UIEntity {
    public SpacingEntity(GameState state, Vec2 size) {
        super(state, size);
    }

    public SpacingEntity(GameState state, double size) {
        super(state, new Vec2(size));
    }
}