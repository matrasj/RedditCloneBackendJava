package com.example.redditclonebackend.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

import static javax.persistence.CascadeType.*;
import static javax.persistence.FetchType.EAGER;
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

    @CreationTimestamp
    @Column(name = "created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "last_updated")
    private Date lastUpdated;

    @Column(name = "enabled")
    private boolean enabled;

    @Column(name = "profile_image_link")
    private String profileImageLink;

    @OneToMany(mappedBy = "author", cascade = ALL)
    private List<Subreddit> subreddits = new ArrayList<>();

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

    @ManyToMany(cascade = {
            MERGE, PERSIST
    }, fetch = EAGER)
    @JoinTable(name = "user_role",
        joinColumns = {
            @JoinColumn(name = "user_id", referencedColumnName = "id")
        },
        inverseJoinColumns = {
            @JoinColumn(name = "role_id", referencedColumnName = "id")
        }
    )
    private Set<Role> roles = new HashSet<>();

    public Set<Authority> getAuthorities() {
        return roles
                .stream()
                .map(Role::getAuthorities)
                .flatMap(Set::stream)
                .collect(Collectors.toSet());
    }




}


