package com.example.redditclonebackend.entity;

import lombok.*;

import javax.persistence.*;

import static javax.persistence.CascadeType.*;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode
@Table(name = "vote")
public class Vote {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "vote_type")
    @Enumerated(value = STRING)
    private VoteType voteType;

    @ManyToOne(cascade = {
            MERGE,
            DETACH,
            PERSIST,
            REFRESH
    })
    @JoinColumn(name = "post_id", referencedColumnName = "id")
    private Post post;

    @Override
    public String toString() {
        return "Vote{" +
                "id=" + id +
                ", voteType=" + voteType +
                '}';
    }

    @ManyToOne(cascade = {
            MERGE,
            DETACH,
            PERSIST,
            REFRESH
    })
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    private User author;


}
