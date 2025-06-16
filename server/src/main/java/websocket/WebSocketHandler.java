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
import java.util.HashSet;
import java.util.Set;

@WebSocket
public class WebSocketHandler {
    private final Gson gson = new Gson();
    private final ConnectionManager connections = new ConnectionManager();
    private static final String[] headers = {"a", "b", "c", "d", "e", "f", "g", "h" };
    private final Set<Integer> resignedGames = new HashSet<>();



    @OnWebSocketConnect
    public void onConnect(Session session) {
        System.out.println("Client connected: " + session);
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        try {
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
            if (username == null) {
                session.getRemote().sendString(gson.toJson(new ErrorMessage("Invalid auth token")));
                return;
            }
            int gameID = command.getGameID();

            if (command.getCommandType() == UserGameCommand.CommandType.CONNECT) {
                connections.add(gameID, authToken, session);
                GameData gameData = dataAccess.getGame(gameID);
                if (gameData == null) {
                    connections.send(gameID, authToken, new ErrorMessage("Game not found."));
                    return;
                }
                String role;
                if (username.equals(gameData.whiteUsername())) {
                    role = "White Player";
                } else if (username.equals(gameData.blackUsername())) {
                    role = "Black Player";
                } else {
                    role = "Observer";
                }

                String customText = username + " joined game " + gameID + " as " + role;
                NotificationMessage notification = new NotificationMessage(customText);
                connections.broadcastExcept(gameID, authToken, notification);  // 👈 NEW
                connections.send(gameID, authToken, new LoadGameMessage(gameData.game()));
            } else if (command.getCommandType() == UserGameCommand.CommandType.MAKE_MOVE) {
                if (resignedGames.contains(gameID)) {
                    connections.send(gameID, authToken, new ErrorMessage("Game is over."));
                    return;
                }
                GameData gameData = dataAccess.getGame(command.getGameID());
                if (gameData == null) {
                    ServerMessage error = new ErrorMessage("Game not found.");
                    connections.broadcast(command.getGameID(), error);
                    return;
                }
                ChessGame.TeamColor playerColor = null;
                if (username.equals(gameData.whiteUsername())) {
                    playerColor = ChessGame.TeamColor.WHITE;
                } else if (username.equals(gameData.blackUsername())) {
                    playerColor = ChessGame.TeamColor.BLACK;
                } else {
                    connections.send(gameID, authToken, new ErrorMessage("You are not a player in this game."));
                    return;
                }

                ChessGame game = gameData.game();
                if (game.getTeamTurn() != playerColor) {
                    connections.send(gameID, authToken, new ErrorMessage("It's not your turn."));
                    return;
                }
                MakeMoveCommand moveCommand = (MakeMoveCommand) command;
                ChessMove move = moveCommand.getMove();

                try {
                    game.makeMove(move);
                    dataAccess.updateGameState(gameID, game);

                    connections.broadcast(command.getGameID(), new NotificationMessage(username + " made a move"));
                    connections.broadcast(command.getGameID(), new LoadGameMessage(game));

                    ChessGame.TeamColor nextToMove = game.getTeamTurn();

                    if (game.isInCheckmate(nextToMove)) {
                        String winner = nextToMove == ChessGame.TeamColor.WHITE ? gameData.blackUsername() : gameData.whiteUsername();
                        String loser = nextToMove == ChessGame.TeamColor.WHITE ? gameData.whiteUsername() : gameData.blackUsername();
                        connections.broadcast(gameID, new NotificationMessage(loser + " is in checkmate. " + winner + " wins!"));
                        resignedGames.add(gameID);
                    } else if (game.isInCheck(nextToMove)) {
                        String inCheckPlayer = nextToMove == ChessGame.TeamColor.WHITE ? gameData.whiteUsername() : gameData.blackUsername();
                        connections.broadcast(gameID, new NotificationMessage(inCheckPlayer + " is in check!"));
                    }
                } catch (InvalidMoveException e) {
                    ServerMessage error = new ErrorMessage("Illegal Move");
                    connections.send(gameID, authToken, error);
                    return;
                }

            } else if (command.getCommandType() == UserGameCommand.CommandType.LEAVE) {
                connections.remove(gameID, authToken);
                connections.broadcast(gameID, new NotificationMessage(username + " has left the game"));
            } else if (command.getCommandType() == UserGameCommand.CommandType.RESIGN) {
                handleResignCommand(username, gameID, authToken);
            }
        } catch (Exception e) {
            try {
                session.getRemote().sendString(gson.toJson(new ErrorMessage("Unexpected error: " + e.getMessage())));

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }

    @OnWebSocketClose
    public void onClose(Session session, int status, String reason){
        System.out.println("Websocket closed: " + reason);
    }

    private void handleResignCommand(String username, int gameID, String authToken) {
        try {
            DataAccess dataAccess = new MySqlDataAccess();
            GameData game = dataAccess.getGame(gameID);
            if (game == null) {
                connections.send(gameID, authToken, new ErrorMessage("Game not found."));
                return;
            }

            if (!username.equals(game.whiteUsername()) && !username.equals(game.blackUsername())) {
                connections.send(gameID, authToken, new ErrorMessage("You're not a player in this game."));
                return;
            }

            if (resignedGames.contains(gameID)) {
                connections.send(gameID, authToken, new NotificationMessage("Game already resigned."));
                return;
            }

            resignedGames.add(gameID);

            String winner = username.equals(game.whiteUsername()) ? game.blackUsername() : game.whiteUsername();
            connections.broadcast(gameID, new NotificationMessage(username + " has resigned. " + winner + " wins!"));
        } catch (Exception e) {
            try {
                connections.send(gameID, authToken, new ErrorMessage("Resign failed: " + e.getMessage()));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

}
