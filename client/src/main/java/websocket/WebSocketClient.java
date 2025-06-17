package websocket;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;
import websocket.commands.UserGameCommand;
import websocket.NotificationHandler;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@ClientEndpoint
public class WebSocketClient {

    private Session session;
    private final Gson gson = new Gson();
    private final NotificationHandler handler;

    public WebSocketClient(String serverUrl, NotificationHandler handler) {
        this.handler = handler;
        try {
            URI uri = new URI(serverUrl.replace("http", "ws") + "/ws");
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(this, uri);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("WebSocket connection failed: " + e.getMessage());
        }
    }

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
        this.session = session;
    }

    @OnMessage
    public void onMessage(String message) {
        ServerMessage serverMessage = gson.fromJson(message, ServerMessage.class);
        //handler.notify(serverMessage);
        ServerMessage base = gson.fromJson(message, ServerMessage.class);

        if (base.getServerMessageType() == ServerMessage.ServerMessageType.LOAD_GAME) {
            LoadGameMessage load = gson.fromJson(message, LoadGameMessage.class);
            handler.notify(load);
        } else if (base.getServerMessageType() == ServerMessage.ServerMessageType.NOTIFICATION) {
            NotificationMessage note = gson.fromJson(message, NotificationMessage.class);
            handler.notify(note);
        } else if (base.getServerMessageType() == ServerMessage.ServerMessageType.ERROR) {
            ErrorMessage error = gson.fromJson(message, ErrorMessage.class);
            handler.notify(error);
        }
    }


    public void send(UserGameCommand command) throws RuntimeException {
        if (session == null || !session.isOpen()) {
            throw new RuntimeException("WebSocket session is not open");
        }
        try {
            String json = gson.toJson(command);
            this.session.getBasicRemote().sendText(json);
        } catch (IOException e) {
            throw new RuntimeException("Failed to send command: " + e.getMessage());
        }
    }

    public boolean isOpen() {
        return session != null && session.isOpen();
    }

    public void close() {
        try {
            session.close();
        } catch (IOException ignored) {
        }
    }
}