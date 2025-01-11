package hu.ponte.homework.pontevotehomework.service;

import hu.ponte.homework.pontevotehomework.dto.income.AuthenticationRequest;
import hu.ponte.homework.pontevotehomework.dto.income.IdeaCommand;
import hu.ponte.homework.pontevotehomework.dto.income.VoteCommand;
import hu.ponte.homework.pontevotehomework.dto.outgoing.AllAcceptedIdeaListItem;
import hu.ponte.homework.pontevotehomework.dto.outgoing.IdeaResponse;
import hu.ponte.homework.pontevotehomework.dto.outgoing.MakeVoteDetails;
import hu.ponte.homework.pontevotehomework.dto.outgoing.VoteResponse;
import hu.ponte.homework.pontevotehomework.exception.InvalidVoteException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class IdeaServiceTest {


    @Autowired
    private AuthService authService;

    @Autowired
    private IdeaService ideaService;


    @Test
    @Sql("/insert.user.for.test")
    void test_make_IdeaWhenInputIsValidNoExceptionExpected() {
        AuthenticationRequest command = new AuthenticationRequest();
        command.setEmail("mail@mail.com");
        command.setPassword("pw");
        HttpServletRequest request = new MockHttpServletRequest();
        authService.authenticate(command, request);
        IdeaCommand ideaCommand = new IdeaCommand();
        ideaCommand.setIdea("idea");
        IdeaResponse ideaResponse = ideaService.makeIdea(ideaCommand, request);
        assertEquals("idea", ideaResponse.getIdeaText());
        assertEquals("Success, admins gonna accept or decline depends on your idea", ideaResponse.getMessage());
    }

    @Test
    @Sql("/insert.user.for.test")
    @Sql("/insert.idea.for.test")
    void test_getAllAccepted_WhenThereIsOnlyOneAcceptedIdea() {

        AllAcceptedIdeaListItem response = ideaService.getAllAccepted();
        assertNotNull(response);
        assertEquals("All accepted ideas and expire date", response.getInfo());
        assertEquals(3, response.getDetailsList().size());
        List<MakeVoteDetails> voteDetails = response.getDetailsList();
        MakeVoteDetails rawDetail = voteDetails.get(0);
        assertEquals("Idea", rawDetail.getIdea());
        assertEquals("2025-02-17T03:53:48.057003Z", rawDetail.getExpireDate());

    }

    @Test
    @Sql("/insert.user.for.test")
    @Sql("/insert.idea.for.test")
    void test_vote_WhenEveryInputIsValidNoExceptionExpected() {

        VoteCommand voteCommand = new VoteCommand();
        voteCommand.setIdeaToVoteOn("Idea");
        Long voterId = 2L;

        VoteResponse response = ideaService.vote(voteCommand, voterId);
        assertNotNull(response);
        assertEquals("Idea", response.getIdeaOnVoted());
        assertEquals("Vote successful", response.getMessage());
        ZonedDateTime timeFromResponse = ZonedDateTime.parse(response.getVoteTimeStamp());
        long diff = Duration.between(ZonedDateTime.now(), timeFromResponse).toMillis();
        assertTrue(diff < 300);
    }

    @Test
    @Sql("/insert.user.for.test")
    @Sql("/insert.idea.for.test")
    void test_vote_WhenSomeoneTryToVoteOneHisOwnIdeaExpectInvalidVoteExceptionThrown() {

        VoteCommand voteCommand = new VoteCommand();
        voteCommand.setIdeaToVoteOn("Idea");
        Long voterId = 1L;
        Exception exp = assertThrows(InvalidVoteException.class, () ->
                ideaService.vote(voteCommand, voterId));
        assertEquals("You cannot vote on your idea.", exp.getMessage());
    }

    @Test
    @Sql("/insert.user.for.test")
    @Sql("/insert.idea.for.test")
    void test_vote_WhenSomeoneTryToVoteWhenAlreadyVotedExpectInvalidVoteExceptionThrown() {

        VoteCommand voteCommand = new VoteCommand();
        voteCommand.setIdeaToVoteOn("Idea");
        Long voterId = 2L;

        VoteResponse response = ideaService.vote(voteCommand, voterId);
        assertNotNull(response);
        assertEquals("Idea", response.getIdeaOnVoted());
        assertEquals("Vote successful", response.getMessage());
        ZonedDateTime timeFromResponse = ZonedDateTime.parse(response.getVoteTimeStamp());
        long diff = Duration.between(ZonedDateTime.now(), timeFromResponse).toMillis();
        assertTrue(diff < 300);

        Exception exp = assertThrows(InvalidVoteException.class, () ->
                ideaService.vote(voteCommand, voterId));
        assertEquals("You are already voted tomorrow you can try again.", exp.getMessage());
    }

    @Test
    @Sql("/insert.user.for.test")
    @Sql("/insert.idea.for.test")
    void test_vote_WhenAdminTryToVote() {
        VoteCommand voteCommand = new VoteCommand();
        voteCommand.setIdeaToVoteOn("Idea");
        Long voterId = 4L;
        Exception exp = assertThrows(InvalidVoteException.class, () ->
                ideaService.vote(voteCommand, voterId));
        assertEquals("You are admin you cannot vote", exp.getMessage());
    }

}