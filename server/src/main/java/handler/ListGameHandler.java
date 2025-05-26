package handler;
import com.google.gson.Gson;
import result.GameMetadata;
import result.ListGameResult;
import result.MessageResult;
import service.ListGameService;
import service.UnauthorizedException;
import spark.*;

public class ListGameHandler implements Route {
    private final ListGameService listGameService = new ListGameService();
    private final Gson gson = new Gson();

    public Object handle(Request req, Response res){

        try{
            String authToken = req.headers("Authorization");
            if (authToken == null || authToken.isBlank()) {
                res.status(400);
                return gson.toJson(new MessageResult("Error: Bad request"));
            }

            GameMetadata[] gamesList = listGameService.listGames(authToken);


            res.status(200);
            if (gamesList == null){
                return gson.toJson(new ListGameResult(new GameMetadata[0]));
            }
            return gson.toJson(new ListGameResult(gamesList));


        }
        catch (UnauthorizedException u){
            res.status(401);
            return gson.toJson(new MessageResult("Error : unauthorized"));
        }

    }


}
