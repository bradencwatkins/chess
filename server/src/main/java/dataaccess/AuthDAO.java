package dataaccess;

import model.AuthData;

import java.util.HashMap;
import java.util.Map;

public class AuthDAO {
    private Map<String, AuthData> tokens = new HashMap<>();


    public void createAuth(AuthData authData){
        //ADD AUTHDATA TO DATABASE
    }

    public void clear(){
        tokens.clear();
    }
}
