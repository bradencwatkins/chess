package websocket;


import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;

import org.eclipse.jetty.websocket.api.annotations.*;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import java.io.IOException;
@WebSocket
public class WebSocketHandler {
    private final Gson gson = new Gson();
    private final ConnectionManager connections = new ConnectionManager();

    @OnWebSocketConnect
    public void onConnect(Session session) {
        System.out.println("Client connected: " + session);
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        UserGameCommand command = gson.fromJson(message, UserGameCommand.class);
        String authToken = command.getAuthToken();
        int gameID = command.getGameID();

        if (command.getCommandType() == UserGameCommand.CommandType.CONNECT){
            connections.add(gameID, authToken, session);
            connections.broadcast(gameID, new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION));
                  //  authToken + " joined game " + gameID));
        }
        else if (command.getCommandType() == UserGameCommand.CommandType.MAKE_MOVE){
            connections.broadcast(gameID, new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION));
                  //  authToken + " made a move"));
        }
        else if (command.getCommandType() == UserGameCommand.CommandType.LEAVE){
            connections.remove(gameID, authToken);
            connections.broadcast(gameID, new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION));
                   // authToken + " left the game"));
        }
        else if (command.getCommandType() == UserGameCommand.CommandType.RESIGN){
            connections.broadcast(gameID, new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION));
            //        authToken + " resigned"));
        }
    }

    @OnWebSocketClose
    public void onClose(Session session, int status, String reason){
        System.out.println("Websocket closed: " + reason);
    }


}
