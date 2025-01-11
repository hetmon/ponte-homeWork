package hu.ponte.homework.pontevotehomework.service;

import hu.ponte.homework.pontevotehomework.domain.Idea;
import hu.ponte.homework.pontevotehomework.domain.IdeaStatus;
import hu.ponte.homework.pontevotehomework.dto.income.VoteCommand;
import hu.ponte.homework.pontevotehomework.dto.outgoing.*;
import hu.ponte.homework.pontevotehomework.exception.IdeaNotExistsException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.time.Duration;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AdminServiceTest {

    @Autowired
    private AdminService adminService;

    @Autowired
    private IdeaService ideaService;

    @Test
    @Sql("/insert.user.for.test")
    @Sql("/insert.idea.for.test")
    void test_accepting_Ideas() {

        Idea idea = ideaService.unwrapIdea("Idea2");
        assertEquals(IdeaStatus.WAITING_FOR_RESPOND, idea.getStatus());
        IdeaAuthorizeResponse response = adminService.accepting(2L);
        assertNotNull(response);
        assertEquals("Idea accepted", response.getMessage());
        ZonedDateTime timeFromResponse = ZonedDateTime.parse(response.getTimeStamp());
        long diff = Duration.between(ZonedDateTime.now(), timeFromResponse).toMillis();
        assertTrue(diff < 300);
        assertEquals("Idea3", response.getIdeaText());
        assertEquals("200 OK", response.getHttpStatus());
        Idea ideaAfterAccept = ideaService.unwrapIdea("Idea3");
        assertEquals(IdeaStatus.ACCEPTED, ideaAfterAccept.getStatus());
    }

    @Test
    @Sql("/insert.user.for.test")
    @Sql("/insert.idea.for.test")
    void test_accepting_IdeaWhenIdeaWithIdNotExits() {

        Exception exc = assertThrows(IdeaNotExistsException.class, () ->
                adminService.accepting(-1L));
        String errorMessage = "Idea with id: -1 does not exist";
        assertEquals(errorMessage, exc.getMessage());
    }

    @Test
    @Sql("/insert.user.for.test")
    @Sql("/insert.idea.for.test")
    void test_decline_Ideas() {

        Idea idea = ideaService.unwrapIdea("Idea2");
        assertEquals(IdeaStatus.WAITING_FOR_RESPOND, idea.getStatus());
        IdeaAuthorizeResponse response = adminService.decline(2L);
        assertNotNull(response);
        assertEquals("Idea declined", response.getMessage());
        ZonedDateTime timeFromResponse = ZonedDateTime.parse(response.getTimeStamp());
        long diff = Duration.between(ZonedDateTime.now(), timeFromResponse).toMillis();
        assertTrue(diff < 300);
        assertEquals("Idea3", response.getIdeaText());
        assertEquals("200 OK", response.getHttpStatus());
        Idea ideaAfterAccept = ideaService.unwrapIdea("Idea3");
        assertEquals(IdeaStatus.DECLINED, ideaAfterAccept.getStatus());
    }

    @Test
    @Sql("/insert.user.for.test")
    @Sql("/insert.idea.for.test")
    void test_decline_IdeaWhenIdeaWithIdNotExits() {

        Exception exc = assertThrows(IdeaNotExistsException.class, () ->
                adminService.decline(-1L));
        String errorMessage = "Idea with id: -1 does not exist";
        assertEquals(errorMessage, exc.getMessage());
    }

    @Test
    @Sql("/insert.user.for.test")
    @Sql("/insert.idea.for.test")
    void test_getAllPending_WhenThereIsOnePending() {

        WaitingStatusIdeaListItem response = adminService.getAllPending();
        assertNotNull(response);
        assertEquals("All idea what waiting for respond", response.getInfo());
        assertEquals(1, response.getIdeas().size());
        IdeasWithWaitingStatusDetails listItem = response.getIdeas().get(0);
        assertEquals(4L, listItem.getId());
        assertEquals("Idea2", listItem.getIdeaText());
        assertEquals("2025-02-17T03:53:48.057003Z", listItem.getExpireAt());
        assertEquals("2025-01-10T03:53:48.057003Z", listItem.getTimeOfMake());
        assertEquals(1, listItem.getTotalVotes());
        assertEquals("WAITING_FOR_RESPOND", listItem.getStatus());

    }

    @Test
    @Sql("/insert.user.for.test")
    @Sql("/insert.idea.for.test")
    void test_getAllLive_WithAscendingOrder() {

        vote();
        VoteStateDetailsListItem response = adminService.getAllLive(true);
        assertNotNull(response);
        assertEquals("All all pending idea with information", response.getInfo());
        assertEquals(3, response.getDetails().size());

        IdeaVoteStateDetails firstElement = response.getDetails().get(0);
        IdeaVoteStateDetails secondElement = response.getDetails().get(1);
        IdeaVoteStateDetails thirdElement = response.getDetails().get(2);

        assertEquals(firstElement.getDetailsList().size(), 0);
        assertEquals(2, firstElement.getTotalVotes());
        assertEquals(firstElement.getExpireAt(), "2025-02-17T03:53:48.057003Z");
        assertEquals(firstElement.getIdeaText(), "Idea");
        assertEquals(firstElement.getDetailsList().size(), 0);


        assertEquals(secondElement.getDetailsList().size(), 0);
        assertEquals(3, secondElement.getTotalVotes());
        assertEquals(secondElement.getExpireAt(), "2025-02-17T03:53:48.057003Z");
        assertEquals(secondElement.getIdeaText(), "Idea3");
        assertEquals(secondElement.getDetailsList().size(), 0);

        assertEquals(thirdElement.getDetailsList().size(), 1);
        assertEquals(4, thirdElement.getTotalVotes());
        assertEquals(thirdElement.getExpireAt(), "2025-02-17T03:53:48.057003Z");
        assertEquals(thirdElement.getIdeaText(), "Idea4");
        assertEquals(thirdElement.getDetailsList().size(), 1);

        VoteDetails voteDetail = thirdElement.getDetailsList().get(0);
        assertEquals("Tomi Duro", voteDetail.getVoter());
        ZonedDateTime timeFromResponse = ZonedDateTime.parse(voteDetail.getVoteTimeStamp());
        long diff = Duration.between(ZonedDateTime.now(), timeFromResponse).toMillis();
        assertTrue(diff < 300);

    }

    @Test
    @Sql("/insert.user.for.test")
    @Sql("/insert.idea.for.test")
    void test_getAllLive_WithDescendingOrder() {
        vote();
        VoteStateDetailsListItem response = adminService.getAllLive(false);
        assertNotNull(response);
        assertEquals("All all pending idea with information", response.getInfo());
        assertEquals(3, response.getDetails().size());

        IdeaVoteStateDetails thirdElement = response.getDetails().get(0);
        IdeaVoteStateDetails secondElement = response.getDetails().get(1);
        IdeaVoteStateDetails firstElement = response.getDetails().get(2);

        assertEquals(firstElement.getDetailsList().size(), 0);
        assertEquals(2, firstElement.getTotalVotes());
        assertEquals(firstElement.getExpireAt(), "2025-02-17T03:53:48.057003Z");
        assertEquals(firstElement.getIdeaText(), "Idea");
        assertEquals(firstElement.getDetailsList().size(), 0);

        assertEquals(secondElement.getDetailsList().size(), 0);
        assertEquals(3, secondElement.getTotalVotes());
        assertEquals(secondElement.getExpireAt(), "2025-02-17T03:53:48.057003Z");
        assertEquals(secondElement.getIdeaText(), "Idea3");
        assertEquals(secondElement.getDetailsList().size(), 0);

        assertEquals(thirdElement.getDetailsList().size(), 1);
        assertEquals(4, thirdElement.getTotalVotes());
        assertEquals(thirdElement.getExpireAt(), "2025-02-17T03:53:48.057003Z");
        assertEquals(thirdElement.getIdeaText(), "Idea4");
        assertEquals(thirdElement.getDetailsList().size(), 1);

        VoteDetails voteDetail = thirdElement.getDetailsList().get(0);
        assertEquals("Tomi Duro", voteDetail.getVoter());
        ZonedDateTime timeFromResponse = ZonedDateTime.parse(voteDetail.getVoteTimeStamp());
        long diff = Duration.between(ZonedDateTime.now(), timeFromResponse).toMillis();
        assertTrue(diff < 300);

    }

    private void vote() {
        VoteCommand command = new VoteCommand();
        command.setIdeaToVoteOn("Idea4");
        ideaService.vote(command, 1L);
    }
}