package websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    private final Map<Integer, Map<String, Session>> gameConnections = new ConcurrentHashMap<>();

    public void add(int gameId, String authToken, Session session) {
        gameConnections.computeIfAbsent(gameId, k -> new ConcurrentHashMap<>())
                .put(authToken, session);
    }


    public void remove(int gameId, String authToken) {
        Map<String, Session> gameMap = gameConnections.get(gameId);
        if (gameMap != null) {
            gameMap.remove(authToken);
            if (gameMap.isEmpty()) {
                gameConnections.remove(gameId);
            }
        }
    }

    public void broadcast(int gameId, ServerMessage message) throws IOException {
        Map<String, Session> sessions = gameConnections.get(gameId);
        if (sessions != null) {
            for (Session session : sessions.values()) {
                if (session.isOpen()) {
                    session.getRemote().sendString(ServerMessage.toJson(message));
                }
            }
        }
    }

//    public Session getSession(String authToken) {
//        return gameConnections.get(authToken);
//    }


}
