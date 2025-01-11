package hu.ponte.homework.pontevotehomework.service;

import hu.ponte.homework.pontevotehomework.dto.income.RegisterRequest;
import hu.ponte.homework.pontevotehomework.dto.outgoing.AuthenticationResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Test
    void test_register_WhenInputsAreValidNoExceptionThrow() {
        RegisterRequest command = new RegisterRequest();
        command.setEmail("mial@1");
        command.setPassword("password");
        command.setFirstName("Nagy");
        command.setLastName("Laci");
        AuthenticationResponse response = authService.register(command);
        String message = "Register was successful. Welcome Nagy Laci Now you can login";
        assertNotNull(response);
        assertEquals(response.getMessage(), message);
        assertNotNull(response.getToken());
    }

}