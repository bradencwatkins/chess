package service;

import chess.ChessGame;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import dataaccess.MySqlDataAccess;
import model.AuthData;
import model.GameData;

public class GetGameService {
    private final DataAccess dataAccess;

    public GetGameService() {
        try {
            this.dataAccess = new MySqlDataAccess();
        } catch (DataAccessException e){
            throw new RuntimeException("Failed to initialize GetGameService");
        }
    }

    public ChessGame getGame(int gameID, String authToken) throws UnauthorizedException, DataAccessException {
        try {
            AuthData token = dataAccess.getAuth(authToken);
            if (token == null) {
                throw new UnauthorizedException("Token does not exist");
            }

            GameData game = dataAccess.getGame(gameID);
            return game.game();
        } catch (DataAccessException e) {
            throw new DataAccessException("Error: failed to retrieve game");
        }
    }

}
