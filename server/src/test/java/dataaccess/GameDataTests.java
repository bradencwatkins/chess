package dataaccess;

import chess.ChessGame;
import model.GameData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import result.GameMetadata;

import static org.junit.jupiter.api.Assertions.*;

public class GameDataTests {
    private final DataAccess dataAccess;

    public GameDataTests() {
        try {
            this.dataAccess = new MySqlDataAccess();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    void setUp() throws DataAccessException {
        DataAccess dataAccess = new MySqlDataAccess();
        dataAccess.clearGames();
    }

    @Test
    void testCreateGamePositive() throws DataAccessException {
        GameData game = new GameData(0, null, null, "TestGame", new ChessGame());
        GameData created = dataAccess.createGame(game);
        assertTrue(created.gameID() > 0);
    }

    @Test
    void testCreateGameNegativeNullChessGame() throws DataAccessException {
        GameData game = new GameData(1, null, null, "TestGame", new ChessGame());
        GameData created = dataAccess.createGame(game);
        assertTrue(created.gameID() > 0);
    }

    @Test
    void testGetGamePositive() throws DataAccessException {
        GameData game = dataAccess.createGame(new GameData(0, null, null, "FetchMe", new ChessGame()));
        GameData fetched = dataAccess.getGame(game.gameID());
        assertEquals("FetchMe", fetched.gameName());
    }

    @Test
    void testGetGameNegativeNotFound() throws DataAccessException {
        assertNull(dataAccess.getGame(9999));
    }

    @Test
    void testGetGamesDataPositive() throws DataAccessException {
        dataAccess.createGame(new GameData(0, null, null, "ListMe", new ChessGame()));
        GameMetadata[] games = dataAccess.getGamesData();
        assertTrue(games.length > 0);
    }

    @Test
    void testUpdateGamePositive() throws DataAccessException {
        GameData game = dataAccess.createGame(new GameData(0, null, null, "UpdGame", new ChessGame()));
        dataAccess.updateGame("WHITE", "whitePlayer", game.gameID());
        GameData updated = dataAccess.getGame(game.gameID());
        assertEquals("whitePlayer", updated.whiteUsername());
    }

    @Test
    void testUpdateGameNegative_invalidID() {
        assertThrows(NullPointerException.class, () -> dataAccess.updateGame("WHITE", "userX", -1));
    }

}
