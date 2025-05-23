package service;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;

public class ClearService {
    private final AuthDAO authDAO = new AuthDAO();
    private final UserDAO userDAO = new UserDAO();
    private final GameDAO gameDAO = new GameDAO();

    //CLEAR ALL DATABASES
    public void clearData(){
        userDAO.clear();
        authDAO.clear();
        gameDAO.clear();
    }
}
