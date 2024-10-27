package jGameLib.network;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;

public interface TurnBasedGameState<Action extends TurnBasedGameAction> extends Serializable {
    /**
     * Updates the game state on an action. If the action is invalid, this should do nothing
     */
    @NotNull TurnBasedGameState<Action> onAction(@NotNull Action action);

    /**
     * Updates the game state on player joining. If players are not allowed to join, this should return
     * null
     */
    @Nullable TurnBasedGameState<Action> onPlayerJoin();

    default <T extends TurnBasedGameState<Action>> T cast() {
        //noinspection unchecked
        return (T) this;
    }
}