package service;

import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import model.AuthData;
import model.GameData;
import request.CreateGameRequest;
import result.CreateGameResult;

public class CreateGameService {
    private final GameDAO gameDAO = new GameDAO();
    private final AuthDAO authDAO = new AuthDAO();

    public CreateGameResult createGame(String token, CreateGameRequest gameName) throws UnauthorizedException{

        GameData gameData = gameDAO.getGame(gameName.gameName());
        if (gameData != null){
            throw new UnauthorizedException("Game name already exists");
        }

        AuthData authToken = authDAO.getToken(token);
        if (authToken == null){
            throw new UnauthorizedException("Not authorized");
        }

        //CREATE NEW GAME
        int newGameID = gameDAO.generateNewGameID();
        ChessGame chessGame = new ChessGame();
        GameData newGame = new GameData(newGameID, null, null, gameName.gameName(), chessGame);

        gameDAO.createGame(newGame);

        return new CreateGameResult(newGameID);

    }


}
