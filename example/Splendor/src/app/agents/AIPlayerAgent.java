package app.agents;

import app.backend.Game;
import app.backend.PlayerTurnAction;
import jGameLib.core.GameState;

// TODO
public class AIPlayerAgent extends PlayerAgent {
    public AIPlayerAgent(String name) {
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
