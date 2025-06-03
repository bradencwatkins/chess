package service;

import chess.ChessGame;
import dataaccess.*;
import model.AuthData;
import model.GameData;
import request.CreateGameRequest;
import result.CreateGameResult;

public class CreateGameService {
    private final DataAccess dataAccess;

    public CreateGameService() {
        try {
            this.dataAccess = new MySqlDataAccess();
        }
        catch (DataAccessException e) {
            throw new RuntimeException("Failed to initialize Create game service");
        }
    }

    public CreateGameResult createGame(String token, CreateGameRequest gameName) throws UnauthorizedException, DataAccessException{

        try {

            AuthData authToken = dataAccess.getAuth(token);
            if (authToken == null) {
                throw new UnauthorizedException("Not authorized");
            }

            //CREATE NEW GAME
            ChessGame chessGame = new ChessGame();
            GameData newGame = new GameData(0, null, null, gameName.gameName(), chessGame);

            newGame = dataAccess.createGame(newGame);

            return new CreateGameResult(newGame.gameID());
        } catch (DataAccessException e) {
            throw new DataAccessException("Error: Create Game failed");
        }

    }


}
