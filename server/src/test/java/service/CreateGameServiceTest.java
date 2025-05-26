package service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.CreateGameRequest;
import request.LoginRequest;
import request.RegisterRequest;
import result.CreateGameResult;
import result.LoginResult;

import static org.junit.jupiter.api.Assertions.*;

public class CreateGameServiceTest {
    private final RegisterService registerService = new RegisterService();
    private final LoginService loginService = new LoginService();
    private final CreateGameService createGameService = new CreateGameService();

    private String validAuthToken;

    public void setup() throws Exception{
        RegisterRequest request = new RegisterRequest("user", "pass", "email");
        registerService.register(request);
        LoginRequest loginRequest = new LoginRequest("user","pass");
        LoginResult loginResult = loginService.login(loginRequest);
        validAuthToken = loginResult.authToken();
    }

    @Test
    public void testCreateGameSuccess() throws Exception {
        setup();
        CreateGameRequest createRequest = new CreateGameRequest(1, "name");
        CreateGameResult result = createGameService.createGame(validAuthToken, createRequest);

        assertNotNull(result);
        assertTrue(result.gameID() > 0, "Game ID should be positive");
    }

    @Test
    public void testCreateGameFailWithInvalidAuth() {
        CreateGameRequest createRequest = new CreateGameRequest(1, "game");

        assertThrows(UnauthorizedException.class, () -> {
            createGameService.createGame("invalid-token", createRequest);
        });
    }



}
