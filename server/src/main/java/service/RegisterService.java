package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;
import request.RegisterRequest;
import result.RegisterResult;

import java.util.UUID;

public class RegisterService {
    private final UserDAO userDAO = new UserDAO();
    private final AuthDAO authDAO = new AuthDAO();
    private final DataAccess dataAccess;

    public RegisterService(DataAccess dataAccess){
        this.dataAccess = dataAccess;
    }

    public RegisterResult register(RegisterRequest request) throws AlreadyTakenException, DataAccessException {

        UserData existingUser = dataAccess.getUser(request.username());
        if (existingUser != null){
            throw new AlreadyTakenException("Username already exists");
        }

        //CREATE NEW USER
        UserData newUser = new UserData(request.username(), request.password(), request.email());
        dataAccess.createUser(newUser);

        //CREATE AUTH TOKEN
        String token = UUID.randomUUID().toString();
        AuthData authData = new AuthData(token, request.username());
        dataAccess.createAuth(authData);

        return new RegisterResult(request.username(), token);
    }
}
