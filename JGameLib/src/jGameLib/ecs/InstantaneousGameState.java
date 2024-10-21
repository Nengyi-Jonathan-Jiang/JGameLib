package jGameLib.ecs;

/**
 * Extend this class when your intention is to make an instant state executing a single action as its body
 */
public abstract class InstantaneousGameState extends GameState {
    @Override
    public final void onUpdate() {
    }

    @Override
    public final void onExecutionEnd() {
    }

    @Override
    public final void onExecutionStart() {
        execute();
    }

    @Override
    public final boolean isFinished() {
        return true;
    }

    public abstract void execute();
}