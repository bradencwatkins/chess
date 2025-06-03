package handler;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import request.CreateGameRequest;
import result.CreateGameResult;
import result.MessageResult;
import service.CreateGameService;
import service.UnauthorizedException;
import spark.Request;
import spark.Response;
import spark.Route;

public class CreateGameHandler implements Route {
    private final Gson gson = new Gson();

    public Object handle(Request req, Response res){
        try{
            CreateGameService createGameService = new CreateGameService();
            CreateGameRequest gameName = gson.fromJson(req.body(), CreateGameRequest.class);
            String authToken = req.headers("Authorization");

            if (gameName == null || authToken == null || authToken.isBlank()
                || gameName.gameName() == null || gameName.gameName().isBlank()){
                res.status(400);
                return gson.toJson(new MessageResult("Error: Bad request"));
            }


            CreateGameResult gameID = createGameService.createGame(authToken, gameName);
            if (gameID == null){
                res.status(400);
                return gson.toJson(new MessageResult("Error: Bad request"));
            }

            res.status(200);
            return gson.toJson(gameID);
        }
        catch (UnauthorizedException u) {
            res.status(401);
            return gson.toJson(new MessageResult("Error: Unauthorized"));
        } catch (DataAccessException e){
            res.status(500);
            return gson.toJson(new MessageResult("Error: Data access error"));
        }
    }
}
