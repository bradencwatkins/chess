package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import dataaccess.MySqlDataAccess;
import model.AuthData;

public class LogoutService {
    private final DataAccess dataAccess;

    public LogoutService() {
        try {
            this.dataAccess = new MySqlDataAccess();
        } catch (DataAccessException e) {
            throw new RuntimeException("Failed to initialize LogoutService");
        }
    }

    public void logout(String authToken) throws UnauthorizedException, DataAccessException{

        try {
            AuthData token = dataAccess.getAuth(authToken);
            if (token == null) {
                throw new UnauthorizedException("Token does not exist");
            }

            dataAccess.deleteAuth(token.authToken());
        } catch (DataAccessException e) {
            throw new DataAccessException("Error: Logout failed");
        }

    }

}
