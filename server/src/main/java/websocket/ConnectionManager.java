package websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.Iterator;
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
            // Iterate with iterator so we can remove closed sessions safely
            Iterator<Map.Entry<String, Session>> it = sessions.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, Session> entry = it.next();
                Session session = entry.getValue();
                if (session.isOpen()) {
                    session.getRemote().sendString(ServerMessage.toJson(message));
                } else {
                    // Remove dead session from the map
                    it.remove();
                }
            }
            // If after removal map is empty, remove the whole gameId key
            if (sessions.isEmpty()) {
                gameConnections.remove(gameId);
            }
        }
    }
    public void broadcastExcept(int gameID, String excludedAuthToken, ServerMessage message) throws IOException {
        var sessions = gameConnections.get(gameID);
        if (sessions != null) {
            for (var entry : sessions.entrySet()) {
                String authToken = entry.getKey();
                Session session = entry.getValue();
                if (!authToken.equals(excludedAuthToken) && session.isOpen()) {
                    session.getRemote().sendString(new Gson().toJson(message));
                }
            }
        }
    }

    public void send(int gameId, String authToken, ServerMessage message) throws IOException {
        Map<String, Session> gameMap = gameConnections.get(gameId);
        if (gameMap != null) {
            Session session = gameMap.get(authToken);
            if (session != null && session.isOpen()) {
                session.getRemote().sendString(ServerMessage.toJson(message));
            }
        }
    }
}
