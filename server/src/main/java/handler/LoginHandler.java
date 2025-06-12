package handler;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import request.LoginRequest;
import result.LoginResult;
import result.MessageResult;
import service.LoginService;
import service.UnauthorizedException;
import spark.*;

public class LoginHandler implements Route {
    private final Gson gson = new Gson();

    public Object handle(Request req, Response res){

        try {
            LoginService loginService = new LoginService();
            LoginRequest loginRequest = gson.fromJson(req.body(), LoginRequest.class);

            //CHECK IF USERNAME OR PASSWORD IS BLANK
            if (loginRequest == null || loginRequest.username() == null ||
                loginRequest.username().isBlank() || loginRequest.password() == null ||
                loginRequest.password().isBlank()){

                res.status(400);
                return gson.toJson(new MessageResult("Error: bad request"));
            }

            LoginResult result = loginService.login(loginRequest);
            res.status(200);
            return gson.toJson(result);
        }
        catch (UnauthorizedException u){
            res.status(401);
            return gson.toJson(new MessageResult("Error: " + u.getMessage()));
        }
        catch (DataAccessException e){
            res.status(500);
            return gson.toJson(new MessageResult("Error: Database access error"));
        }
        catch (Exception e){
            res.status(500);
            return gson.toJson(new MessageResult("Error: " + e.getMessage()));
        }

    }

}
