package hu.ponte.homework.pontevotehomework.service;

import hu.ponte.homework.pontevotehomework.domain.Vote;
import hu.ponte.homework.pontevotehomework.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class VoteService {

    private final VoteRepository voteRepository;

    @Autowired
    public VoteService(VoteRepository voteRepository) {
        this.voteRepository = voteRepository;
    }

    public Vote saveVote(Vote vote) {
        return voteRepository.save(vote);
    }
}
