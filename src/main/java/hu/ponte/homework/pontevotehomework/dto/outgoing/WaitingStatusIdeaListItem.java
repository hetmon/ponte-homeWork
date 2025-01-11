package hu.ponte.homework.pontevotehomework.dto.outgoing;

import java.util.ArrayList;
import java.util.List;

public class WaitingStatusIdeaListItem {

    private String info;
    private List<IdeasWithWaitingStatusDetails> ideas =new ArrayList<>();

    public List<IdeasWithWaitingStatusDetails> getIdeas() {
        return ideas;
    }

    public void setIdeas(List<IdeasWithWaitingStatusDetails> ideas) {
        this.ideas = ideas;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
