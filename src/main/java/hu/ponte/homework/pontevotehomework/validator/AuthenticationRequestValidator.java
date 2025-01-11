package hu.ponte.homework.pontevotehomework.validator;

import hu.ponte.homework.pontevotehomework.domain.User;
import hu.ponte.homework.pontevotehomework.dto.income.AuthenticationRequest;
import hu.ponte.homework.pontevotehomework.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

@Component
public class AuthenticationRequestValidator implements Validator {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationRequestValidator(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return AuthenticationRequest.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        AuthenticationRequest request = (AuthenticationRequest) target;
        Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());
        if (optionalUser.isEmpty()) {
            errors.rejectValue("email", "email.not.exists");
        }
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                errors.rejectValue("password", "password.not.match");
            }
        }
    }
}
