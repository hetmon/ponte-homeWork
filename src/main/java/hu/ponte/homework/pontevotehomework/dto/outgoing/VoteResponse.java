package hu.ponte.homework.pontevotehomework.dto.outgoing;

public class VoteResponse {

    private String message;

    private String ideaOnVoted;

    private String voteTimeStamp;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getIdeaOnVoted() {
        return ideaOnVoted;
    }

    public void setIdeaOnVoted(String ideaOnVoted) {
        this.ideaOnVoted = ideaOnVoted;
    }

    public String getVoteTimeStamp() {
        return voteTimeStamp;
    }

    public void setVoteTimeStamp(String voteTimeStamp) {
        this.voteTimeStamp = voteTimeStamp;
    }
}
