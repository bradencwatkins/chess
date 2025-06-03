package service;

import dataaccess.*;
import model.AuthData;
import model.GameData;

import java.util.Objects;

public class JoinGameService {
    private final GameDAO gameDAO = new GameDAO();
    private final AuthDAO authDAO = new AuthDAO();
    private final DataAccess dataAccess;

    public JoinGameService() {
        try {
            this.dataAccess = new MySqlDataAccess();
        } catch (DataAccessException e) {
            throw new RuntimeException("Failed to initialize Join Game Service");
        }
    }

    public void joinGame(String authToken, String playerColor, int gameID) throws Exception {

        try {
            AuthData token = dataAccess.getAuth(authToken);
            if (token == null) {
                throw new UnauthorizedException("Token does not exist");
            }
            String username = dataAccess.getUsernameByToken(authToken);

            GameData gameJoin = dataAccess.getGame(gameID);
            if (gameJoin == null) {
                throw new Exception("gameID doesnt exist");
            }

            if ("BLACK".equalsIgnoreCase(playerColor)) {
                if (gameJoin.blackUsername() != null && !gameJoin.blackUsername().equals(username)) {
                    throw new AlreadyTakenException("Color already taken");
                }
            }
            if ("WHITE".equalsIgnoreCase(playerColor)) {
                if (gameJoin.whiteUsername() != null && !gameJoin.whiteUsername().equals(username)) {
                    throw new AlreadyTakenException("Color already taken");
                }
            }

            dataAccess.updateGame(playerColor, username, gameID);
        } catch (DataAccessException e) {
            throw new DataAccessException("Error Joining game");
        }
    }


}
