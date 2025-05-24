package handler;
import com.google.gson.Gson;
import result.MessageResult;
import service.ListGameService;
import service.UnauthorizedException;
import spark.*;

public class ListGameHandler implements Route {
    private final ListGameService listGameService = new ListGameService();
    private final Gson gson = new Gson();

    public Object handle(Request req, Response res){

        try{

        }
        catch (UnauthorizedException u){
            res.status(401);
            return gson.toJson(new MessageResult("Error : unauthorized"));
        }

    }


}
