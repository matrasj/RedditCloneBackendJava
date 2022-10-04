package com.example.redditclonebackend.payload.subreddit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class SubredditPayloadRequest {
    private String title;
    private String description;
}
