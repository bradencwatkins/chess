package handler;

import com.google.gson.Gson;
import request.RegisterRequest;
import result.RegisterResult;
import service.AlreadyTakenException;
import service.RegisterService;
import spark.Request;
import spark.Response;
import spark.Route;


//TAKES USER INPUT AND TURNS IT TO JSON AND PASSES IT TO SERVICE
public class RegisterHandler implements Route {
    private final RegisterService registerService = new RegisterService();
    private final Gson gson = new Gson();

    @Override
    public Object handle(Request req, Response res){
        System.out.println("register attempted");

        try {
            RegisterRequest registerRequest = gson.fromJson(req.body(), RegisterRequest.class);
            RegisterResult result = registerService.register(registerRequest);
            res.status(200);
            return gson.toJson(result);
        }
        catch (AlreadyTakenException e){
            res.status(403);
            return gson.toJson("Username already taken");
        }
    }

}
