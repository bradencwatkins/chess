package handler;

import com.google.gson.Gson;
import result.MessageResult;
import service.ClearService;
import spark.*;

public class ClearHandler implements Route {
    private final Gson gson = new Gson();
    private final ClearService clearService = new ClearService();

    public Object handle(Request req, Response res) {
        try {
            clearService.clearData();
            res.status(200);
            return gson.toJson(new MessageResult("Database cleared successfully"));
        } catch(RuntimeException r) {
            res.status(500);
            return gson.toJson(new MessageResult("Error clearing database"));
        }
    }

}
