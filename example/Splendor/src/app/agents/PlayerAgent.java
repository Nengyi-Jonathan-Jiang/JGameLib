package app.agents;

import app.backend.Game;
import app.backend.Player;
import app.backend.PlayerTurnAction;
import jGameLib.core.GameState;

public abstract class PlayerAgent {
    public final Player player;

    protected PlayerAgent(String name) {
        this.player = new Player(name);
    }

    public abstract GameState getState();

    public abstract PlayerTurnAction getAction(Game game);
}
