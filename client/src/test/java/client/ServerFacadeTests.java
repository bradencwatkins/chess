package client;

import ServerFacade.ServerFacadeHandler;
import ServerFacade.ServerFacade;
import chess.ChessGame;
import org.junit.jupiter.api.*;
import server.Server;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacadeHandler handler;


    @BeforeAll
    public static void init() {
        server = new Server();
        int port = server.run(0);
        System.out.println("Started test HTTP server on port " + port);
        handler = new ServerFacadeHandler(new ServerFacade("http://localhost:" + port));
    }

    @BeforeEach
    public void clear() throws Exception {
        handler.clear();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @Test
    public void testRegisterSuccess() {
        String[] input = {"register", "user1", "pass1", "email1@example.com"};
        boolean result = handler.registerHandler(input);
        Assertions.assertTrue(result, "User should be registered successfully");
    }

    @Test
    public void testRegisterDuplicateUsername() {
        String[] input = {"register", "user1", "pass1", "email1@example.com"};
        handler.registerHandler(input);  // First registration
        boolean result = handler.registerHandler(input);  // Duplicate
        Assertions.assertFalse(result, "Duplicate username should fail registration");
    }

    @Test
    public void testLoginSuccess() {
        handler.registerHandler(new String[]{"register", "user2", "pass2", "email2@example.com"});
        boolean result = handler.loginHandler(new String[]{"login", "user2", "pass2"});
        Assertions.assertTrue(result, "Login should succeed for correct credentials");
    }

    @Test
    public void testLoginInvalidCredentials() {
        handler.registerHandler(new String[]{"register", "user3", "pass3", "email3@example.com"});
        boolean result = handler.loginHandler(new String[]{"login", "user3", "wrongpass"});
        Assertions.assertFalse(result, "Login should fail for incorrect password");
    }

    @Test
    public void testLogout() {
        handler.registerHandler(new String[]{"register", "user4", "pass4", "email4@example.com"});
        handler.loginHandler(new String[]{"login", "user4", "pass4"});
        Assertions.assertDoesNotThrow(() -> handler.logoutHandler(), "Logout should not throw an exception");
    }

    @Test
    public void testCreateGame() {
        handler.registerHandler(new String[]{"register", "user5", "pass5", "email5@example.com"});
        handler.loginHandler(new String[]{"login", "user5", "pass5"});
        Assertions.assertDoesNotThrow(() -> handler.createGameHandler(new String[]{"create", "Cool Game"}), "Creating game should succeed");
    }

    @Test
    public void testListGamesSuccess() {
        handler.registerHandler(new String[]{"register", "user6", "pass6", "email6@example.com"});
        handler.loginHandler(new String[]{"login", "user6", "pass6"});
        handler.createGameHandler(new String[]{"create", "GameToList"});

        Assertions.assertDoesNotThrow(() -> handler.listGameHandler(), "Listing games should not throw an exception");
    }

    @Test
    public void testListGamesUnauthorized() {
        Assertions.assertDoesNotThrow(() -> handler.listGameHandler(), "Listing games without login should not throw, but may print an error");
    }

    @Test
    public void testJoinGameSuccess() {
        handler.registerHandler(new String[]{"register", "user7", "pass7", "email7@example.com"});
        handler.loginHandler(new String[]{"login", "user7", "pass7"});
        handler.createGameHandler(new String[]{"create", "JoinableGame"});

        // Assume gameID is 1 for simplicity
        Assertions.assertDoesNotThrow(() -> handler.joinGameHandler(new String[]{"join", "1", "white"}), "Joining a game should succeed");
    }

    @Test
    public void testJoinGameInvalidGameID() {
        handler.registerHandler(new String[]{"register", "user8", "pass8", "email8@example.com"});
        handler.loginHandler(new String[]{"login", "user8", "pass8"});

        Assertions.assertDoesNotThrow(() ->
                handler.joinGameHandler(new String[]{"join", "99999", "black"}), "Joining a non-existent game should print error but not throw"
        );
    }

    @Test
    public void testGetGameSuccess() {
        handler.registerHandler(new String[]{"register", "user9", "pass9", "email9@example.com"});
        handler.loginHandler(new String[]{"login", "user9", "pass9"});
        handler.createGameHandler(new String[]{"create", "GettableGame"});

        // Assume gameID is 1
        handler.joinGameHandler(new String[]{"join", "1", "white"});
        ChessGame game = handler.getGameHandler(1);
        Assertions.assertNotNull(game, "Should retrieve game state successfully");
    }

    @Test
    public void testGetGameInvalidID() {
        ChessGame game = handler.getGameHandler(99999);
        Assertions.assertNull(game, "Should return null for invalid game ID");
    }

    @Test
    public void testObserveGameSuccess() {
        handler.registerHandler(new String[]{"register", "user10", "pass10", "email10@example.com"});
        handler.loginHandler(new String[]{"login", "user10", "pass10"});
        handler.createGameHandler(new String[]{"create", "ObservableGame"});

        Assertions.assertDoesNotThrow(() ->
                handler.observeGameHandler(new String[]{"observe", "1"}), "Observing a game should succeed"
        );
    }

    @Test
    public void testObserveGameInvalidID() {
        Assertions.assertDoesNotThrow(() ->
                handler.observeGameHandler(new String[]{"observe", "99999"}), "Observing a non-existent game should print error but not throw"
        );
    }

}
