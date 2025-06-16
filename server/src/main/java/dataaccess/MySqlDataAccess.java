package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.AuthData;
import model.UserData;
import model.GameData;
import result.GameMetadata;
import service.UnauthorizedException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import static java.sql.Types.NULL;

public class MySqlDataAccess implements DataAccess {

    public MySqlDataAccess() throws DataAccessException {
        configureDatabase();
    }

    //USER GOODIES
    public UserData createUser(UserData user) throws DataAccessException{
        String json = new Gson().toJson(user);
        var statement = "INSERT INTO user (username, password, email, json) VALUES (?, ?, ?, ?)";
        var id = executeUpdate(statement, user.username(), user.password(), user.email(), json);
        return user;
    }

    public UserData getUser(String username) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT id, json FROM user WHERE username=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, username);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readUser(rs);
                    }
                }
            }
        }
        catch (Exception e){
            throw new DataAccessException("Unable to read user");
        }
        return null;
    }

    public String getUsernameByToken(String authToken) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT username FROM auth WHERE authToken=?";
            try ( var ps = conn.prepareStatement(statement)) {
                ps.setString(1, authToken);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return rs.getString("username");
                    } else {
                        throw new UnauthorizedException("Invalid authToken");
                    }
                }
            }
        } catch (Exception e){
            throw new DataAccessException("Unable to get username");
        }
    }

    public UserData readUser(ResultSet rs) throws SQLException {
        String json = rs.getString("json");
        return new Gson().fromJson(json, UserData.class);
    }

    public void clearUsers() throws DataAccessException{
        var statement = "TRUNCATE TABLE user";
        executeUpdate(statement);
    }

    //AUTH GOODIES
    public AuthData createAuth(AuthData auth) throws DataAccessException{
        String json = new Gson().toJson(auth);
        var statement = "INSERT INTO auth (authToken, username, json) VALUES (?, ?, ?)";
        var id = executeUpdate(statement, auth.authToken(), auth.username(), json);
        return auth;
    }

    public AuthData getAuth(String auth) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT id, json FROM auth WHERE authToken=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, auth);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readAuth(rs);
                    }
                }
            }
        }
        catch (Exception e){
            throw new DataAccessException("Unable to read auth");
        }
        return null;
    }

    public AuthData readAuth(ResultSet rs) throws SQLException {
        String json = rs.getString("json");
        return new Gson().fromJson(json, AuthData.class);
    }

    //DELETE AUTH TOKEN WHEN USER LOGS OUT
    public void deleteAuth(String authToken) throws DataAccessException {
        var statement = "DELETE FROM auth WHERE authToken = ?";
        executeUpdate(statement, authToken);
    }

    public void clearAuth() throws DataAccessException {
        var statement = "TRUNCATE TABLE auth";
        executeUpdate(statement);
    }

    //STUFF FOR GAMEDATA
    public GameData createGame(GameData game) throws DataAccessException {
        String json = new Gson().toJson(game);
        var statement = "INSERT INTO game (whiteUsername, blackUsername, gameName, json) VALUES (?, ?, ?, ?)";
        int newID = executeUpdate(statement, game.whiteUsername(), game.blackUsername(), game.gameName(), json);
        return new GameData(newID, game.whiteUsername(), game.blackUsername(), game.gameName(), game.game());
    }


    public GameData getGame(int gameID) throws DataAccessException{
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT gameID, json FROM game WHERE gameID=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setInt(1, gameID);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readGame(rs);
                    }
                }
            }
        }
        catch (Exception e){
            throw new DataAccessException("Unable to read game");
        }
        return null;
    }

    public GameData readGame(ResultSet rs) throws SQLException {
        String json = rs.getString("json");
        return new Gson().fromJson(json, GameData.class);
    }

    public GameMetadata[] getGamesData() throws DataAccessException {
        var gamesList = new ArrayList<GameMetadata>();
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT gameID, gameName, whiteUsername, blackUsername FROM game";
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    while (rs.next()) {
                        int gameID = rs.getInt("gameID");
                        String gameName = rs.getString("gameName");
                        String whiteUsername = rs.getString("whiteUsername");
                        String blackUsername = rs.getString("blackUsername");

                        gamesList.add(new GameMetadata(gameID, gameName, whiteUsername, blackUsername));
                    }
                }
            }

            return gamesList.toArray(new GameMetadata[0]);
        } catch (SQLException e) {
            throw new DataAccessException("Unable to get game list");
        }
    }

    public void updateGame(String playerColor, String username, int gameID) throws DataAccessException {

        GameData oldGame = getGame(gameID);
        String white = oldGame.whiteUsername();
        String black = oldGame.blackUsername();

        if ("WHITE".equalsIgnoreCase(playerColor)) {
            white = username;
        } else if ("BLACK".equalsIgnoreCase(playerColor)) {
            black = username;
        }

        GameData newGame = new GameData(gameID, white, black, oldGame.gameName(), oldGame.game());
        String newJson = new Gson().toJson(newGame);

        String statement = "UPDATE game SET whiteUsername = ?, blackUsername = ?, json = ? WHERE gameID=?";
        executeUpdate(statement, white, black, newJson, gameID);
    }

    public void updateGameState(int gameID, ChessGame updatedGame) throws DataAccessException {
        GameData oldGame = getGame(gameID);

        String updatedJson = new Gson().toJson(new GameData(
                gameID,
                oldGame.whiteUsername(),
                oldGame.blackUsername(),
                oldGame.gameName(),
                updatedGame
        ));

        String statement = "UPDATE game SET json = ? WHERE gameID = ?";
        executeUpdate(statement, updatedJson, gameID);
    }

    public void clearGames() throws DataAccessException {
        var statement = "TRUNCATE TABLE game";
        executeUpdate(statement);
    }

    private int executeUpdate(String statement, Object... params) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)) {
            for (int i = 0; i < params.length; i++) {
                var param = params[i];
                if (param instanceof String p) { ps.setString(i + 1, p); }
                else if  (param instanceof Integer p) { ps.setInt(i + 1, p); }
                else if (param == null) { ps.setNull(i + 1, NULL); }
            }
            ps.executeUpdate();
            var rs = ps.getGeneratedKeys();
            return rs.next() ? rs.getInt(1) : 0;
        }
        catch (SQLException e) {
            throw new DataAccessException("Database update error");
        }
    }

    private final String[] createStatements = {
            CreateDB.CREATE_USER_TABLE,
            CreateDB.CREATE_AUTH_TABLE,
            CreateDB.CREATE_GAME_TABLE
    };

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var ps = conn.prepareStatement(statement)){
                    ps.executeUpdate();
                }
            }
        }
        catch (SQLException a){
            throw new DataAccessException("Database config failed");
        }
    }



}
