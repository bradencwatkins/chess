package dataaccess;

import model.GameData;
import model.UserData;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameDAO {
    private static final Map<String, GameData> games = new HashMap<>();
    private int nextGameID = 1;


    public GameData getGame(String gameName) {
        return games.get(gameName);
    }

    public void createGame(GameData game) {
        games.put(game.gameName(), game);
    }

    public int generateNewGameID() {
        return nextGameID++;
    }

    public Collection<GameData> getGames(){
        return games.values();
    }

    public void clear(){
        games.clear();
        nextGameID = 1;
    }
}
