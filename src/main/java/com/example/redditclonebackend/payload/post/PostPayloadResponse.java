package com.example.redditclonebackend.payload.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
public class PostPayloadResponse {
    private Long id;
    private String title;
    private String url;
    private String content;
    private Integer voteCount;
    private Date createdAt;
    private Date lastUpdated;
    private String subredditName;
    private String authorUsername;
    private Integer commentsNumber;
    private Integer likedStatus;
    private String profileImageLink;
    private String postImageLink;



}
