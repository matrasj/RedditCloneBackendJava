package com.example.redditclonebackend.mapper;

import com.example.redditclonebackend.entity.Subreddit;
import com.example.redditclonebackend.payload.subreddit.SubredditPayloadResponse;

public class SubredditPayloadResponseMapper {
    public static SubredditPayloadResponse mapToSubredditPayloadResponse(Subreddit subreddit) {
        return SubredditPayloadResponse.builder()
                .id(subreddit.getId())
                .title(subreddit.getTitle())
                .description(subreddit.getDescription())
                .createdAt(subreddit.getCreatedAt())
                .lastUpdated(subreddit.getLastUpdated())
                .authorUsername(subreddit.getAuthor().getUsername())
                .build();

    }
}
