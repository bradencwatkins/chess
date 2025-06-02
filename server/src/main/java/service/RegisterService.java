package service;

import dataaccess.*;
import model.AuthData;
import model.UserData;
import request.RegisterRequest;
import result.RegisterResult;

import java.util.UUID;

public class RegisterService {
    private final DataAccess dataAccess;

    public RegisterService(){
        try {
            this.dataAccess = new MySqlDataAccess();
        } catch (DataAccessException e) {
            throw new RuntimeException("Failed to initialize RegisterService");
        }
    }

    public RegisterResult register(RegisterRequest request) throws AlreadyTakenException, DataAccessException {

        try {
            UserData existingUser = dataAccess.getUser(request.username());
            if (existingUser != null) {
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
        } catch (DataAccessException d) {
            throw new DataAccessException("Error: Register failed");
        }
    }
}
