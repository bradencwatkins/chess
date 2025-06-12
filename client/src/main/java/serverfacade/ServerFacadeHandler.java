package serverfacade;

import chess.ChessGame;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import static java.lang.System.out;
import static ui.EscapeSequences.*;

public class ServerFacadeHandler {
    private static ServerFacade server;

    public ServerFacadeHandler(ServerFacade server) {
        this.server = server;
    }

    public void clear() throws Exception {
        server.clear();
    }

    public boolean registerHandler(String[] inputWords) {
        try {
            Map<String, Object> request = new HashMap<>();
            request.put("username", inputWords[1]);
            request.put("password", inputWords[2]);
            request.put("email", inputWords[3]);

            server.registerUser(request);

            out.println("\u001b[36m  Successfully registered");
            return true;
        } catch (ServerException e) {
            out.println("\u001b[31m  " + e.getMessage());
            return false;
        } catch (Exception e) {
            out.println("\u001b[31m  Server failure");
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

            out.println("\u001b[36m  Successfully logged in");
            return true;
        } catch (ServerException e) {
            out.println("\u001b[31m  " + e.getMessage());
            return false;
        } catch (Exception e) {
            out.println("\u001b[31m  Server failure");
            return false;
        }
    }

    public void createGameHandler(String[] inputWords){
        try {
            Map<String, Object> request = new HashMap<>();
            request.put("gameName", inputWords[1]);

            server.createGame(request);
            out.println("\u001b[36m  Successfully created game");
        } catch (ServerException e) {
            out.println("\u001b[31m  " + e.getMessage());
        } catch (Exception e) {
            out.println("\u001b[31m  Server failure");
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
                int displayID = 1;
                for (Object gameObject : games){
                    if (gameObject instanceof Map<?, ?> game) {
                        Object name = game.get("gameName");
                        Object whiteUsername = game.get("whiteUsername");
                        Object blackUsername = game.get("blackUsername");
                        out.printf("   -%s | ID: %d | White Player: %s | Black Player: %s%n", name, displayID++, whiteUsername, blackUsername);
                    }
                }
            }

        } catch (ServerException e) {
            out.println("\u001b[31m  " + e.getMessage());
        } catch (Exception e) {
            out.println("\u001b[31m  Server failure");
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
            out.println("\u001b[36m  Successfully joined game");
        } catch (ServerException e) {
            out.println("\u001b[31m  " + e.getMessage());
        } catch (Exception e) {
            out.println("\u001b[31m  Server failure");
        }
    }

    public void logoutHandler() {
        try {
            server.logoutUser();
            server.setAuthToken(null);
            out.println("\u001b[36m  Successfully logged out");
        } catch (ServerException e) {
            out.println("\u001b[31m  " + e.getMessage());
        } catch (Exception e) {
            out.println("\u001b[31m  Server failure");
        }
    }

    public void observeGameHandler(String[] inputWords) {
        try {
            Map<String, Object> request = new HashMap<>();
            int gameID = Integer.parseInt(inputWords[1]);
            request.put("gameID", gameID);
            request.put("playerColor", null);

            server.joinGame(request);
            out.println("\u001b[36m  Successfully joined game as observer");
        } catch (ServerException e) {
            out.println("\u001b[31m  " + e.getMessage());
        } catch (Exception e) {
            out.println("\u001b[31m  Server failure");
        }
    }

    public ChessGame getGameHandler(int gameID) {
        try {
            Map<String, Object> response = server.getGameState(gameID);
            Gson gson = new Gson();
            String json = gson.toJson(response);

            ChessGame game = gson.fromJson(json, ChessGame.class);
            return game;
        } catch (Exception e) {
            return null;
        }
    }

}
