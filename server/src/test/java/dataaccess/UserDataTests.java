package dataaccess;

import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserDataTests {


    @BeforeEach
    void setUp() throws DataAccessException {
        DataAccess dataAccess = new MySqlDataAccess();
        dataAccess.clearUsers();
    }

    @Test
    void testCreateUserPositive() throws DataAccessException {
        UserData user = new UserData("testUser", "pass123", "test@example.com");
        DataAccess dataAccess = new MySqlDataAccess();
        UserData result = dataAccess.createUser(user);
        assertEquals(user.username(), result.username());
    }

    @Test
    void testCreateUserNegativeDuplicateUsername() throws DataAccessException {
        UserData user = new UserData("testUser", "pass123", "test@example.com");
        DataAccess dataAccess = new MySqlDataAccess();
        dataAccess.createUser(user);
        assertThrows(DataAccessException.class, () -> dataAccess.createUser(user));
        dataAccess.clearUsers();
    }

    @Test
    void testGetUserPositive() throws DataAccessException {
        UserData user = new UserData("newbie", "pass", "mail@x.com");
        DataAccess dataAccess = new MySqlDataAccess();
        dataAccess.createUser(user);
        UserData result = dataAccess.getUser("newbie");
        assertNotNull(result);
    }

    @Test
    void testGetUserNegativeNotFound() throws DataAccessException {
        DataAccess dataAccess = new MySqlDataAccess();
        assertNull(dataAccess.getUser("GhostGuy321"));
    }

}
