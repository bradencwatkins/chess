package websocket;


import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dataaccess.DataAccess;
import dataaccess.MySqlDataAccess;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;

import org.eclipse.jetty.websocket.api.annotations.*;
import service.GetGameService;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.Collection;

@WebSocket
public class WebSocketHandler {
    private final Gson gson = new Gson();
    private final ConnectionManager connections = new ConnectionManager();
    private static final String[] headers = {"a", "b", "c", "d", "e", "f", "g", "h" };


    @OnWebSocketConnect
    public void onConnect(Session session) {
        System.out.println("Client connected: " + session);
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        JsonObject obj = JsonParser.parseString(message).getAsJsonObject();
        String type = obj.get("commandType").getAsString();

        UserGameCommand command;
        if ("MAKE_MOVE".equals(type)) {
            command = gson.fromJson(obj, MakeMoveCommand.class);
        } else {
            command = gson.fromJson(obj, UserGameCommand.class);
        }
        String authToken = command.getAuthToken();
        DataAccess dataAccess = new MySqlDataAccess();
        String username = dataAccess.getUsernameByToken(authToken);
        int gameID = command.getGameID();


        if (command.getCommandType() == UserGameCommand.CommandType.CONNECT){
            connections.add(gameID, authToken, session);
            String customText = username + " joined game " + gameID;
            NotificationMessage notification = new NotificationMessage(customText);
            connections.broadcast(gameID, notification);
        }
        else if (command.getCommandType() == UserGameCommand.CommandType.MAKE_MOVE){
            GameData gameData = dataAccess.getGame(command.getGameID());
            if (gameData == null) {
                ServerMessage error = new ErrorMessage("Game not found.");
                connections.broadcast(command.getGameID(), error);
                return;
            }
            ChessGame game = gameData.game();
            MakeMoveCommand moveCommand = (MakeMoveCommand) command;
            ChessMove move = moveCommand.getMove();

            try {
                game.makeMove(move);
                dataAccess.updateGameState(gameID, game);

                String notificationText = username + " made a move";
                connections.broadcast(command.getGameID(), new NotificationMessage(notificationText));
                ServerMessage update = new LoadGameMessage(game);
                connections.broadcast(command.getGameID(), update);
            } catch (InvalidMoveException e) {
                ServerMessage error = new ErrorMessage("Illegal Move");
                connections.broadcast(command.getGameID(), error);
            }


            String notificationText = username + " made a move";
            connections.broadcast(command.getGameID(), new NotificationMessage(notificationText));
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
