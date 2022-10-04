package com.example.redditclonebackend.controller;

import com.example.redditclonebackend.payload.comment.CommentPayloadRequest;
import com.example.redditclonebackend.payload.comment.CommentPayloadResponse;
import com.example.redditclonebackend.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class CommentController {
    private final CommentService commentService;

    @GetMapping("/post/id/{postId}")
    public ResponseEntity<Page<CommentPayloadResponse>> getCommentsPageByPostId(@PathVariable Long postId,
                                                                        @RequestParam int pageNumber,
                                                                        @RequestParam int pageSize) {
        return ResponseEntity.status(OK)
                .body(commentService.findCommentsPageByPostId(postId, pageNumber, pageSize));
    }

    @PostMapping("/post/id/{postId}")
    public ResponseEntity<String> addComment(@PathVariable Long postId,
                                          @ModelAttribute CommentPayloadRequest commentPayloadRequest) throws IOException {
        return ResponseEntity.status(CREATED)
                .body(commentService.createCommentAndAssignToPost(postId, commentPayloadRequest));
    }
}
