package websocket;


import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import com.google.gson.Gson;
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
        UserGameCommand command = gson.fromJson(message, UserGameCommand.class);
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
            ChessGame game = gameData.game();
            MakeMoveCommand moveCommand = (MakeMoveCommand) command;
            String from = moveCommand.getFrom();
            String letter = String.valueOf(from.charAt(0));
            String number = String.valueOf(from.charAt(1));
            int col = java.util.Arrays.asList(headers).indexOf(letter) + 1;
            int row = Integer.parseInt(number);
            ChessPosition fromPos = new ChessPosition(row, col);
            Collection<ChessMove> validMoves = game.validMoves(fromPos);


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
