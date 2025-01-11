package hu.ponte.homework.pontevotehomework.dto.income;

import jakarta.validation.constraints.NotBlank;

public class VoteCommand {


    @NotBlank
    private String ideaToVoteOn;

    public String getIdeaToVoteOn() {
        return ideaToVoteOn;
    }

    public void setIdeaToVoteOn(String ideaToVoteOn) {
        this.ideaToVoteOn = ideaToVoteOn;
    }
}
