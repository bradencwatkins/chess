package handler;

import com.google.gson.Gson;
import request.CreateGameRequest;
import result.CreateGameResult;
import result.MessageResult;
import service.CreateGameService;
import service.UnauthorizedException;
import spark.Request;
import spark.Response;
import spark.Route;

public class CreateGameHandler implements Route {
    private final CreateGameService createGameService = new CreateGameService();
    private final Gson gson = new Gson();

    public Object handle(Request req, Response res){

        try{
            CreateGameRequest gameName = gson.fromJson(req.body(), CreateGameRequest.class);
            String authToken = req.headers("Authorization");

            if (gameName == null || authToken == null || authToken.isBlank()){
                res.status(400);
                return gson.toJson(new MessageResult("Error: Bad request"));
            }


            CreateGameResult gameID = createGameService.createGame(authToken, gameName);
            res.status(200);
            return gson.toJson(gameID);
        }
        catch (UnauthorizedException u) {
            res.status(401);
            return gson.toJson(new MessageResult("Error: Unauthorized"));
        }
    }


}
