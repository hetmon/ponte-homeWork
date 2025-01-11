package hu.ponte.homework.pontevotehomework.domain;

import jakarta.persistence.*;

import java.time.ZonedDateTime;

@Entity
@Table(name = "vote")
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "idea_id")
    private Idea idea;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "vote_at")
    private ZonedDateTime voteAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Idea getIdea() {
        return idea;
    }

    public void setIdea(Idea idea) {
        this.idea = idea;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ZonedDateTime getVoteAt() {
        return voteAt;
    }

    public void setVoteAt(ZonedDateTime voteAt) {
        this.voteAt = voteAt;
    }
}
