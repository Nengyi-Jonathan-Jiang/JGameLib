package jGameLib.network;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.HashSet;
import java.util.concurrent.atomic.AtomicReference;

public abstract class TurnBasedGameConnection<Action extends TurnBasedGameAction, GameState extends TurnBasedGameState<Action>> {
    public interface UpdateListenerRemover {
        void run();
    }

    public interface UpdateListenerSupplier<Action, GameState> {
        UpdateListener<Action, GameState> get(UpdateListenerRemover remover);
    }

    public interface UpdateListener<Action, GameState> {
        void run(@Nullable Action action, @NotNull GameState newState);
    }

    /**
     * Get the current state. This may be null if the state is unknown
     */
    public abstract @Nullable GameState getState();

    /**
     * Requests an action to be applied to the current state.
     */
    public abstract void sendAction(@NotNull Action action);

    /**
     * <p>
     * Adds a listener to the connection.
     * </p>
     * <p>
     * Usage:
     * </p>
     * <blockquote><pre>
     * Runnable fooRemover = connection.addListener(remover -> (action, newState) -> {
     *     doFoo();
     *
     *     if(shouldNotDoFooAnymore()) {
     *         // We can remove the listener from inside the listener
     *         remover.run();
     *     }
     * });
     *
     * ...
     *
     * // Or we can remove the listener from outside the listener
     * fooRemover.run();
     * </pre></blockquote>
     * <p>
     * Note that action can be null if no meaningful action could be given (for example, on player join or on
     * initial connection). The listener should handle this case correctly.
     * </p>
     */
    public final UpdateListenerRemover addUpdateListener(
        @NotNull UpdateListenerSupplier<Action, GameState> listenerSupplier
    ) {
        var removerRef = new AtomicReference<UpdateListenerRemover>();
        var listener = listenerSupplier.get(() -> removerRef.get().run());
        listeners.add(listener);
        UpdateListenerRemover remover = () -> listeners.remove(listener);
        removerRef.set(remover);
        return remover;
    }

    private final HashSet<UpdateListener<Action, GameState>> listeners = new HashSet<>();

    protected final void runUpdateListeners(Action action, GameState newState) {
        listeners.forEach(a -> a.run(action, newState));
    }

    protected static <T> byte[] serialize(T object) {
        try {
            var b = new ByteArrayOutputStream();
            var o = new ObjectOutputStream(b);
            o.writeObject(object);
            return b.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected static <T> T deserialize(byte[] bytes) {
        try {
            var b = new ByteArrayInputStream(bytes);
            var o = new ObjectInputStream(b);
            //noinspection unchecked
            return (T) o.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
