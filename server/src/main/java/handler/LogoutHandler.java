package handler;
import com.google.gson.Gson;
import result.MessageResult;
import service.LogoutService;
import service.UnauthorizedException;
import spark.*;

public class LogoutHandler implements Route {
    private final LogoutService logoutService = new LogoutService();
    private final Gson gson = new Gson();

    public Object handle(Request req, Response res){

        try{
            String authToken = req.headers("Authorization");

            if (authToken == null || authToken.isBlank()) {
                res.status(400);
                return gson.toJson(new MessageResult("Error: Bad request"));
            }


            logoutService.logout(authToken);
            res.status(200);
            return gson.toJson(new MessageResult("Logout Successful"));

        }
        catch (UnauthorizedException e){
            res.status(401);
            return gson.toJson(new MessageResult("Error: Unauthorized"));
        }

    }

}
