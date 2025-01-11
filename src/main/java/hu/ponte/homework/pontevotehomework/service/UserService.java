package hu.ponte.homework.pontevotehomework.service;

import hu.ponte.homework.pontevotehomework.domain.User;
import hu.ponte.homework.pontevotehomework.exception.UserNotFoundException;
import hu.ponte.homework.pontevotehomework.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void executeRefreshingUsableVotes() {
        log.info("Refreshing usable votes at 00:00");
        List<User> users = userRepository.getAllAlreadyVoted();
        for (User currentUser : users) {
            currentUser.setUsableVotes(1);
        }
    }

    public User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }
}
