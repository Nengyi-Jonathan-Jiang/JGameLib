package jGameLib.network;

import jGameLib.util.Pair;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.URI;
import java.nio.ByteBuffer;

public class TurnBasedGameClient<Action extends TurnBasedGameAction, GameData extends TurnBasedGameState<Action>>
    extends TurnBasedGameConnection<Action, GameData> {
    private GameData gameData;
    private final WebSocketClient client;

    public TurnBasedGameClient(URI serverURI) {
        client = new WebSocketClient(serverURI) {

            @Override
            public void onOpen(ServerHandshake serverHandshake) {

            }

            @Override
            public void onMessage(String s) {

            }

            @Override
            public void onMessage(ByteBuffer bytes) {
                Pair<Action, GameData> data = deserialize(bytes.array());
                applyUpdate(data.a(), data.b());
            }

            @Override
            public void onClose(int i, String s, boolean b) {

            }

            @Override
            public void onError(Exception e) {

            }
        };
    }

    @Override
    public @Nullable GameData getState() {
        return gameData;
    }

    @Override
    public void sendAction(@NotNull Action action) {
        // Update own state
        applyUpdate(action, gameData.onAction(action).cast());
        // Send action to server
        client.send(serialize(action));
    }

    private void applyUpdate(Action action, GameData state) {
        gameData = state;
        runUpdateListeners(action, state);
    }
}
