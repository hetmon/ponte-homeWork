package hu.ponte.homework.pontevotehomework.dto.outgoing;

import java.util.ArrayList;
import java.util.List;

public class AllAcceptedIdeaListItem {


    private String info;


    private List<MakeVoteDetails> detailsList = new ArrayList<>();

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public List<MakeVoteDetails> getDetailsList() {
        return detailsList;
    }

    public void setDetailsList(List<MakeVoteDetails> detailsList) {
        this.detailsList = detailsList;
    }
}
