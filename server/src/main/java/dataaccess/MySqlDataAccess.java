package dataaccess;

import com.google.gson.Gson;
import model.UserData;

import javax.xml.crypto.Data;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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

    public UserData readUser(ResultSet rs) throws SQLException {
        String json = rs.getString("json");
        return new Gson().fromJson(json, UserData.class);
    }

    public void clearUsers() throws DataAccessException{
        var statement = "TRUNCATE TABLE user";
        executeUpdate(statement);
    }

    //AUTH GOODIES

    private int executeUpdate(String statement, Object... params) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)) {
            for (int i = 0; i < params.length; i++) {
                var param = params[i];
                if (param instanceof String p) ps.setString(i + 1, p);
                else if (param instanceof Integer p) ps.setInt(i + 1, p);
                else if (param == null) ps.setNull(i + 1, NULL);
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
            CreateDB.createUserTable,
            CreateDB.createAuthTable,
            CreateDB.createGameTable
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
