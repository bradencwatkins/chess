package ServerFacade;

import chess.ChessGame;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import static java.lang.System.out;
import static ui.EscapeSequences.*;

public class ServerFacadeHandler {
    private static ServerFacade server = new ServerFacade("http://localhost:8080");


    public boolean registerHandler(String[] inputWords) {
        try {
            Map<String, Object> request = new HashMap<>();
            request.put("username", inputWords[1]);
            request.put("password", inputWords[2]);
            request.put("email", inputWords[3]);

            server.registerUser(request);

            out.println("Successfully registered and logged in");
            return true;
        } catch (ServerException e) {
            out.println(e.getMessage());
            return false;
        } catch (Exception e) {
            out.println("Server failure");
            return false;
        }
    }

    public boolean loginHandler(String[] inputWords){
        try {
            Map<String, Object> request = new HashMap<>();
            request.put("username", inputWords[1]);
            request.put("password", inputWords[2]);

            Map<String, Object> response = server.loginUser(request);
            String authToken = (String) response.get("authToken");
            server.setAuthToken(authToken);

            out.println("Successfully logged in");
            return true;
        } catch (ServerException e) {
            out.println(e.getMessage());
            return false;
        } catch (Exception e) {
            out.println("Server failure");
            return false;
        }
    }

    public void createGameHandler(String[] inputWords){
        try {
            Map<String, Object> request = new HashMap<>();
            request.put("gameName", inputWords[1]);

            server.createGame(request);
            out.println("Successfully created game");
        } catch (ServerException e) {
            out.println(e.getMessage());
        } catch (Exception e) {
            out.println("Server failure");
        }
    }

    public void listGameHandler() {
        try {
            out.print(SET_TEXT_COLOR_MAGENTA);
            out.println();
            Map<String, Object> response = server.listGames();

            Object gamesObject = response.get("games");

            if (gamesObject instanceof Iterable<?> games) {
                out.println("Games:");
                for (Object gameObject : games){
                    if (gameObject instanceof Map<?, ?> game) {
                        Object idObj = game.get("gameID");
                        int id = (idObj instanceof Number) ? ((Number) idObj).intValue() : -1;
                        Object name = game.get("gameName");
                        Object whiteUsername = game.get("whiteUsername");
                        Object blackUsername = game.get("blackUsername");
                        out.printf("   -%s | ID: %d | White Player: %s | Black Player: %s%n", name, id, whiteUsername, blackUsername);
                    }
                }
            }

        } catch (ServerException e) {
            out.println(e.getMessage());
        } catch (Exception e) {
            out.println("Server failure");
        }
    }

    public void joinGameHandler(String[] inputWords){
        try {
            Map<String, Object> request = new HashMap<>();
            int gameID = Integer.parseInt(inputWords[1]);
            request.put("gameID", gameID);
            String playerColor = inputWords[2];
            if (playerColor != null) {
                request.put("playerColor", playerColor.toUpperCase());
            }

            server.joinGame(request);
            out.println("Successfully joined game");
        } catch (ServerException e) {
            out.println(e.getMessage());
        } catch (Exception e) {
            out.println("Server failure");
        }
    }

    public void logoutHandler() {
        try {
            server.logoutUser();
            server.setAuthToken(null);
            out.println("Successfully logged out");
        } catch (ServerException e) {
            out.println(e.getMessage());
        } catch (Exception e) {
            out.println("Server failure");
        }
    }

    public void observeGameHandler(String[] inputWords) {
        try {
            Map<String, Object> request = new HashMap<>();
            int gameID = Integer.parseInt(inputWords[1]);
            request.put("gameID", gameID);
            request.put("playerColor", null);

            server.joinGame(request);
            out.println("Successfully joined game as observer");
        } catch (ServerException e) {
            out.println(e.getMessage());
        } catch (Exception e) {
            out.println("Server failure");
        }
    }

    public ChessGame getGameHandler(int gameID) {
        try {
            Map<String, Object> response = server.getGameState(gameID);
            out.println(response);
            Gson gson = new Gson();
            String json = gson.toJson(response);

            ChessGame game = gson.fromJson(json, ChessGame.class);
            out.println("Successfully retrieved game state");
            return game;
        } catch (Exception e) {
            out.println("Failed to retrieve game state.");
            return null;
        }
    }

}
