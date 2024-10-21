package jGameLib.ecs;

import java.util.Collection;

public abstract class JSystem {
    public abstract boolean canActOn(Entity e);

    public abstract void applyAction(Collection<Entity> e, GameState currentState);
}
