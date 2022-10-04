package com.example.redditclonebackend.service;

import com.example.redditclonebackend.entity.Comment;
import com.example.redditclonebackend.entity.Post;
import com.example.redditclonebackend.entity.User;
import com.example.redditclonebackend.exception.PostNotFoundException;
import com.example.redditclonebackend.mapper.CommentPayloadResponseMapper;
import com.example.redditclonebackend.payload.comment.CommentPayloadRequest;
import com.example.redditclonebackend.payload.comment.CommentPayloadResponse;
import com.example.redditclonebackend.repository.CommentRepository;
import com.example.redditclonebackend.repository.PostRepository;
import com.example.redditclonebackend.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CommentService {
    private static final String COMMENT_SUCCESS_CREATION_MESSAGE = "Successfully added comment %s to post with id %d";
    private static final String POST_NOT_FOUND_MESSAGE = "Not found post with id --> %d";
    private static final String USER_NOT_FOUND_MESSAGE = "Not found user with username --> %s";
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final AuthService authService;
    private final StorageService storageService;

    public Page<CommentPayloadResponse> findCommentsPageByPostId(Long postId, int pageNumber, int pageSize) {
        Page<Comment> commentsByPostId =
                commentRepository.findByPostId(postId, PageRequest.of(pageNumber, pageSize));

        return new PageImpl<>(
                commentsByPostId.stream()
                        .map(CommentPayloadResponseMapper::mapToCommentPayloadResponse)
                        .collect(Collectors.toList()),
                PageRequest.of(pageNumber, pageSize),
                commentsByPostId.getTotalElements()
        );
    }

    @Transactional
    public String createCommentAndAssignToPost(Long postId, CommentPayloadRequest commentPayloadRequest) throws IOException {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(String.format(POST_NOT_FOUND_MESSAGE, postId)));


        User author = authService.getCurrentUser();

        Comment comment = Comment.builder()
                .post(post)
                .content(commentPayloadRequest.getContent())
                .author(author)
                .build();

        if (commentPayloadRequest.getFile() != null) {
            String commentImageLink = storageService.saveImage(commentPayloadRequest.getFile(), "comments");
            comment.setImageLink(commentImageLink);
        }

        commentRepository.save(comment);

        return String.format(COMMENT_SUCCESS_CREATION_MESSAGE, comment.getContent(), postId);
    }
}
