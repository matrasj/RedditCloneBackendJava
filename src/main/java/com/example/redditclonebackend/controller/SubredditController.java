package com.example.redditclonebackend.controller;

import com.example.redditclonebackend.payload.subreddit.SubredditPayloadRequest;
import com.example.redditclonebackend.payload.subreddit.SubredditPayloadResponse;
import com.example.redditclonebackend.service.SubredditService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/v1/sub-reddits")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SubredditController {
    private final SubredditService subredditService;
    @PostMapping
    public ResponseEntity<SubredditPayloadResponse> createSubreddit(@RequestBody SubredditPayloadRequest
                                                                               subredditPayloadRequest) {
        return ResponseEntity.status(CREATED)
                .body(subredditService.createSubreddit(subredditPayloadRequest));
    }

    @GetMapping
    public ResponseEntity<Page<SubredditPayloadResponse>> getSubreddits(@RequestParam int pageSize,
                                                                        @RequestParam int pageNumber) {
        return ResponseEntity.status(OK)
                .body(subredditService.findSubredditsPage(pageNumber, pageSize));
    }

    @GetMapping("/findByTitleOrDescriptionContainingKeyword")
    public ResponseEntity<Page<SubredditPayloadResponse>> getSubredditsByKeyword(@RequestParam String keyword,
                                                                                 @RequestParam int pageSize,
                                                                                 @RequestParam int pageNumber
                                                                                 ) {
        return ResponseEntity.status(OK)
                .body(subredditService.findSubredditsPageByKeyword(keyword, pageNumber, pageSize));
    }

    @GetMapping("/all")
    public ResponseEntity<List<SubredditPayloadResponse>> getAllSubreddits() {
        return ResponseEntity.status(OK)
                .body(subredditService.findAllSubreddits());
    }

    @GetMapping("/limit")
    public ResponseEntity<List<SubredditPayloadResponse>> getWithLimit(@RequestParam Long limit) {
        return ResponseEntity.status(OK)
                .body(subredditService.findSubredditsByLimit(limit));
    }
}
