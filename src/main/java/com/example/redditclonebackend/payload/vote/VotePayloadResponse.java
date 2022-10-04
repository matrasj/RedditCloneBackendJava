package com.example.redditclonebackend.payload.vote;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class VotePayloadResponse {
    private Integer score;
}
