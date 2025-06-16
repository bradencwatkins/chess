package handler;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import request.LeaveGameRequest;
import result.MessageResult;
import service.UnauthorizedException;
import service.LeaveGameService;
import spark.Request;
import spark.Response;
import spark.Route;

public class LeaveGameHandler implements Route {
    private final Gson gson = new Gson();

    @Override
    public Object handle(Request req, Response res) {
        try {
            LeaveGameService leaveGameService = new LeaveGameService();
            LeaveGameRequest leaveGameRequest = gson.fromJson(req.body(), LeaveGameRequest.class);
            String authToken = req.headers("Authorization");
            System.out.println("Auth header: " + req.headers("Authorization"));
            System.out.println("Request body: " + req.body());

//            if (authToken == null || authToken.isBlank() ||
//                    leaveGameRequest == null ||
//                    leaveGameRequest.Username() == null || leaveGameRequest.Username().isBlank() ||
//                    leaveGameRequest.GameID() == 0) {
//
//                res.status(400);
//                return gson.toJson(new MessageResult("Error: Bad request"));
//            }

            leaveGameService.leaveGame(authToken, leaveGameRequest.username(), leaveGameRequest.gameID());

            res.status(200);
            return gson.toJson(new MessageResult("Successfully left game"));
        }
        catch (UnauthorizedException e) {
            res.status(401);
            return gson.toJson(new MessageResult("Error: Unauthorized"));
        }
        catch (DataAccessException e){
            res.status(500);
            return gson.toJson(new MessageResult("Error: Data access error"));
        }
        catch (RuntimeException e) {
            res.status(500);
            return gson.toJson(new MessageResult("Error: " + e.getMessage()));
        }
        catch (Exception e){
            res.status(400);
            return gson.toJson(new MessageResult("Error: " + e.getMessage()));
        }
    }
}
