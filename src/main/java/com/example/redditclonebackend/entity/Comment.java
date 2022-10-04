package com.example.redditclonebackend.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

import static javax.persistence.CascadeType.*;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;


@Entity
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "content")
    private String content;

    @CreationTimestamp
    @Column(name = "created_at")
    private Date createdAt;

    @ManyToOne(cascade = {
            MERGE,
            PERSIST,
            REFRESH,
            DETACH
    }, fetch = LAZY)
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    private User author;

    @ManyToOne(fetch = LAZY, cascade = {
            MERGE,
            PERSIST,
            REFRESH,
            DETACH
    })
    @JoinColumn(name = "post_id", referencedColumnName = "id")
    private Post post;

    @Column(name = "image_link")
    private String imageLink;
}
