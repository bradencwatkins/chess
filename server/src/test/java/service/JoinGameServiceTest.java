package service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.CreateGameRequest;
import request.LoginRequest;
import request.RegisterRequest;
import result.CreateGameResult;
import result.LoginResult;

import static org.junit.jupiter.api.Assertions.*;

public class JoinGameServiceTest {
    private final RegisterService registerService = new RegisterService();
    private final LoginService loginService = new LoginService();
    private final JoinGameService joinGameService = new JoinGameService();
    private String validAuthToken;
    private final CreateGameService createGameService = new CreateGameService();
    private int createdGameID;

    public void setup() throws Exception{
        RegisterRequest request = new RegisterRequest("user", "pass", "email");
        registerService.register(request);
        LoginRequest loginRequest = new LoginRequest("user","pass");
        LoginResult loginResult = loginService.login(loginRequest);
        validAuthToken = loginResult.authToken();

        CreateGameRequest createRequest = new CreateGameRequest(1, "MyGame");
        CreateGameResult createResult = createGameService.createGame(validAuthToken, createRequest);
        createdGameID = createResult.gameID();
    }

    @Test
    public void testJoinGameSuccess() throws Exception {
        setup();
        assertDoesNotThrow(() -> {
            joinGameService.joinGame(validAuthToken, "WHITE", createdGameID);
        });
    }

    @Test
    public void testJoinGameInvalidAuthToken() {
        Exception exception = assertThrows(UnauthorizedException.class, () -> {
            joinGameService.joinGame("invalid-token", "WHITE", createdGameID);
        });

        assertEquals("Token does not exist", exception.getMessage());
    }


}
