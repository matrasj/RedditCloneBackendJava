package com.example.redditclonebackend.service;

import com.example.redditclonebackend.entity.Post;
import com.example.redditclonebackend.entity.Subreddit;
import com.example.redditclonebackend.entity.User;
import com.example.redditclonebackend.exception.PostNotFoundException;
import com.example.redditclonebackend.exception.SubredditNotFoundException;
import com.example.redditclonebackend.mapper.PostPayloadResponseMapper;
import com.example.redditclonebackend.payload.post.PostPayloadRequest;
import com.example.redditclonebackend.payload.post.PostPayloadResponse;
import com.example.redditclonebackend.repository.PostRepository;
import com.example.redditclonebackend.repository.SubredditRepository;
import com.example.redditclonebackend.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {
    private static final String SUBREDDIT_NOT_FOUND_MESSAGE = "Not found subreddit with id %d";
    private static final String POST_CREATION_SUCCESS_MESSAGE = "Successfully created post";
    private static final String POST_NOT_FOUND_MESSAGE = "Not found post with id %d";
    private static final String SUCCESSFULLY_DELETED_POST_MESSAGE = "Successfully deleted post";
    private final PostRepository postRepository;
    private final SubredditRepository subredditRepository;
    private final AuthService authService;
    private final PostPayloadResponseMapper postPayloadResponseMapper;
    private final StorageService storageService;

    @Transactional
    public String createPostAndAssignToSubreddit(PostPayloadRequest postPayloadRequest,
                                                 Long subredditId) throws IOException {
        User currentUser = authService.getCurrentUser();

        Subreddit subreddit = subredditRepository.findById(subredditId)
                .orElseThrow(() -> new SubredditNotFoundException(String.format(SUBREDDIT_NOT_FOUND_MESSAGE, subredditId)));

        Post post = Post.builder()
                .title(postPayloadRequest.getTitle())
                .content(postPayloadRequest.getContent())
                .voteCount(0)
                .user(currentUser)
                .subreddit(subreddit)
                .build();
        if (postPayloadRequest.getFile() != null) {
            String postImageLink = storageService.saveImage(postPayloadRequest.getFile(), "posts");
            post.setImageLink(postImageLink);
        }

        postRepository.save(post);

        return POST_CREATION_SUCCESS_MESSAGE;
    }

    public Page<PostPayloadResponse> findPostsPage(int pageNumber, int pageSize) {
        Page<Post> postPage = postRepository.findAll(PageRequest.of(pageNumber, pageSize));

        return new PageImpl<>(
                postPage.stream()
                        .map(postPayloadResponseMapper::mapToPostPayloadResponse)
                        .collect(Collectors.toList()),
                PageRequest.of(pageNumber, pageSize),
                postPage.getTotalElements()
        );
    }

    public Page<PostPayloadResponse> findPostsPageBySubredditTitle(String subredditTitle,
                                                                       int pageNumber,
                                                                       int pageSize) {
        Page<Post> bySubredditTitle =
                postRepository.findBySubredditTitle(subredditTitle, PageRequest.of(pageNumber, pageSize));

        return new PageImpl<>(
                bySubredditTitle.stream()
                        .map(postPayloadResponseMapper::mapToPostPayloadResponse)
                        .collect(Collectors.toList()),
                PageRequest.of(pageNumber, pageSize),
                bySubredditTitle.getTotalElements()
        );
    }

    public PostPayloadResponse findPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(String.format(POST_NOT_FOUND_MESSAGE, id)));
        return postPayloadResponseMapper.mapToPostPayloadResponse(post);
    }

    public Page<PostPayloadResponse> findPostsByAuthorUsername(String username, int pageNumber, int pageSize) {
        Page<Post> postsByUsername
                = postRepository.findByUserUsername(username, PageRequest.of(pageNumber, pageSize));

        return new PageImpl<>(
                postsByUsername.stream()
                        .map(postPayloadResponseMapper::mapToPostPayloadResponse)
                        .collect(Collectors.toList()),
                PageRequest.of(pageNumber, pageSize),
                postsByUsername.getTotalElements()
        );
    }

    public Page<PostPayloadResponse> findPostsByAuthorUsernameAndKey(String username,
                                                                     String keyword,
                                                                     int pageNumber,
                                                                     int pageSize) {
        Page<Post> postsByKeywordAndUsername =
                postRepository.findByUserUsernameAndTitleContaining(username,
                        keyword,
                        PageRequest.of(pageNumber, pageSize));

        return new PageImpl<>(
                postsByKeywordAndUsername
                        .stream()
                        .map(postPayloadResponseMapper::mapToPostPayloadResponse)
                        .collect(Collectors.toList()),
                PageRequest.of(pageNumber, pageSize),
                postsByKeywordAndUsername.getTotalElements()
        );
    }

    public String deletePostById(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(String.format(POST_NOT_FOUND_MESSAGE, postId)));
        postRepository.delete(post);



        return SUCCESSFULLY_DELETED_POST_MESSAGE;
    }
}
