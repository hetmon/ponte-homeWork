package hu.ponte.homework.pontevotehomework.validator;

import hu.ponte.homework.pontevotehomework.dto.income.IdeaCommand;
import hu.ponte.homework.pontevotehomework.repository.IdeaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class MakeIdeaCommandValidator implements Validator {

    private final IdeaRepository ideaRepository;

    @Autowired
    public MakeIdeaCommandValidator(IdeaRepository ideaRepository) {
        this.ideaRepository = ideaRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return IdeaCommand.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        IdeaCommand command = (IdeaCommand) target;
        if (ideaRepository.findIdeaByIdea(command.getIdea()).isPresent()) {
            errors.rejectValue("idea", "idea.exists");
        }
    }
}
