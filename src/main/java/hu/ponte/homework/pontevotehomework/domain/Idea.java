package hu.ponte.homework.pontevotehomework.domain;

import jakarta.persistence.*;

import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Table(name = "ideas")
public class Idea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "idea")
    private String idea;

    @Column(name = "total_votes")
    private Long totalVotes;

    @Column(name = "time_of_make")
    private ZonedDateTime timeOfMake;

    @Column(name = "expire_at")
    private ZonedDateTime expireAt;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private IdeaStatus status;

    @OneToMany(mappedBy = "idea")
    private List<Vote> votes;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public IdeaStatus getStatus() {
        return status;
    }

    public void setStatus(IdeaStatus status) {
        this.status = status;
    }

    public synchronized void incrementTotalVotes() {
        totalVotes++;
    }

    public Long getTotalVotes() {
        return totalVotes;
    }

    public void setTotalVotes(Long totalVotes) {
        this.totalVotes = totalVotes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdea() {
        return idea;
    }

    public void setIdea(String idea) {
        this.idea = idea;
    }

    public ZonedDateTime getTimeOfMake() {
        return timeOfMake;
    }

    public void setTimeOfMake(ZonedDateTime timeOfMake) {
        this.timeOfMake = timeOfMake;
    }

    public ZonedDateTime getExpireAt() {
        return expireAt;
    }

    public void setExpireAt(ZonedDateTime expireAt) {
        this.expireAt = expireAt;
    }

    public List<Vote> getVotes() {
        return votes;
    }

    public void setVotes(List<Vote> votes) {
        this.votes = votes;
    }
}
