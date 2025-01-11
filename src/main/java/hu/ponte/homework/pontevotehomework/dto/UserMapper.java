package hu.ponte.homework.pontevotehomework.dto;

import hu.ponte.homework.pontevotehomework.domain.Roles;
import hu.ponte.homework.pontevotehomework.domain.User;
import hu.ponte.homework.pontevotehomework.dto.income.RegisterRequest;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {


    public User makeUser(RegisterRequest request, String password) {
        User user = new User();
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPassword(password);
        user.setRole(Roles.USER);
        user.setUsableVotes(1);
        return user;
    }
}
