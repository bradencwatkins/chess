package handler;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import request.JoinGameRequest;
import result.MessageResult;
import service.AlreadyTakenException;
import service.JoinGameService;
import service.UnauthorizedException;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.Objects;

public class JoinGameHandler implements Route {
    private final Gson gson = new Gson();

    public Object handle(Request req, Response res){
        try {
            JoinGameService joinGameService = new JoinGameService();
            JoinGameRequest joinGameRequest = gson.fromJson(req.body(), JoinGameRequest.class);
            String authToken = req.headers("Authorization");

            if ((authToken == null || authToken.isBlank()) ||
                    (!"BLACK".equalsIgnoreCase(joinGameRequest.playerColor()) &&
                            !"WHITE".equalsIgnoreCase(joinGameRequest.playerColor()))){
                res.status(400);
                return gson.toJson(new MessageResult("Error: Bad request"));
            }

            joinGameService.joinGame(authToken, joinGameRequest.playerColor(), joinGameRequest.gameID());
            res.status(200);
            return gson.toJson(new MessageResult("Game joined Successfully"));
        }
        catch (AlreadyTakenException e) {
            res.status(403);
            return gson.toJson(new MessageResult("Error: " + e.getMessage()));
        }
        catch (UnauthorizedException u){
            res.status(401);
            return gson.toJson(new MessageResult("Error: Unauthorized"));
        }
        catch (DataAccessException e){
            res.status(500);
            return gson.toJson(new MessageResult("Error: Data access error"));
        } catch (RuntimeException e) {
            res.status(500);
            return gson.toJson(new MessageResult("Error: " + e.getMessage()));
        }
        catch (Exception e){
            res.status(400);
            return gson.toJson(new MessageResult("Error: " + e.getMessage()));
        }


    }

}
