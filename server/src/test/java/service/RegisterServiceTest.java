package service;

import org.junit.jupiter.api.Test;
import request.RegisterRequest;
import result.RegisterResult;

import static org.junit.jupiter.api.Assertions.*;

public class RegisterServiceTest {

    @Test
    public void testRegisterSuccess() throws AlreadyTakenException {
        RegisterService service = new RegisterService();
        RegisterRequest request = new RegisterRequest("yo", "mama", "yomama@gmail");


        RegisterResult result = service.register(request);

        assertNotNull(result);
        assertNotNull(result.authToken());
        assertEquals("yo", result.username());

    }

    @Test
    public void testDuplicateRegistration() throws AlreadyTakenException {
        RegisterService service = new RegisterService();
        RegisterRequest request = new RegisterRequest("yo", "mama", "yomama@gmail");
        service.register(request);

        RegisterRequest request2 = new RegisterRequest("yo", "mama", "yomama@gmail");

        assertThrows(AlreadyTakenException.class, () -> {
            service.register(request2);
        });
    }
}
