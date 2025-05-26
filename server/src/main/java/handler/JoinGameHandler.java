package handler;

import com.google.gson.Gson;
import request.JoinGameRequest;
import result.MessageResult;
import service.AlreadyTakenException;
import service.JoinGameService;
import spark.Request;
import spark.Response;
import spark.Route;

public class JoinGameHandler implements Route {
    private final JoinGameService joinGameService = new JoinGameService();
    private final Gson gson = new Gson();

    public Object handle(Request req, Response res){

        try {
            JoinGameRequest joinGameRequest = gson.fromJson(req.body(), JoinGameRequest.class);
            String authToken = req.headers("Authorization");

            joinGameService.joinGame(authToken, joinGameRequest.playerColor(), joinGameRequest.gameID());
            res.status(200);
            return gson.toJson(new MessageResult("Game joined Successfully"));
        }
        catch (AlreadyTakenException e) {
            res.status(403);
            return gson.toJson(new MessageResult("Error: Player color already taken"));
        }

    }

}
