package hu.ponte.homework.pontevotehomework.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.ponte.homework.pontevotehomework.domain.User;
import hu.ponte.homework.pontevotehomework.dto.income.IdeaCommand;
import hu.ponte.homework.pontevotehomework.dto.income.VoteCommand;
import hu.ponte.homework.pontevotehomework.dto.outgoing.AllAcceptedIdeaListItem;
import hu.ponte.homework.pontevotehomework.dto.outgoing.IdeaResponse;
import hu.ponte.homework.pontevotehomework.dto.outgoing.VoteResponse;
import hu.ponte.homework.pontevotehomework.service.JwtService;
import hu.ponte.homework.pontevotehomework.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class VoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserService userService;


    @Test
    @Sql("/insert.user.for.test")
    @Sql("/insert.idea.for.test")
    void test_voteOnIdea_WhenUserTryToVoteOnHisOwnIdea() throws Exception {
        String token = generateTokenForUser();

        VoteCommand command = new VoteCommand();
        command.setIdeaToVoteOn("Idea4");

        HttpServletRequest request = new MockHttpServletRequest();
        MockHttpSession session = (MockHttpSession) request.getSession();
        assert session != null;
        session.setAttribute("userId", 1L);

        String body = objectMapper.writeValueAsString(command);
        MvcResult result = mockMvc.perform(put("http://localhost:8080/api/idea/vote-on-idea")
                        .header("Authorization", "Bearer " + token)
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andReturn();
        String responseContent = result.getResponse().getContentAsString();
        VoteResponse response = objectMapper.readValue(responseContent, VoteResponse.class);
        assertNotNull(response);
    }

    @Test
    @Sql("/insert.user.for.test")
    @Sql("/insert.idea.for.test")
    void test_voteOnIdea_WhenEveryInputValid() throws Exception {
        String token = generateTokenForUser();

        VoteCommand command = new VoteCommand();
        command.setIdeaToVoteOn("Idea4");

        HttpServletRequest request = new MockHttpServletRequest();
        MockHttpSession session = (MockHttpSession) request.getSession();
        assert session != null;
        session.setAttribute("userId", 1L);

        String body = objectMapper.writeValueAsString(command);
        MvcResult result = mockMvc.perform(put("http://localhost:8080/api/idea/vote-on-idea")
                        .header("Authorization", "Bearer " + token)
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andReturn();
        String responseContent = result.getResponse().getContentAsString();
        VoteResponse response = objectMapper.readValue(responseContent, VoteResponse.class);
        assertNotNull(response);
    }

    @Test
    @Sql("/insert.user.for.test")
    @Sql("/insert.idea.for.test")
    void test_getAllPendingIdea() throws Exception {
        String token = generateTokenForUser();
        MvcResult result = mockMvc.perform(get("http://localhost:8080/api/idea/all-pending-idea")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andReturn();
        String responseContent = result.getResponse().getContentAsString();
        AllAcceptedIdeaListItem response = objectMapper.readValue(responseContent, AllAcceptedIdeaListItem.class);
        assertNotNull(response);
    }

    @Test
    @Sql("/insert.user.for.test")
    void test_makeIdea_WhenEveryInputValid() throws Exception {
        String token = generateTokenForUser();
        IdeaCommand command = new IdeaCommand();
        command.setIdea("Another Idea");

        HttpServletRequest request = new MockHttpServletRequest();
        MockHttpSession session = (MockHttpSession) request.getSession();
        assert session != null;
        session.setAttribute("userId", 1L);

        String body = objectMapper.writeValueAsString(command);
        MvcResult result = mockMvc.perform(post("http://localhost:8080/api/idea/make-idea")
                        .header("Authorization", "Bearer " + token)
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andReturn();
        String responseContent = result.getResponse().getContentAsString();
        IdeaResponse response = objectMapper.readValue(responseContent, IdeaResponse.class);
        assertNotNull(response);
    }


    @Test
    @Sql("/insert.user.for.test")
    @Sql("/insert.idea.for.test")
    void test_makeIdea_WhenTheIdeaIsAlreadyInDataBase() throws Exception {
        String token = generateTokenForUser();
        IdeaCommand command = new IdeaCommand();
        command.setIdea("Idea");

        HttpServletRequest request = new MockHttpServletRequest();
        MockHttpSession session = (MockHttpSession) request.getSession();
        assert session != null;
        session.setAttribute("userId", 1L);

        String body = objectMapper.writeValueAsString(command);
        mockMvc.perform(post("http://localhost:8080/api/idea/make-idea")
                        .header("Authorization", "Bearer " + token)
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].field").value("idea"))
                .andExpect(jsonPath("$.errors[0].message").value("Idea is already exits try something else."))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Sql("/insert.user.for.test")
    void test_WhenAdminTryToUseRegularEndpointsExpectAllForbidden() throws Exception {
        String token = generateTokenForAdmin();

        mockMvc.perform(post("http://localhost:8080/api/idea/make-idea")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());

        mockMvc.perform(get("http://localhost:8080/api/idea/all-pending-idea")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());

        mockMvc.perform(put("http://localhost:8080/api/idea/vote-on-idea")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());


    }


    private String generateTokenForAdmin() {
        User admin = userService.getUser(4L);
        return jwtService.generateToken(admin);
    }

    private String generateTokenForUser() {
        User user = userService.getUser(1L);
        return jwtService.generateToken(user);
    }
}