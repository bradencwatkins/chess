package service;

import dataaccess.AuthDAO;
import dataaccess.UserDAO;
import model.UserData;
import org.eclipse.jetty.server.Authentication;
import request.LoginRequest;
import result.LoginResult;

import java.util.UUID;

public class LoginService {
    private final UserDAO userDAO = new UserDAO();
    private final AuthDAO authDAO = new AuthDAO();

    public LoginResult login(LoginRequest request) throws UnauthorizedException{

        UserData user = userDAO.getUser(request.username());
        if (user == null){
            throw new UnauthorizedException("Username does not exist");
        }

        //VALIDATE PASSWORD
        if (!user.password().equals(request.password())){
            throw new UnauthorizedException("Incorrect password");
        }

        String token = UUID.randomUUID().toString();
        return new LoginResult(request.username(), token);
    }

}
