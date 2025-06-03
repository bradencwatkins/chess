package dataaccess;

import model.AuthData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AuthDataTests {
    private final DataAccess dataAccess;

    public AuthDataTests() {
        try {
            this.dataAccess = new MySqlDataAccess();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testCreateAuthPositive() throws DataAccessException {
        DataAccess dataAccess = new MySqlDataAccess();
        AuthData auth = new AuthData("exampleToken", "userGuy");
        AuthData result = dataAccess.createAuth(auth);
        assertEquals(auth.authToken(), result.authToken());
    }

    @Test
    void testCreateAuthNegativeDuplicateToken() throws DataAccessException {
        AuthData auth = new AuthData("exampleToken", "userGuy");
        DataAccess dataAccess = new MySqlDataAccess();
        AuthData result = dataAccess.createAuth(auth);
        assertEquals(auth.authToken(), result.authToken());
        dataAccess.clearAuth();
    }

    @Test
    void testGetAuthPositive() throws DataAccessException {
        AuthData auth = new AuthData("tokenABC", "userX");
        DataAccess dataAccess = new MySqlDataAccess();
        dataAccess.createAuth(auth);
        AuthData result = dataAccess.getAuth("tokenABC");
        assertNotNull(result);
    }

    @Test
    void testGetAuthNegative_notFound() throws DataAccessException {
        DataAccess dataAccess = new MySqlDataAccess();
        assertNull(dataAccess.getAuth("invalidToken"));
    }

    @Test
    void testGetUsernameByTokenPositive() throws DataAccessException {
        DataAccess dataAccess = new MySqlDataAccess();
        AuthData auth = new AuthData("tokenXYZ", "userZ");
        dataAccess.createAuth(auth);
        String username = dataAccess.getUsernameByToken("tokenXYZ");
        assertEquals("userZ", username);
    }

    @Test
    void testGetUsernameByTokenNegative_invalidToken() {
        assertThrows(DataAccessException.class, () -> dataAccess.getUsernameByToken("badToken"));
    }

    @Test
    void testDeleteAuthPositive() throws DataAccessException {
        AuthData auth = new AuthData("deleteMe", "userY");
        dataAccess.createAuth(auth);
        dataAccess.deleteAuth("deleteMe");
        assertNull(dataAccess.getAuth("deleteMe"));
    }


}
