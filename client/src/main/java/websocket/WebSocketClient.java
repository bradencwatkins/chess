package websocket;

import com.google.gson.Gson;
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
            System.out.println("server: " + uri);
            container.connectToServer(this, uri);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("WebSocket connection failed: " + e.getMessage());
        }
    }

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
        this.session = session;
        this.session.addMessageHandler(String.class, message -> {
            ServerMessage msg = gson.fromJson(message, ServerMessage.class);
            handler.notify(msg);
        });
    }

    @OnMessage
    public void onMessage(String message) {
        ServerMessage serverMessage = gson.fromJson(message, ServerMessage.class);
        handler.notify(serverMessage);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        System.err.println("WebSocket error: " + throwable.getMessage());
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        System.out.println("WebSocket closed: " + closeReason.getReasonPhrase());
    }

    public void send(UserGameCommand command) throws RuntimeException {
        try {
            String json = gson.toJson(command);
            this.session.getBasicRemote().sendText(json);
        } catch (IOException e) {
            throw new RuntimeException("Failed to send command: " + e.getMessage());
        }
    }

    public void close() {
        try {
            session.close();
        } catch (IOException ignored) {
        }
    }
}