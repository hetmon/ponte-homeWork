package hu.ponte.homework.pontevotehomework.dto;

import hu.ponte.homework.pontevotehomework.domain.Idea;
import hu.ponte.homework.pontevotehomework.domain.User;
import hu.ponte.homework.pontevotehomework.domain.Vote;
import hu.ponte.homework.pontevotehomework.dto.outgoing.VoteResponse;
import org.springframework.context.annotation.Configuration;

import java.time.ZonedDateTime;

@Configuration
public class VoteMapper {

    public Vote makeVote(User user, Idea idea) {
        Vote vote = new Vote();
        vote.setUser(user);
        vote.setIdea(idea);
        vote.setVoteAt(ZonedDateTime.now());
        return vote;
    }

    public VoteResponse makeVoteResponse(String idea, String timeStamp) {
        VoteResponse voteResponse = new VoteResponse();
        voteResponse.setVoteTimeStamp(timeStamp);
        voteResponse.setMessage("Vote successful");
        voteResponse.setIdeaOnVoted(idea);
        return voteResponse;
    }
}
