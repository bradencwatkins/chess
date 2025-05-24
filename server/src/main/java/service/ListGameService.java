package service;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import model.AuthData;
import model.GameData;
import result.ListGameResult;

import java.util.Collection;
import java.util.List;

public class ListGameService {
    private final AuthDAO authDAO = new AuthDAO();
    private final GameDAO gameDAO = new GameDAO();

    public ListGameResult listGames(String authToken) throws UnauthorizedException{

        AuthData token = authDAO.getToken(authToken);
        if (token == null){
            throw new UnauthorizedException("Token does not exist");
        }

        //RETURN A LIST OF ALL GAMES
        return new ListGameResult(gameDAO.getGames());
    }

}
