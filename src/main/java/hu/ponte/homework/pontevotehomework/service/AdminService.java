package hu.ponte.homework.pontevotehomework.service;

import hu.ponte.homework.pontevotehomework.domain.Idea;
import hu.ponte.homework.pontevotehomework.domain.IdeaStatus;
import hu.ponte.homework.pontevotehomework.dto.AdminToolMapper;
import hu.ponte.homework.pontevotehomework.dto.outgoing.IdeaAuthorizeResponse;
import hu.ponte.homework.pontevotehomework.dto.outgoing.VoteStateDetailsListItem;
import hu.ponte.homework.pontevotehomework.dto.outgoing.WaitingStatusIdeaListItem;
import hu.ponte.homework.pontevotehomework.exception.IdeaNotExistsException;
import hu.ponte.homework.pontevotehomework.repository.IdeaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AdminService {

    private final IdeaRepository ideaRepository;
    private final AdminToolMapper adminToolMapper;


    public AdminService(IdeaRepository ideaRepository,
                        AdminToolMapper adminToolMapper) {

        this.ideaRepository = ideaRepository;
        this.adminToolMapper = adminToolMapper;

    }

    public IdeaAuthorizeResponse accepting(Long ideaId) {
        Idea idea = ideaRepository.findById(ideaId)
                .orElseThrow(() -> new IdeaNotExistsException(ideaId));
        idea.setStatus(IdeaStatus.ACCEPTED);
        return adminToolMapper.makeIdeaAuthorizeResponse(idea, "Idea accepted");
    }

    public IdeaAuthorizeResponse decline(Long id) {
        Idea idea = ideaRepository.findById(id)
                .orElseThrow(() -> new IdeaNotExistsException(id));
        idea.setStatus(IdeaStatus.DECLINED);
        return adminToolMapper.makeIdeaAuthorizeResponse(idea, "Idea declined");
    }

    public WaitingStatusIdeaListItem getAllPending() {
        List<Idea> ideas = ideaRepository.findWaitingForRespond();
        return adminToolMapper.makeWaitingStatusIdeaListItem(ideas);
    }


    public VoteStateDetailsListItem getAllLive(boolean isAscending) {
        if (isAscending) {
            List<Idea> ideas = ideaRepository.findCurrentLiveWithVotesASC();
            return adminToolMapper.makeVoteDetailsListItem(ideas);
        }
        List<Idea> ideas = ideaRepository.findCurrentLiveWithVotesDESC();
        return adminToolMapper.makeVoteDetailsListItem(ideas);
    }

}
