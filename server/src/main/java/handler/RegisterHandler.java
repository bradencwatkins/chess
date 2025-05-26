package handler;

import com.google.gson.Gson;
import request.RegisterRequest;
import result.MessageResult;
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

        try {
            RegisterRequest registerRequest = gson.fromJson(req.body(), RegisterRequest.class);
            System.out.println(registerRequest.email());

            //CHECK IF THE USERS INPUT WAS BLANK OR NULL
            if (registerRequest == null || registerRequest.username() == null ||
                registerRequest.username().isBlank() || registerRequest.password() == null ||
                registerRequest.password().isBlank() || registerRequest.email() == null ||
                registerRequest.email().isBlank()) {

                res.status(400);
                return gson.toJson(new MessageResult("Error: bad request"));
            }

            //SUCCESSFUL REGISTRATION
            RegisterResult result = registerService.register(registerRequest);
            res.status(200);
            return gson.toJson(result);
        }
        catch (AlreadyTakenException e){
            res.status(403);
            return gson.toJson(new MessageResult("Error: Username already taken"));
        } catch (Exception e) {
            res.status(500);
            return gson.toJson(new MessageResult("Error: Internal server error"));
        }
    }

}
