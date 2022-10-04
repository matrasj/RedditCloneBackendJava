package com.example.redditclonebackend.payload.subreddit;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
public class SubredditPayloadResponse {
    private Long id;
    private String title;
    private String description;
    private String authorUsername;
    private Date createdAt;
    private Date lastUpdated;
}
