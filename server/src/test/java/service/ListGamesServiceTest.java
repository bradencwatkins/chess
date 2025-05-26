package service;

import org.junit.jupiter.api.Test;
import request.CreateGameRequest;
import request.LoginRequest;
import request.RegisterRequest;
import result.GameMetadata;
import result.LoginResult;

import static org.junit.jupiter.api.Assertions.*;



public class ListGamesServiceTest {
    private final RegisterService registerService = new RegisterService();
    private final LoginService loginService = new LoginService();
    private final CreateGameService createGameService = new CreateGameService();
    private final ListGameService listGameService = new ListGameService();

    private String validAuthToken;

    public void setup() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest("user", "pass", "email");
        registerService.register(registerRequest);

        LoginRequest loginRequest = new LoginRequest("user", "pass");
        LoginResult loginResult = loginService.login(loginRequest);
        validAuthToken = loginResult.authToken();

        CreateGameRequest createGameRequest = new CreateGameRequest(1, "GameName");
        createGameService.createGame(validAuthToken, createGameRequest);
    }

    @Test
    public void testListGamesSuccess() throws Exception {
        setup();
        GameMetadata[] games = listGameService.listGames(validAuthToken);
        assertNotNull(games);
        assertTrue(games.length > 0, "Expected at least one game to be listed.");
    }

    @Test
    public void testListGamesInvalidToken() {
        Exception exception = assertThrows(UnauthorizedException.class, () -> {
            listGameService.listGames("invalid-token");
        });

        assertEquals("Token does not exist", exception.getMessage());
    }
}

