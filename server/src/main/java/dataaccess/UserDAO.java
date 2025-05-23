package dataaccess;

import model.UserData;

public class UserDAO {


    public UserData getUser(String username){
        return new UserData("B","Y","U");

    }

    public void createUser(UserData user){
        //CREATE USER AND ADD IT TO SET OF USERS
    }
}
