package hu.ponte.homework.pontevotehomework.service;

import hu.ponte.homework.pontevotehomework.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserServiceTest {


    @Autowired
    private UserService userService;


    @Test
    @Sql("/insert.user.for.test")
    void test_executeRefreshingUsableVotesWhenUserWithIdThreeAlreadyVotedDuringADay() {
        User user = userService.getUser(3L);
        assertEquals(0, user.getUsableVotes());
        userService.executeRefreshingUsableVotes();
        User afterRefreshing = userService.getUser(3L);
        assertEquals(1, afterRefreshing.getUsableVotes());

    }

}