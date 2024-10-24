package jGameLib.network;

import jGameLib.util.Pair;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

public class TurnBasedGameServer<Action extends TurnBasedGameAction, GameData extends TurnBasedGameState<Action>>
    extends TurnBasedGameConnection<Action, GameData> {
    private final WebSocketServer server;
    private GameData state;

    public TurnBasedGameServer(@NotNull GameData initialState) {
        this.state = initialState;
        server = new WebSocketServer(new InetSocketAddress(255)) {
            @Override
            public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
                var afterPlayerJoined = state.onPlayerJoin();
                if(afterPlayerJoined == null) {
                    webSocket.close();
                    return;
                }

                webSocket.send(serialize(new Pair<Action, GameData>(null, state)));
            }

            @Override
            public void onClose(WebSocket webSocket, int i, String s, boolean b) {
            }

            @Override
            public void onMessage(WebSocket webSocket, String s) {
            }

            @Override
            public void onMessage(WebSocket conn, ByteBuffer message) {
                onAction(deserialize(message.array()));
            }

            @Override
            public void onError(WebSocket webSocket, Exception e) {
            }

            @Override
            public void onStart() {
            }
        };
    }

    @Override
    public @Nullable GameData getState() {
        return state;
    }

    @Override
    public void sendAction(@NotNull Action action) {
        onAction(action);
    }

    private void onAction(Action action) {
        // Update own state
        state = state.onAction(action).cast();
        // Broadcast state update to clients.
        server.broadcast(serialize(new Pair<>(action, state)));
        // Run update listeners on self
        runUpdateListeners(action, state);
    }
}
