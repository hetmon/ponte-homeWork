package hu.ponte.homework.pontevotehomework.validator;

import hu.ponte.homework.pontevotehomework.dto.income.RegisterRequest;
import hu.ponte.homework.pontevotehomework.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class RegisterRequestValidator implements Validator {

    private final UserRepository userRepository;

    @Autowired
    public RegisterRequestValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return RegisterRequest.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        RegisterRequest registerRequest = (RegisterRequest) target;
        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            errors.rejectValue("email", "register.email.already.exists");
        }
    }
}
