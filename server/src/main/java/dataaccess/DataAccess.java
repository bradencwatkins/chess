package dataaccess;

import model.AuthData;
import model.GameData;
import model.UserData;
import org.eclipse.jetty.server.Authentication;
import result.GameMetadata;

public interface DataAccess {

    UserData createUser(UserData user) throws DataAccessException;
    UserData getUser(String username) throws DataAccessException;
    void clearUsers() throws DataAccessException;

    AuthData createAuth(AuthData auth) throws DataAccessException;
    AuthData getAuth(String authToken) throws DataAccessException;
    AuthData getAuthByUsername(String username) throws DataAccessException;
    void deleteAuth(String authToken) throws DataAccessException;
    void clearAuth() throws DataAccessException;


    GameData createGame(GameData game) throws DataAccessException;
    GameData getGame(int gameID) throws DataAccessException;
    GameMetadata[] getGamesData() throws DataAccessException;
    String getUsernameByToken(String authToken) throws  DataAccessException;
    void updateGame(String playerColor, String username, int gameID) throws DataAccessException;
    void clearGames() throws DataAccessException;

}
