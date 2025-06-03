package handler;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import result.GameMetadata;
import result.ListGameResult;
import result.MessageResult;
import service.ListGameService;
import service.UnauthorizedException;
import spark.*;

public class ListGameHandler implements Route {
    private final Gson gson = new Gson();

    public Object handle(Request req, Response res){

        try{
            ListGameService listGameService = new ListGameService();
            String authToken = req.headers("Authorization");
            if (authToken == null || authToken.isBlank()) {
                res.status(400);
                return gson.toJson(new MessageResult("Error: Bad request"));
            }

            GameMetadata[] gamesList = listGameService.listGames(authToken);


            res.status(200);
            if (gamesList == null || gamesList.length == 0){
                return gson.toJson(new ListGameResult(new GameMetadata[0]));
            }
            return gson.toJson(new ListGameResult(gamesList));


        }
        catch (UnauthorizedException u){
            res.status(401);
            return gson.toJson(new MessageResult("Error: unauthorized"));
        }
        catch (DataAccessException e) {
            res.status(500);
            return gson.toJson(new MessageResult("Error: Data access error"));
        } catch (RuntimeException e) {
            res.status(500);
            return gson.toJson(new MessageResult("Internal Server Error"));
        }
    }


}
