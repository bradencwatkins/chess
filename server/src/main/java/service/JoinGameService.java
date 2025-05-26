package service;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import model.AuthData;
import model.GameData;

import java.util.Objects;

public class JoinGameService {
    private final GameDAO gameDAO = new GameDAO();
    private final AuthDAO authDAO = new AuthDAO();

    public void joinGame(String authToken, String playerColor, int gameID) throws AlreadyTakenException{


        AuthData token = authDAO.getToken(authToken);
        if (token == null){
            throw new AlreadyTakenException("Token does not exist");
        }
        String username = authDAO.getUsernameByToken(authToken);

        GameData gameJoin = gameDAO.getGame(gameID);
        if (gameJoin.blackUsername() != null && Objects.equals(playerColor, "BLACK")){
            throw new AlreadyTakenException("Color already taken");
        }
        if (gameJoin.whiteUsername() != null && Objects.equals(playerColor, "WHITE")){
            throw new AlreadyTakenException("Color already taken");
        }

        gameDAO.updateGame(playerColor, username, gameID);

    }


}
