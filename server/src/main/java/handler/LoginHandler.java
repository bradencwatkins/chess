package handler;
import com.google.gson.Gson;
import dataaccess.UserDAO;
import model.UserData;
import request.LoginRequest;
import result.LoginResult;
import result.MessageResult;
import service.AlreadyTakenException;
import service.LoginService;
import service.UnauthorizedException;
import spark.*;

public class LoginHandler implements Route{
    private final LoginService loginService = new LoginService();
    private final Gson gson = new Gson();
    private final UserDAO userDAO = new UserDAO();

    public Object handle(Request req, Response res){

        try {
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
            return gson.toJson(new MessageResult("Error: Username doesn't exist"));
        }
        catch (Exception e){
            res.status(400);
            return gson.toJson(new MessageResult("Error: bad request"));
        }
    }

}
