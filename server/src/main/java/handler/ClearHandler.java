package handler;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import result.MessageResult;
import service.ClearService;
import spark.*;

public class ClearHandler implements Route {
    private final Gson gson = new Gson();


    public Object handle(Request req, Response res) {
        try {
            ClearService clearService = new ClearService();
            clearService.clearData();
            res.status(200);
            return gson.toJson(new MessageResult("Database cleared successfully"));
        } catch (DataAccessException e){
            res.status(500);
            return gson.toJson(new MessageResult("Error accessing database to clear"));
        } catch(RuntimeException r) {
            res.status(500);
            return gson.toJson(new MessageResult("Error clearing database"));
        }

    }

}
