package hu.ponte.homework.pontevotehomework.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.ponte.homework.pontevotehomework.dto.income.AuthenticationRequest;
import hu.ponte.homework.pontevotehomework.dto.income.RegisterRequest;
import hu.ponte.homework.pontevotehomework.dto.outgoing.AuthenticationResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthenticationControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    @Sql("/insert.user.for.test")
    @Sql("/insert.idea.for.test")
    void test_registerWhenInputIsValid() throws Exception {

        RegisterRequest command = new RegisterRequest();
        command.setFirstName("test");
        command.setLastName("test");
        command.setEmail("test@test.com");
        command.setPassword("password");

        String body = objectMapper.writeValueAsString(command);

        MvcResult result = mockMvc.perform(post("http://localhost:8080/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andReturn();
        String responseContent = result.getResponse().getContentAsString();
        AuthenticationResponse response = objectMapper.readValue(responseContent, AuthenticationResponse.class);
        assertNotNull(response);
    }

    @Test
    void test_registerWhenEmailIsNotFormattedWell() throws Exception {

        RegisterRequest command = new RegisterRequest();
        command.setFirstName("test");
        command.setLastName("test");
        command.setEmail("mail");
        command.setPassword("password");

        String body = objectMapper.writeValueAsString(command);
        mockMvc.perform(post("http://localhost:8080/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].field").value("email"))
                .andExpect(jsonPath("$.errors[0].message").value("must be a well-formed email address"));
    }

    @Test
    @Sql("/insert.user.for.test")
    void test_registerWhenEmailIsAlreadyExits() throws Exception {

        RegisterRequest command = new RegisterRequest();
        command.setFirstName("test");
        command.setLastName("test");
        command.setEmail("mail@mail.com");
        command.setPassword("password");

        String body = objectMapper.writeValueAsString(command);
        mockMvc.perform(post("http://localhost:8080/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].field").value("email"))
                .andExpect(jsonPath("$.errors[0].message").value("Email already registered."));
    }


    @Test
    @Sql("/insert.user.for.test")
    void test_authenticateWhenInputsAreValid() throws Exception {
        AuthenticationRequest command = new AuthenticationRequest();
        command.setPassword("pw");
        command.setEmail("mail@mail.com");

        String body = objectMapper.writeValueAsString(command);

        MvcResult result = mockMvc.perform(post("http://localhost:8080/api/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andReturn();
        String responseContent = result.getResponse().getContentAsString();
        AuthenticationResponse response = objectMapper.readValue(responseContent, AuthenticationResponse.class);
        assertNotNull(response);
    }


    @Test
    @Sql("/insert.user.for.test")
    void test_authenticateWhenThePasswordIsWrong() throws Exception {
        AuthenticationRequest command = new AuthenticationRequest();
        command.setPassword("notGoodPassword");
        command.setEmail("mail@mail.com");

        String body = objectMapper.writeValueAsString(command);

        mockMvc.perform(post("http://localhost:8080/api/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].field").value("password"))
                .andExpect(jsonPath("$.errors[0].message").value("Wrong password."))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Sql("/insert.user.for.test")
    void test_authenticateWhenInputEmailNotRegistered() throws Exception {

        AuthenticationRequest command = new AuthenticationRequest();
        command.setPassword("pw");
        command.setEmail("noEmail@Mail.com");

        String body = objectMapper.writeValueAsString(command);

        mockMvc.perform(post("http://localhost:8080/api/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].field").value("email"))
                .andExpect(jsonPath("$.errors[0].message").value("This email is not exists."))
                .andExpect(status().isBadRequest());
    }


}