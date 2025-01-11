package hu.ponte.homework.pontevotehomework.repository;

import hu.ponte.homework.pontevotehomework.domain.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
}
