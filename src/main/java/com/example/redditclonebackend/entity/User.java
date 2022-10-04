package com.example.redditclonebackend.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.*;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "biography", columnDefinition = "TEXT")
    private String biography;

    @OneToMany(mappedBy = "user")
    private List<SocialMediaLink> links = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = ALL)
    private Set<Post> posts = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = ALL)
    private Set<ConfirmationToken> tokens = new HashSet<>();

    @OneToMany(mappedBy = "author")
    private Set<Comment> comments = new HashSet<>();

    @OneToMany(mappedBy = "author")
    private Set<Vote> votes = new HashSet<>();

    @CreationTimestamp
    @Column(name = "created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "last_updated")
    private Date lastUpdated;

    @Column(name = "enabled")
    private boolean enabled;

    @OneToMany(mappedBy = "author", cascade = ALL)
    private List<Subreddit> subreddits = new ArrayList<>();

    @Column(name = "profile_image_link")
    private String profileImageLink;


}


