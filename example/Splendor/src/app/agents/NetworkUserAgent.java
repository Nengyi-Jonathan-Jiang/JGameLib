package app.agents;

import app.backend.Game;
import app.backend.PlayerTurnAction;
import jGameLib.ecs.GameState;

// TODO
public class NetworkUserAgent extends PlayerAgent {
    public NetworkUserAgent(String name) {
        super(name);
    }

    @Override
    public GameState getState() {
        return null;
    }

    @Override
    public PlayerTurnAction getAction(Game game) {
        return new PlayerTurnAction.Pass();
    }
}
