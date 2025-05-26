package service;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import model.AuthData;
import result.GameMetadata;

public class ListGameService {
    private final AuthDAO authDAO = new AuthDAO();
    private final GameDAO gameDAO = new GameDAO();

    public GameMetadata[] listGames(String authToken) throws UnauthorizedException{

        AuthData token = authDAO.getToken(authToken);
        if (token == null){
            throw new UnauthorizedException("Token does not exist");
        }

        //RETURN A LIST OF ALL GAMES
        return gameDAO.getGamesData();
    }

}
