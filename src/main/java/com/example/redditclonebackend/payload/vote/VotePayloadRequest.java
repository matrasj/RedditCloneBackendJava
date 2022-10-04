package com.example.redditclonebackend.payload.vote;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class VotePayloadRequest {
    private Integer voteValue;
    private Long postId;
}
