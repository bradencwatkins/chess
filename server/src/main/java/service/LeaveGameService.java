package service;

import dataaccess.*;
import model.AuthData;
import model.GameData;

public class LeaveGameService {
    private final DataAccess dataAccess;

    public LeaveGameService() {
        try {
            this.dataAccess = new MySqlDataAccess();
        } catch (DataAccessException e) {
            throw new RuntimeException("Failed to initialize Leave Game Service");
        }
    }

    public void leaveGame(String authToken, String username, int gameID) throws Exception {
        try {
            AuthData token = dataAccess.getAuth(authToken);
            if (token == null) {
                throw new UnauthorizedException("Token does not exist");
            }

            GameData game = dataAccess.getGame(gameID);
            if (game == null) {
                throw new Exception("gameID doesn't exist");
            }

            boolean changed = false;
            if (username.equals(game.whiteUsername())) {
                dataAccess.updateGame("WHITE", null, gameID);
                changed = true;
            }
            if (username.equals(game.blackUsername())) {
                dataAccess.updateGame("BLACK", null, gameID);
                changed = true;
            }

            if (!changed) {
                throw new Exception("User not found in game");
            }

        } catch (DataAccessException e) {
            throw new DataAccessException("Error leaving game");
        }
    }
}
