package app.states;

import jGameLib.ecs.GameState;

import java.util.Iterator;

public abstract class BasicState extends GameState {
    private GameState nextState = null;

    protected void setNextState(GameState nextState) {
        this.nextState = nextState;
    }

    @Override
    public final boolean isFinished() {
        return nextState != null;
    }

    @Override
    public final Iterator<? extends GameState> getStatesAfter() {
        var n = nextState;
        nextState = null;
        return iteratorOver(n);
    }
}