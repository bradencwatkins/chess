package dataaccess;

import model.AuthData;

import java.util.HashMap;
import java.util.Map;

public class AuthDAO {
    private static final Map<String, AuthData> tokens = new HashMap<>();

    public AuthData getToken(String token){
        return tokens.get(token);
    }

    public void createAuth(AuthData authData){
        System.out.println(authData.authToken());
        tokens.put(authData.authToken(), authData);
    }

    public void deleteAuth(String token){
        tokens.remove(token);
    }

    public void clear(){
        tokens.clear();
    }
}
