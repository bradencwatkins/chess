package service;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import model.AuthData;
import model.GameData;

import java.util.Objects;

public class JoinGameService {
    private final GameDAO gameDAO = new GameDAO();
    private final AuthDAO authDAO = new AuthDAO();

    public void joinGame(String authToken, String playerColor, int gameID) throws Exception {


        AuthData token = authDAO.getToken(authToken);
        if (token == null){
            throw new UnauthorizedException("Token does not exist");
        }
        String username = authDAO.getUsernameByToken(authToken);

        GameData gameJoin = gameDAO.getGame(gameID);
        if (gameJoin == null){
            throw new Exception("gameID doent exist");
        }
        if ("BLACK".equalsIgnoreCase(playerColor)){
            if (gameJoin.blackUsername() != null && !gameJoin.blackUsername().equals(username)) {
                throw new AlreadyTakenException("Color already taken");
            }
        }
        if ("WHITE".equalsIgnoreCase(playerColor)){
            if (gameJoin.blackUsername() != null && !gameJoin.blackUsername().equals(username)) {
                throw new AlreadyTakenException("Color already taken");
            }
        }

        gameDAO.updateGame(playerColor, username, gameID);

    }


}
