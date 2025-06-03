package service;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import dataaccess.MySqlDataAccess;
import dataaccess.UserDAO;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.RegisterRequest;

import static org.junit.jupiter.api.Assertions.*;

public class ClearServiceTest {
    private final DataAccess dataAccess;
    private final RegisterService registerService = new RegisterService();
    private final LoginService loginService = new LoginService();
    private final ClearService clearService = new ClearService();

    public ClearServiceTest() {
        try {
            this.dataAccess = new MySqlDataAccess();
        } catch (DataAccessException e) {
            throw new RuntimeException("clear service failed");
        }
    }

    @BeforeEach
    public void setup() throws Exception{
        clearService.clearData();
        RegisterRequest request = new RegisterRequest("user", "pass", "email");
        registerService.register(request);
    }

    @Test
    public void testClearDataSuccess() throws Exception {
        assertNotNull(dataAccess.getUser("user"));

        ClearService service = new ClearService();
        service.clearData();

        assertNull(dataAccess.getUser("user"), "User data should be cleared");
    }

    @Test
    public void testClearHandlesNoData() {
        ClearService service = new ClearService();

        assertDoesNotThrow(service::clearData, "Clearing empty database should not throw");
    }
}
