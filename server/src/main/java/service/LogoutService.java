package service;

import dataaccess.AuthDAO;
import model.AuthData;

public class LogoutService {
    private final AuthDAO authDAO = new AuthDAO();

    public void logout(String authToken) throws UnauthorizedException{

        AuthData token = authDAO.getToken(authToken);
        if (token == null){
            throw new UnauthorizedException("Token does not exist");
        }

        authDAO.deleteAuth(token.authToken());


    }

}
