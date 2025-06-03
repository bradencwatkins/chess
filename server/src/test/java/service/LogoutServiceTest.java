package service;

import org.junit.jupiter.api.Test;
import request.LoginRequest;
import request.RegisterRequest;
import result.LoginResult;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class LogoutServiceTest {

    private final RegisterService registerService = new RegisterService();
    private final LoginService loginService = new LoginService();
    private final LogoutService logoutService = new LogoutService();
    private final ClearService clearService = new ClearService();
    private String validAuthToken;

    public void setup() throws Exception{
        clearService.clearData();
        RegisterRequest req = new RegisterRequest("userGuy", "passwerd", "email");
        registerService.register(req);
        LoginRequest loginReq = new LoginRequest("userGuy","passwerd");
        LoginResult loginResult = loginService.login(loginReq);
        validAuthToken = loginResult.authToken();
    }

    @Test
    public void testLogoutSuccess() throws Exception {
        setup();
        // Should not throw an exception
        assertDoesNotThrow(() -> {
            logoutService.logout(validAuthToken);
        });
    }

    @Test
    public void testLogoutFailWithInvalidToken() {
        // Should throw an UnauthorizedException
        assertThrows(UnauthorizedException.class, () -> {
            logoutService.logout("invalid-token");
        });
    }




}
