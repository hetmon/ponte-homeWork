package hu.ponte.homework.pontevotehomework.dto.income;

import jakarta.validation.constraints.NotBlank;

public class IdeaCommand {

    @NotBlank
    private String idea;

    public @NotBlank String getIdea() {
        return idea;
    }

    public void setIdea(@NotBlank String idea) {
        this.idea = idea;
    }
}
