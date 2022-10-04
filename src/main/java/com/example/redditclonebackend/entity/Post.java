package com.example.redditclonebackend.entity;


import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.CascadeType.*;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "post")
public class Post {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "Post title can not be blank")
    @Column(name = "title")
    private String title;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "vote_count",
    columnDefinition = "int default 0", nullable = false)
    private Integer voteCount;

    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne(fetch = LAZY, cascade = {
            MERGE,
            DETACH,
            REFRESH,
            PERSIST
    })
    private User user;

    @CreationTimestamp
    @Column(name = "created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "last_updated")
    private Date lastUpdated;

    @OneToMany(mappedBy = "post", cascade = ALL)
    private Set<Comment> comments = new HashSet<>();

    @ManyToOne(cascade = {
            DETACH,
            MERGE,
            PERSIST,
            REFRESH
    })
    @JoinColumn(name = "subreddit_id", referencedColumnName = "id")
    private Subreddit subreddit;

    @OneToMany(mappedBy = "post", cascade = ALL)
    private Set<Vote> votes = new HashSet<>();

    @Column(name = "image_link")
    private String imageLink;




}
