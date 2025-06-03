package service;

import dataaccess.*;
import model.AuthData;
import model.UserData;
import org.eclipse.jetty.server.Authentication;
import org.mindrot.jbcrypt.BCrypt;
import request.LoginRequest;
import result.LoginResult;
import result.MessageResult;

import java.util.UUID;

public class LoginService {
    private final DataAccess dataAccess;

    public LoginService() {
        try {
            this.dataAccess = new MySqlDataAccess();
        } catch (DataAccessException e) {
            throw new RuntimeException("Failed to initialize Login service");
        }
    }

    public LoginResult login(LoginRequest request) throws Exception {

        UserData user = dataAccess.getUser(request.username());
        if (user == null){
            throw new UnauthorizedException("Username does not exist");
        }
        if (user.email() == null || user.email().isBlank()){
            throw new Exception("email does not exist");
        }

        //VALIDATE PASSWORD
        if (!BCrypt.checkpw(request.password(), user.password())){
            throw new UnauthorizedException("Incorrect password");
        }

        String token = UUID.randomUUID().toString();
        AuthData authData = new AuthData(token, request.username());
        dataAccess.createAuth(authData);
        return new LoginResult(request.username(), token);
    }

}
