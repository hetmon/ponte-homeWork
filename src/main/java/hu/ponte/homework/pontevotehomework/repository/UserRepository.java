package hu.ponte.homework.pontevotehomework.repository;


import hu.ponte.homework.pontevotehomework.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);


    @Query(nativeQuery = true, value = "select * from users where usable_votes = 0")
    List<User> getAllAlreadyVoted();
}
