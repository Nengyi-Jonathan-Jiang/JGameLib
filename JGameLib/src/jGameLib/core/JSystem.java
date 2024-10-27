package jGameLib.core;

import java.util.Collection;

public abstract class JSystem {
    protected abstract boolean canActOn(Entity e);

    protected abstract void applyAction(Collection<Entity> e, GameState currentState);

    protected void onStateChanged(Collection<Entity> entities, GameState newState) {
    }
}
