package hu.ponte.homework.pontevotehomework.service;


import hu.ponte.homework.pontevotehomework.domain.Idea;
import hu.ponte.homework.pontevotehomework.domain.Roles;
import hu.ponte.homework.pontevotehomework.domain.User;
import hu.ponte.homework.pontevotehomework.domain.Vote;
import hu.ponte.homework.pontevotehomework.dto.IdeaMapper;
import hu.ponte.homework.pontevotehomework.dto.VoteMapper;
import hu.ponte.homework.pontevotehomework.dto.income.IdeaCommand;
import hu.ponte.homework.pontevotehomework.dto.income.VoteCommand;
import hu.ponte.homework.pontevotehomework.dto.outgoing.AllAcceptedIdeaListItem;
import hu.ponte.homework.pontevotehomework.dto.outgoing.IdeaResponse;
import hu.ponte.homework.pontevotehomework.dto.outgoing.VoteResponse;
import hu.ponte.homework.pontevotehomework.exception.IdeaNotExistsException;
import hu.ponte.homework.pontevotehomework.exception.InvalidVoteException;
import hu.ponte.homework.pontevotehomework.extractor.ExtractorHelper;
import hu.ponte.homework.pontevotehomework.repository.IdeaRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class IdeaService {

    private final IdeaRepository ideaRepository;
    private final IdeaMapper ideaMapper;
    private final UserService userService;
    private final VoteMapper voteMapper;
    private final VoteService voteService;

    @Autowired
    public IdeaService(IdeaRepository ideaRepository,
                       IdeaMapper ideaMapper,
                       UserService userService,
                       VoteMapper voteMapper,
                       VoteService voteService) {


        this.ideaRepository = ideaRepository;
        this.ideaMapper = ideaMapper;
        this.userService = userService;
        this.voteMapper = voteMapper;
        this.voteService = voteService;
    }

    public IdeaResponse makeIdea(@Valid IdeaCommand command, HttpServletRequest req) {
        Long id = ExtractorHelper.extractUserIdFromSession(req);
        User user = userService.getUser(id);
        Idea idea = ideaMapper.makeIdea(command, user);
        idea = ideaRepository.save(idea);
        return ideaMapper.makeIdeaResponse(idea);
    }


    public AllAcceptedIdeaListItem getAllAccepted() {
        List<Idea> ideas = ideaRepository.getAcceptedForUser();
        return ideaMapper.makeAllAcceptedIdeaListItem(ideas);
    }

    public VoteResponse vote(VoteCommand command, Long voterId) {
        Idea idea = unwrapIdea(command.getIdeaToVoteOn());
        idea.incrementTotalVotes();
        User user = userService.getUser(voterId);
        voteGuard(idea.getUser(), user);
        user.decreaseUsableVotes();
        Vote vote = voteMapper.makeVote(user, idea);
        vote = voteService.saveVote(vote);
        return voteMapper.makeVoteResponse(idea.getIdea(), vote.getVoteAt().toString());
    }


    private void voteGuard(User user, User toCompare) {
        if (toCompare.getRole() == Roles.ADMIN) {
            throw new InvalidVoteException("You are admin you cannot vote");
        }
        if (user.equals(toCompare)) {
            throw new InvalidVoteException("You cannot vote on your idea.");
        }
        if (toCompare.getUsableVotes() == 0) {
            throw new InvalidVoteException("You are already voted tomorrow you can try again.");
        }
    }

    protected Idea unwrapIdea(String ideaText) {
        return ideaRepository.findIdeaByIdeaAndAcceptStatus(ideaText)
                .orElseThrow(() -> new IdeaNotExistsException(ideaText));
    }
}
