package dataaccess;

import com.google.gson.Gson;
import model.UserData;

import java.sql.SQLException;
import java.sql.Statement;

import static java.sql.Types.NULL;

public class MySqlDataAccess {

    public MySqlDataAccess() throws DataAccessException {
        configureDatabase();
    }

    public UserData addUser(UserData user) throws DataAccessException{
        String json = new Gson().toJson(user);
        var statement = "INSERT INTO user (username, password, email, json) VALUES (?, ?, ?, ?)";
        var id = executeUpdate()


    }



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

}
