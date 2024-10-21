package jGameLib.ecs;

/**
 * A {@link GameState} that executes a single action. No entities should be made in the game state
 */
public abstract class InstantaneousGameState extends GameState {
    @Override
    protected final void onUpdate() {
    }

    @Override
    protected final void onSchedule() {
    }

    @Override
    protected final void onExecutionEnd() {
    }

    @Override
    protected final void onExecutionStart() {
        execute();
    }

    @Override
    protected final boolean isFinished() {
        return true;
    }

    protected abstract void execute();
}