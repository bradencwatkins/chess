package service;

import dataaccess.AuthDAO;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;
import request.RegisterRequest;
import result.RegisterResult;

import java.util.UUID;

public class RegisterService {
    private final UserDAO userDAO = new UserDAO();
    private final AuthDAO authDAO = new AuthDAO();

    public RegisterResult register(RegisterRequest request) throws AlreadyTakenException {

        UserData existingUser = userDAO.getUser(request.username());
        if (existingUser != null){
            throw new AlreadyTakenException("Username already exists");
        }

        //CREATE NEW USER
        UserData newUser = new UserData(request.username(), request.password(), request.email());
        userDAO.createUser(newUser);

        //CREATE AUTH TOKEN
        String token = UUID.randomUUID().toString();
        AuthData authData = new AuthData(token, request.username());
        authDAO.createAuth(authData);

        return new RegisterResult(request.username(), token);
    }
}
