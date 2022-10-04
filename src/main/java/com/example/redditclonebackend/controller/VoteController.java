package com.example.redditclonebackend.controller;

import com.example.redditclonebackend.payload.vote.VotePayloadRequest;
import com.example.redditclonebackend.payload.vote.VotePayloadResponse;
import com.example.redditclonebackend.service.VoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/votes")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class VoteController {
    private final VoteService voteService;
    @PostMapping
    public ResponseEntity<VotePayloadResponse> votePost(@RequestBody VotePayloadRequest votePayloadRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(voteService.votePost(votePayloadRequest));
    }
}
