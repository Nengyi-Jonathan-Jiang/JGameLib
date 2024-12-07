package jGameLib.core;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * An abstract class representing a game state in a {@link StateMachine StateMachine}
 */
@SuppressWarnings("unused")
public abstract class GameState {
    private final Set<Entity> entitiesInState = new LinkedHashSet<>();
    private final List<JSystem> systems = new ArrayList<>();

    void addEntity(Entity entity) {
        entitiesInState.add(entity);
    }

    void removeEntity(Entity entity) {
        entitiesInState.remove(entity);
    }

    Collection<Entity> getEntitiesInState() {
        return Collections.unmodifiableCollection(entitiesInState);
    }

    public Stream<Entity> queryEntitiesInState(Predicate<Entity> predicate) {
        return getEntitiesInState()
            .stream()
            .filter(predicate);
    }

    Collection<JSystem> getSystems() {
        return Collections.unmodifiableCollection(systems);
    }

    /**
     * Constructs a {@link GameState}
     */
    protected GameState() {
        StateMachine.gameStateWithControl = this;
    }

    /**
     * Adds a {@link JSystem} specific to this state to be run in the game loop
     */
    protected void addSystem(JSystem system) {
        this.systems.add(system);
    }

    /**
     * A utility method to create an iterator over a list of {@link GameState GameStates}
     */
    public static Iterator<GameState> iteratorOver(GameState... states) {
        return Arrays.stream(states).iterator();
    }

    /**
     * A utility method to convert a {@link Collection} into an iterator over {@link GameState GameStates} using the
     * given mapping function
     */
    public static <T> Iterator<GameState> iteratorOver(Collection<T> list, Function<T, GameState> func) {
        return list.stream().map(func).iterator();
    }

    /**
     * A utility method to make a while-loop-like iterator over {@link GameState GameStates}
     *
     * @param gameStateSupplier A function with zero parameters that returns a state, to be called repeatedly in the
     *                          loop
     * @param condition         The condition of the while loop, which should return true as long as the loop should
     *                          execute
     */
    public static Iterator<GameState> makeWhileLoopIterator(Supplier<GameState> gameStateSupplier, Supplier<Boolean> condition) {
        return new Iterator<>() {
            @Override
            public boolean hasNext() {
                return condition.get();
            }

            @Override
            public GameState next() {
                return gameStateSupplier.get();
            }
        };
    }

    /**
     * A utility method to join a list of {@link Iterator Iterators} over {@link GameState GameStates} into a
     * single iterator
     */
    @SafeVarargs
    public static Iterator<GameState> joinIterators(Iterator<? extends GameState>... its) {
        return new Iterator<>() {
            private final Iterator<Iterator<? extends GameState>> ii = List.of(its).iterator();
            private Iterator<? extends GameState> current = null;

            @Override
            public boolean hasNext() {
                while (current == null || !current.hasNext()) {
                    if (!ii.hasNext()) return false;
                    current = ii.next();
                }
                return true;
            }

            @Override
            public GameState next() {
                return current.next();
            }
        };
    }

    /**
     * A utility method to convert a method that returns an {@link Iterator} over {@link GameState GameStates} into a
     * single state
     */
    public static GameState toGameState(Supplier<Iterator<GameState>> gameStateIterator) {
        return new GameState() {
            @Override
            public Iterator<? extends GameState> getStatesBefore() {
                return gameStateIterator.get();
            }
        };
    }

    /**
     * A utility method to creates a single {@link GameState} out of a list of states
     */
    public static GameState groupStates(GameState... states) {
        return new GameState() {
            @Override
            public Iterator<? extends GameState> getStatesBefore() {
                return iteratorOver(states);
            }
        };
    }

    /**
     * Should probably be overridden. Returns an {@link Iterator} of {@link GameState GameStates} that should be run
     * before this state executes, may return an indefinite number of states. This is queried immediately after the
     * {@link GameState#onSchedule()} method is called.
     */
    protected Iterator<? extends GameState> getStatesBefore() {
        return null;
    }

    /**
     * Should probably be overridden. Returns an {@link Iterator} of {@link GameState GameStates} that should be run
     * after this state executes, may return an indefinite number of states. This is queried immediately after the
     * {@link GameState#onExecutionEnd()} method is called.
     */
    protected Iterator<? extends GameState> getStatesAfter() {
        return null;
    }

    /**
     * Should probably be overridden. Called every frame before {@link JSystem JSystems} are run. Does nothing by
     * default.
     */
    protected void onUpdate() {
    }

    /**
     * Can be overridden. Called as soon as the {@link GameState} is scheduled. Any initialization up not done in the
     * constructor should happen here. Does nothing by default.
     */
    protected void onSchedule() {
    }

    /**
     * Can be overridden. Called before the first time {@link JSystem JSystems} and {@link GameState#onUpdate()} are
     * run. Does nothing by default.
     */
    protected void onExecutionStart() {
    }

    /**
     * Can be overridden. Called immediately after the {@link GameState#isFinished()} returns true. Does nothing by
     * default.
     */
    protected void onExecutionEnd() {
    }

    /**
     * Can be overridden. Returns whether the {@link GameState} has finished executing. When this returns true, the
     * {@link StateMachine} will not stop executing the state without running {@link JSystem JSystems} or
     * {@link GameState#onUpdate()}. This method returns false by default, meaning that the state will never finish
     */
    protected boolean isFinished() {
        return false;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() +
            "{" +
            ToStringHelper.toMultiLineList(
                "Entities: [" +
                    ToStringHelper.toMultiLineList(
                        entitiesInState.stream()
                    ) +
                    "]"
            ) +
            "}";
    }

    @SuppressWarnings("unchecked")
    public <T extends GameState> T cast(Class<T> cls) {
        return (T) this;
    }
}