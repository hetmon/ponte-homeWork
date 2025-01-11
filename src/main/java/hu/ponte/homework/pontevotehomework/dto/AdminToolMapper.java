package hu.ponte.homework.pontevotehomework.dto;

import hu.ponte.homework.pontevotehomework.domain.Idea;
import hu.ponte.homework.pontevotehomework.domain.User;
import hu.ponte.homework.pontevotehomework.domain.Vote;
import hu.ponte.homework.pontevotehomework.dto.outgoing.*;
import hu.ponte.homework.pontevotehomework.exception.UserNotFoundException;
import hu.ponte.homework.pontevotehomework.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.List;

@Component
public class AdminToolMapper {


    private final UserRepository userRepository;

    @Autowired
    public AdminToolMapper(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public IdeaAuthorizeResponse makeIdeaAuthorizeResponse(Idea idea, String message) {
        IdeaAuthorizeResponse response = new IdeaAuthorizeResponse();
        response.setHttpStatus(HttpStatus.OK.toString());
        response.setMessage(message);
        response.setTimeStamp(ZonedDateTime.now().toString());
        response.setIdeaText(idea.getIdea());
        return response;
    }

    public IdeasWithWaitingStatusDetails makeIdeasWithWaitingStatusDetails(Idea idea) {
        IdeasWithWaitingStatusDetails response = new IdeasWithWaitingStatusDetails();
        response.setId(idea.getId());
        response.setStatus(idea.getStatus().toString());
        response.setIdeaText(idea.getIdea());
        response.setExpireAt(idea.getExpireAt().toString());
        response.setTimeOfMake(idea.getTimeOfMake().toString());
        response.setTotalVotes(idea.getTotalVotes());
        return response;
    }

    public WaitingStatusIdeaListItem makeWaitingStatusIdeaListItem(List<Idea> ideas) {
        WaitingStatusIdeaListItem item = new WaitingStatusIdeaListItem();
        List<IdeasWithWaitingStatusDetails> details = ideas.stream()
                .map(this::makeIdeasWithWaitingStatusDetails)
                .toList();
        item.setInfo("All idea what waiting for respond");
        item.setIdeas(details);
        return item;
    }


    private IdeaVoteStateDetails makeIdeaVoteStateDetails(Idea idea, List<Vote> votes) {
        IdeaVoteStateDetails response = new IdeaVoteStateDetails();
        response.setIdeaText(idea.getIdea());
        response.setExpireAt(idea.getExpireAt().toString());
        response.setTotalVotes(idea.getTotalVotes());
        response.setDetailsList(makeVoteDetailsList(votes));
        return response;
    }

    private List<VoteDetails> makeVoteDetailsList(List<Vote> votes) {
        return votes.stream()
                .map(this::makeVoteDetails)
                .toList();
    }

    private VoteDetails makeVoteDetails(Vote vote) {
        VoteDetails response = new VoteDetails();
        response.setVoter(unwrapAndBuildUserName(vote.getUser().getId()));
        response.setVoteTimeStamp(vote.getVoteAt().toString());
        return response;
    }

    public VoteStateDetailsListItem makeVoteDetailsListItem(List<Idea> ideas) {
        VoteStateDetailsListItem item = new VoteStateDetailsListItem();
        item.setInfo("All all pending idea with information");
        item.setDetails(makeIdeaVoteStateDetailsList(ideas));
        return item;
    }

    private List<IdeaVoteStateDetails> makeIdeaVoteStateDetailsList(List<Idea> ideas) {
        return ideas.stream()
                .map(idea -> makeIdeaVoteStateDetails(idea, idea.getVotes()))
                .toList();
    }

    private User unwrapUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    private String formattingVoterName(User user) {
        return user.getFirstName() + " " + user.getLastName();
    }

    private String unwrapAndBuildUserName(Long id) {
        User user = unwrapUser(id);
        return formattingVoterName(user);
    }

}
