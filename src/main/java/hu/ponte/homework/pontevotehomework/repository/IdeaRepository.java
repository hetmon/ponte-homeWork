package hu.ponte.homework.pontevotehomework.repository;

import hu.ponte.homework.pontevotehomework.domain.Idea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IdeaRepository extends JpaRepository<Idea, Long> {


    @Query(nativeQuery = true, value = "select * from ideas i where i.status='WAITING_FOR_RESPOND'")
    List<Idea> findWaitingForRespond();

    @Query(nativeQuery = true, value = "select * from ideas where status='ACCEPTED' " +
            "order by total_votes")
    List<Idea> findCurrentLiveWithVotesASC();

    @Query(nativeQuery = true, value = "select * from ideas where status='ACCEPTED' " +
            "order by total_votes desc")
    List<Idea> findCurrentLiveWithVotesDESC();

    @Query(nativeQuery = true, value = "select * from ideas where idea=:idea")
    Optional<Idea> findIdeaByIdea(@Param("idea") String idea);

    @Query(nativeQuery = true, value = "select * from ideas i where i.status='ACCEPTED' and" +
            " i.expire_at > now() order by i.time_of_make")
    List<Idea> getAcceptedForUser();

    @Query(nativeQuery = true, value = "select * from ideas i where i.idea=:idea and i.status='ACCEPTED'")
    Optional<Idea> findIdeaByIdeaAndAcceptStatus(@Param("idea")String ideaText);

}
