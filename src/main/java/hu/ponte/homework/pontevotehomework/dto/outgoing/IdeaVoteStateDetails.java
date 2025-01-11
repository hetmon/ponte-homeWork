package hu.ponte.homework.pontevotehomework.dto.outgoing;

import java.util.ArrayList;
import java.util.List;

public class IdeaVoteStateDetails {


    private String ideaText;

    private String expireAt;

    private Long totalVotes;

    private List<VoteDetails> detailsList = new ArrayList<>();


    public String getIdeaText() {
        return ideaText;
    }

    public void setIdeaText(String ideaText) {
        this.ideaText = ideaText;
    }

    public String getExpireAt() {
        return expireAt;
    }

    public void setExpireAt(String expireAt) {
        this.expireAt = expireAt;
    }

    public Long getTotalVotes() {
        return totalVotes;
    }

    public void setTotalVotes(Long totalVotes) {
        this.totalVotes = totalVotes;
    }

    public List<VoteDetails> getDetailsList() {
        return detailsList;
    }

    public void setDetailsList(List<VoteDetails> detailsList) {
        this.detailsList = detailsList;
    }
}
