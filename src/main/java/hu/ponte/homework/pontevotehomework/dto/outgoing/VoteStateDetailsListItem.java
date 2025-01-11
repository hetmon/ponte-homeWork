package hu.ponte.homework.pontevotehomework.dto.outgoing;

import java.util.ArrayList;
import java.util.List;

public class VoteStateDetailsListItem {

    private String info;
    private List<IdeaVoteStateDetails> details = new ArrayList<>();


    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public List<IdeaVoteStateDetails> getDetails() {
        return details;
    }

    public void setDetails(List<IdeaVoteStateDetails> details) {
        this.details = details;
    }
}
