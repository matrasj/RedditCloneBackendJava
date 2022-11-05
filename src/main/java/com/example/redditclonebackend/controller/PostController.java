package com.example.redditclonebackend.controller;

import com.example.redditclonebackend.payload.post.PostPayloadRequest;
import com.example.redditclonebackend.payload.post.PostPayloadResponse;
import com.example.redditclonebackend.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PostController {
    private final PostService postService;

    @PostMapping
    public ResponseEntity<String> createPost(@ModelAttribute @Valid PostPayloadRequest postPayloadRequest,
                                             @RequestParam Long subredditId) throws IOException {
        return ResponseEntity.status(CREATED)
                .body(postService.createPostAndAssignToSubreddit(postPayloadRequest, subredditId));
    }

    @GetMapping
    public ResponseEntity<Page<PostPayloadResponse>> getPostsPage(@RequestParam int pageNumber,
                                                                  @RequestParam int pageSize) {
        return ResponseEntity.status(OK)
                .body(postService.findPostsPage(pageNumber, pageSize));
    }

    @GetMapping("/findBySubredditTitle")
    public ResponseEntity<Page<PostPayloadResponse>> getPostsBySubredditTitleWithPagination(@RequestParam String subredditTitle,
                                                                                           @RequestParam int pageNumber,
                                                                                           @RequestParam int pageSize) {
        return ResponseEntity.status(OK)
                .body(postService.findPostsPageBySubredditTitle(subredditTitle, pageNumber, pageSize));
    }

    @GetMapping("/findByAuthorUsername")
    public ResponseEntity<Page<PostPayloadResponse>> getPostsByAuthorUsername(@RequestParam String username,
                                                                        @RequestParam int pageNumber,
                                                                        @RequestParam int pageSize) {
        return ResponseEntity.status(OK)
                .body(postService.findPostsByAuthorUsername(username, pageNumber, pageSize));
    }

    @GetMapping("/findByAuthorUsernameAndKeyword")
    public ResponseEntity<Page<PostPayloadResponse>> getPostsByAuthorUsernameAndKeyword(@RequestParam String username,
                                                                                        @RequestParam String keyword,
                                                                                        @RequestParam int pageNumber,
                                                                                        @RequestParam int pageSize) {
        return ResponseEntity.status(OK)
                .body(postService.findPostsByAuthorUsernameAndKey(username, keyword, pageNumber, pageSize));
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<PostPayloadResponse> getPost(@PathVariable Long id) {
        return ResponseEntity.status(OK)
                .body(postService.findPostById(id));
    }

    @GetMapping("/delete/{postId}")
    public ResponseEntity<String> deletePost(@PathVariable Long postId) {
        return ResponseEntity.status(ACCEPTED)
                .body(postService.deletePostById(postId));
    }




}
