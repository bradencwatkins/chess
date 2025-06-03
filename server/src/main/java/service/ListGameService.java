package service;

import dataaccess.*;
import model.AuthData;
import result.GameMetadata;

public class ListGameService {
    private final AuthDAO authDAO = new AuthDAO();
    private final GameDAO gameDAO = new GameDAO();
    private final DataAccess dataAccess;

    public ListGameService() {
        try {
            this.dataAccess = new MySqlDataAccess();
        } catch (DataAccessException e){
            throw new RuntimeException("failed to initialize List Game Service");
        }
    }

    public GameMetadata[] listGames(String authToken) throws UnauthorizedException, DataAccessException {

        try {
            AuthData token = dataAccess.getAuth(authToken);
            if (token == null) {
                throw new UnauthorizedException("Token does not exist");
            }

            //RETURN A LIST OF ALL GAMES
            return dataAccess.getGamesData();
        } catch (DataAccessException e) {
            throw new DataAccessException("Error: failed to list games");
        }
    }

}
