package service;

import dataaccess.UserDAO;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.RegisterRequest;

import static org.junit.jupiter.api.Assertions.*;

public class ClearServiceTest {

    private final RegisterService registerService = new RegisterService();
    private final LoginService loginService = new LoginService();
    private final UserDAO userDAO = new UserDAO();
    private final ClearService clearService = new ClearService();


    @BeforeEach
    public void setup() throws Exception{
        clearService.clearData();
        RegisterRequest request = new RegisterRequest("user", "pass", "email");
        registerService.register(request);
    }

    @Test
    public void testClearDataSuccess() throws Exception {
        assertNotNull(userDAO.getUser("user"));

        ClearService service = new ClearService();
        service.clearData();

        assertNull(userDAO.getUser("user"), "User data should be cleared");
    }

    @Test
    public void testClearHandlesNoData() {
        ClearService service = new ClearService();

        assertDoesNotThrow(service::clearData, "Clearing empty database should not throw");
    }
}
