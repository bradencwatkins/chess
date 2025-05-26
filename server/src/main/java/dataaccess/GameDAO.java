package dataaccess;

import model.GameData;
import result.GameMetadata;

import java.util.*;

public class GameDAO {
    private static final Map<Integer, GameData> games = new HashMap<>();
    private int nextGameID = 1;


    public GameData getGame(int gameID) {
        return games.get(gameID);
    }

    public Map<Integer, GameData> returnAllGames(){
        return games;
    }

    public void createGame(GameData game) {
        games.put(game.gameID(), game);
    }

    public void updateGame(String playerColor, String username, int gameID){
        GameData oldGame = games.get(gameID);
        String white = oldGame.whiteUsername();
        String black = oldGame.blackUsername();

        if (Objects.equals(playerColor, "BLACK")){
            black = username;
        }
        if (Objects.equals(playerColor, "WHITE")){
            white = username;
        }

        GameData newGame = new GameData(
                oldGame.gameID(),
                white, black,
                oldGame.gameName(),
                oldGame.game()
        );
        games.put(gameID, newGame);
    }

    public int generateNewGameID() {
        return nextGameID++;
    }

    public GameMetadata[] getGamesData(){
        List<GameMetadata> gameList = new ArrayList<>();

        if (games.isEmpty()){
            return new GameMetadata[0];
        }

        for (GameData game : games.values()) {
            GameMetadata gameData = new GameMetadata(
                    game.gameID(),
                    game.gameName(),
                    game.whiteUsername(),
                    game.blackUsername());
            gameList.add(gameData);
        }

        return gameList.toArray(new GameMetadata[0]);
    }

    public void clear(){
        games.clear();
        nextGameID = 1;
    }
}
