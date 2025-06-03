package service;

import dataaccess.*;

public class ClearService {
    private final DataAccess dataAccess;

    public ClearService() {
        try {
            this.dataAccess = new MySqlDataAccess();
        } catch (DataAccessException e) {
            throw new RuntimeException("Failed to initialize ClearService");
        }
    }

    //CLEAR ALL DATABASES
    public void clearData() throws DataAccessException {
        try {
            dataAccess.clearUsers();
            dataAccess.clearAuth();
            dataAccess.clearGames();
        } catch (DataAccessException e){
            throw new DataAccessException("Error: Clear Failed");
        }
    }
}
