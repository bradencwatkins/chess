package handler;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import model.GameData;
import result.MessageResult;
import service.GetGameService;
import service.UnauthorizedException;
import spark.Request;
import spark.Response;
import spark.Route;

import static java.lang.System.out;

public class GetGameHandler implements Route {
    private final Gson gson = new Gson();

    public Object handle(Request req, Response res) {
        try {
            GetGameService service = new GetGameService();
            String authToken = req.headers("Authorization");
            if (authToken == null || authToken.isBlank()) {
                res.status(400);
                return gson.toJson(new MessageResult("Error: Missing authorization token"));
            }

            int gameID;
            try {
                gameID = Integer.parseInt(req.params("id"));
            } catch (NumberFormatException e) {
                throw new RuntimeException(e);
            }
            res.status(200);

            ChessGame chessGame = service.getGame(gameID, authToken);
            return gson.toJson(chessGame);

        } catch (UnauthorizedException e) {
            res.status(401);
            return gson.toJson(new MessageResult("Error: Unauthorized"));
        } catch (DataAccessException e) {
            res.status(500);
            return gson.toJson(new MessageResult("Error: Data access error"));
        } catch (RuntimeException e) {
            res.status(500);
            return gson.toJson(new MessageResult("Internal Server Error"));
        }

    }
}
