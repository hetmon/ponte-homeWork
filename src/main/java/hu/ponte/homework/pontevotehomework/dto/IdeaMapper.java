package hu.ponte.homework.pontevotehomework.dto;

import hu.ponte.homework.pontevotehomework.domain.Idea;
import hu.ponte.homework.pontevotehomework.domain.IdeaStatus;
import hu.ponte.homework.pontevotehomework.domain.User;
import hu.ponte.homework.pontevotehomework.dto.income.IdeaCommand;
import hu.ponte.homework.pontevotehomework.dto.outgoing.AllAcceptedIdeaListItem;
import hu.ponte.homework.pontevotehomework.dto.outgoing.IdeaResponse;
import hu.ponte.homework.pontevotehomework.dto.outgoing.MakeVoteDetails;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class IdeaMapper {


    public Idea makeIdea(IdeaCommand command, User user) {
        Idea idea = new Idea();
        idea.setStatus(IdeaStatus.WAITING_FOR_RESPOND);
        idea.setExpireAt(ZonedDateTime.now().plusDays(7));
        idea.setTimeOfMake(ZonedDateTime.now());
        idea.setTotalVotes(1L);
        idea.setVotes(new ArrayList<>());
        idea.setIdea(command.getIdea());
        idea.setUser(user);
        return idea;
    }

    public IdeaResponse makeIdeaResponse(Idea idea) {
        IdeaResponse response = new IdeaResponse();
        response.setMessage("Success, admins gonna accept or decline depends on your idea");
        response.setIdeaText(idea.getIdea());
        return response;
    }

    private MakeVoteDetails createMakeVoteDetails(Idea idea) {
        MakeVoteDetails makeVoteDetails = new MakeVoteDetails();
        makeVoteDetails.setIdea(idea.getIdea());
        makeVoteDetails.setExpireDate(idea.getExpireAt().toString());
        return makeVoteDetails;
    }

    public AllAcceptedIdeaListItem makeAllAcceptedIdeaListItem(List<Idea> ideas) {
        List<MakeVoteDetails> details = ideas.stream()
                .map(this::createMakeVoteDetails)
                .toList();
        AllAcceptedIdeaListItem listItem = new AllAcceptedIdeaListItem();
        listItem.setDetailsList(details);
        listItem.setInfo("All accepted ideas and expire date");
        return listItem;
    }
}
