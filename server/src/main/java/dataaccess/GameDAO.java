package dataaccess;

import model.GameData;
import model.UserData;

import java.util.HashMap;
import java.util.Map;

public class GameDAO {
    private Map<String, GameData> games = new HashMap<>();

    public void clear(){
        games.clear();
    }
}
