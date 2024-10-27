package jGameLib.core;

import java.util.*;

public final class StateMachine {
    private final Stack<Iterator<? extends GameState>> scheduleStack;
    private final Stack<GameState> stateStack;
    private final double delay;
    private final List<JSystem> systems = new ArrayList<>();
    private GameState currentState;

    static final Object gameStateWithControlSynchronizer = new Object();
    static GameState gameStateWithControl = null;

    public static final StateMachine globalInstance = new StateMachine(60);

    public StateMachine(double fps) {
        stateStack = new Stack<>();
        scheduleStack = new Stack<>();

        this.delay = 1000 / fps;
    }

    /**
     * Add a {@link JSystem} to be run on all {@link GameState GameStates} executed
     * by this state machine.
     */
    public void addSystem(JSystem system) {
        this.systems.add(system);
    }

    /**
     * Check whether the state machine is currently running
     */
    public boolean isRunning() {
        return !stateStack.isEmpty();
    }

    /**
     * Runs the state machine with the given {@link GameState}. If the state machine is already
     * running, this will throw a runtime exception
     */
    public void run(GameState a) {
        if (isRunning()) {
            throw new RuntimeException("Cannot start state machine while already running");
        }

        scheduleState(new GameState() {
            @Override
            public Iterator<GameState> getStatesBefore() {
                return Collections.singletonList(a).iterator();
            }
        });

        new Thread(() -> {
            while (true) {
                if (stateStack.isEmpty()) break;

                GameState currentGameState = stateStack.peek();
                checkExecutionStart(currentGameState);

                if (currentGameState.isFinished()) {
                    // Finish this action
                    currentGameState.onExecutionEnd();
                    // Remove action from stack
                    stateStack.pop();

                    Iterator<? extends GameState> statesAfter = currentGameState.getStatesAfter();
                    if (statesAfter != null && statesAfter.hasNext()) {
                        scheduleState(new GameState() {
                            @Override
                            public Iterator<? extends GameState> getStatesBefore() {
                                return statesAfter;
                            }
                        });
                    } else {
                        // No more actions to execute, quit the event loop
                        if (scheduleStack.isEmpty()) break;
                            // Get the next action
                        else scheduleNextState();
                    }
                } else {
                    executeState(currentGameState);

                    try {
                        //noinspection BusyWait
                        Thread.sleep((long) delay);
                    } catch (Exception e) {/*Nothing*/}
                }
            }
        }).start();
    }

    private void scheduleState(GameState state) {
        stateStack.push(state);

        state.onSchedule();

        Iterator<? extends GameState> statesBefore = state.getStatesBefore();
        if (statesBefore != null && statesBefore.hasNext()) {
            scheduleStack.push(statesBefore);
            scheduleState(statesBefore.next());
        }
    }

    private void scheduleNextState() {
        if (scheduleStack.isEmpty() || stateStack.isEmpty())
            throw new Error("Pop off schedule stack when empty");   //hopefully never happens

        Iterator<? extends GameState> schedule = scheduleStack.peek();

        if (schedule != null && schedule.hasNext()) {
            GameState nextGameState = schedule.next();
            scheduleState(nextGameState);
        } else {
            scheduleStack.pop();
        }
    }

    private void checkExecutionStart(GameState state) {
        if (state != currentState) {
            state.onExecutionStart();
            currentState = state;
        }
    }

    private void executeState(GameState state) {
        state.onUpdate();
        runSystemsOnState(state);
    }

    private void runSystemsOnState(GameState state) {
        for (JSystem system : systems) {
            runSystemOnState(state, system);
        }
        for (JSystem system : state.getSystems()) {
            runSystemOnState(state, system);
        }
    }

    private void runSystemOnState(GameState state, JSystem system) {
        List<Entity> entities = state.queryEntitiesInState(system::canActOn).toList();

        system.applyAction(
            entities,
            state
        );
    }
}