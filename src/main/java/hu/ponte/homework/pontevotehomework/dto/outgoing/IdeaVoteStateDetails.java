package hu.ponte.homework.pontevotehomework.dto.outgoing;

public class IdeaVoteStateDetails {

    private Long id;

    private String ideaText;

    private String expireAt;

    private String timeOfMake;

    private Long totalVotes;

    private String status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getTimeOfMake() {
        return timeOfMake;
    }

    public void setTimeOfMake(String timeOfMake) {
        this.timeOfMake = timeOfMake;
    }

    public Long getTotalVotes() {
        return totalVotes;
    }

    public void setTotalVotes(Long totalVotes) {
        this.totalVotes = totalVotes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
