package handler;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import result.MessageResult;
import service.LogoutService;
import service.UnauthorizedException;
import spark.*;

public class LogoutHandler implements Route {
    private final Gson gson = new Gson();

    public Object handle(Request req, Response res){
        try{
            LogoutService logoutService = new LogoutService();
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
        catch (DataAccessException e){
            res.status(500);
            return gson.toJson(new MessageResult("Error: Database access error"));
        } catch (RuntimeException e) {
            res.status(500);
            return gson.toJson(new MessageResult("Internal Server Error"));
        }
    }
}
