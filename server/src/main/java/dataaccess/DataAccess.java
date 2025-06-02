package dataaccess;

import model.AuthData;
import model.UserData;
import org.eclipse.jetty.server.Authentication;

public interface DataAccess {

    UserData createUser(UserData user) throws DataAccessException;
    UserData getUser(String username) throws DataAccessException;
    void clearUsers() throws DataAccessException;

    AuthData createAuth(AuthData auth) throws DataAccessException;
    void clearAuth() throws DataAccessException;

}
