package com.example.redditclonebackend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static javax.persistence.CascadeType.*;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@RequiredArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Table(name = "subreddit")
public class Subreddit {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "created_at")
    @CreationTimestamp
    private Date createdAt;

    @Column(name = "last_updated")
    private Date lastUpdated;

    @OneToMany(mappedBy = "subreddit", fetch = LAZY)
    private List<Post> posts = new ArrayList<>();

    @ManyToOne(cascade = {
            DETACH,
            MERGE,
            REFRESH,
            PERSIST
    })
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    private User author;
}
