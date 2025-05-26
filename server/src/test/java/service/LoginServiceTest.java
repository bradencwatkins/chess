package service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.LoginRequest;
import request.RegisterRequest;
import result.LoginResult;

import static org.junit.jupiter.api.Assertions.*;

public class LoginServiceTest {

    private final RegisterService registerService = new RegisterService();
    private final LoginService loginService = new LoginService();
    private final ClearService clearService = new ClearService();


    public void setup() throws Exception{
        clearService.clearData();
        RegisterRequest request = new RegisterRequest("user", "pass", "email");
        registerService.register(request);
    }

    @Test
    public void testLoginSuccess() throws Exception{
        setup();
        LoginRequest request = new LoginRequest("user", "pass");

        LoginResult result = loginService.login(request);

        assertNotNull(result);
        assertNotNull(result.authToken());
        assertEquals("user", result.username());
    }

    @Test
    public void testWrongPassword() throws UnauthorizedException{
        LoginRequest request = new LoginRequest("user", "password");

        assertThrows(UnauthorizedException.class, () -> {
            loginService.login(request);
        });
    }
}
