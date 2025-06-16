package server;

import handler.*;
import spark.*;
import websocket.WebSocketHandler;

import static java.lang.System.out;

public class Server {
    private final WebSocketHandler webSocketHandler;

    public Server() {
        webSocketHandler = new WebSocketHandler();
    }


    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", new RegisterHandler());
        Spark.delete("/db", new ClearHandler());
        Spark.post("/session", new LoginHandler());
        Spark.delete("/session", new LogoutHandler());
        Spark.post("/game", new CreateGameHandler());
        Spark.get("/game", new ListGameHandler());
        Spark.put("/game", new JoinGameHandler());
        Spark.get("/game/:id", new GetGameHandler());
        Spark.webSocket("/ws", WebSocketHandler.class);

        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
