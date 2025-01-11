package hu.ponte.homework.pontevotehomework.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.ponte.homework.pontevotehomework.domain.User;
import hu.ponte.homework.pontevotehomework.dto.outgoing.IdeaAuthorizeResponse;
import hu.ponte.homework.pontevotehomework.dto.outgoing.VoteStateDetailsListItem;
import hu.ponte.homework.pontevotehomework.service.JwtService;
import hu.ponte.homework.pontevotehomework.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AdminControllerTest {

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
    void test_listAllCurrentOrderAsc() throws Exception {
        String token = generateTokenForAdmin();
        MvcResult result = mockMvc.perform(get("http://localhost:8080/api/admin/list-all-current/order/asc")
                        .header("Authorization","Bearer "+ token))
                .andExpect(status().isOk())
                .andReturn();
        String responseContent = result.getResponse().getContentAsString();
        VoteStateDetailsListItem response = objectMapper.readValue(responseContent, VoteStateDetailsListItem.class);
        assertNotNull(response);
    }

    @Test
    @Sql("/insert.user.for.test")
    void test_listAllCurrentOrderDesc() throws Exception {
        String token = generateTokenForAdmin();
        MvcResult result = mockMvc.perform(get("http://localhost:8080/api/admin/list-all-current/order/desc")
                        .header("Authorization","Bearer "+ token))
                .andExpect(status().isOk())
                .andReturn();
        String responseContent = result.getResponse().getContentAsString();
        VoteStateDetailsListItem response = objectMapper.readValue(responseContent, VoteStateDetailsListItem.class);
        assertNotNull(response);
    }

    @Test
    @Sql("/insert.user.for.test")
    void test_listAllPending() throws Exception {
        String token = generateTokenForAdmin();
        MvcResult result = mockMvc.perform(get("http://localhost:8080/api/admin/list-all-pending")
                        .header("Authorization","Bearer "+ token))
                .andExpect(status().isOk())
                .andReturn();
        String responseContent = result.getResponse().getContentAsString();
        IdeaAuthorizeResponse response = objectMapper.readValue(responseContent, IdeaAuthorizeResponse.class);
        assertNotNull(response);
    }


    @Test
    @Sql("/insert.user.for.test")
    @Sql("/insert.idea.for.test")
    void test_acceptIdea() throws Exception {
        String token = generateTokenForAdmin();
        Long id = 4L;
        MvcResult result = mockMvc.perform(put("http://localhost:8080/api/admin/accept-idea/{id}",id)
                        .header("Authorization","Bearer "+ token))
                .andExpect(status().isOk())
                .andReturn();
        String responseContent = result.getResponse().getContentAsString();
        IdeaAuthorizeResponse response = objectMapper.readValue(responseContent, IdeaAuthorizeResponse.class);
        assertNotNull(response);
    }


    @Test
    @Sql("/insert.user.for.test")
    @Sql("/insert.idea.for.test")
    void test_declineIdea() throws Exception {
        String token = generateTokenForAdmin();
        Long id = 4L;
        MvcResult result = mockMvc.perform(put("http://localhost:8080/api/admin/decline-idea/{id}",id)
                        .header("Authorization","Bearer "+ token))
                .andExpect(status().isOk())
                .andReturn();
        String responseContent = result.getResponse().getContentAsString();
        IdeaAuthorizeResponse response = objectMapper.readValue(responseContent, IdeaAuthorizeResponse.class);
        assertNotNull(response);
    }

    @Test
    @Sql("/insert.user.for.test")
    void test_WhenNormalUserTryToUseAdminEndpoints() throws Exception {
        String token = generateTokenForUser();
        Long id = 1L;
        mockMvc.perform(put("http://localhost:8080/api/admin/decline-idea/{id}",id)
                .header("Authorization","Bearer "+ token))
                .andExpect(status().isForbidden());

        mockMvc.perform(put("http://localhost:8080/api/admin/accept-idea/{id}",id)
                        .header("Authorization","Bearer "+ token))
                .andExpect(status().isForbidden());

        mockMvc.perform(put("http://localhost:8080/api/admin/list-all-pending")
                        .header("Authorization","Bearer "+ token))
                .andExpect(status().isForbidden());

        mockMvc.perform(put("http://localhost:8080/api/admin/list-all-current/order/desc")
                        .header("Authorization","Bearer "+ token))
                .andExpect(status().isForbidden());

        mockMvc.perform(put("http://localhost:8080/api/admin/list-all-current/order/asc")
                        .header("Authorization","Bearer "+ token))
                .andExpect(status().isForbidden());
    }




    private String generateTokenForAdmin(){
        User admin = userService.getUser(4L);
        return jwtService.generateToken(admin);
    }

    private String generateTokenForUser(){
        User user = userService.getUser(1L);
        return jwtService.generateToken(user);
    }



}