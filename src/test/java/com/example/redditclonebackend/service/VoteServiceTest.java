package com.example.redditclonebackend.service;

import com.example.redditclonebackend.controller.AuthController;
import com.example.redditclonebackend.entity.Post;
import com.example.redditclonebackend.entity.User;
import com.example.redditclonebackend.entity.Vote;
import com.example.redditclonebackend.entity.VoteType;
import com.example.redditclonebackend.jwt.JwtTokenProvider;
import com.example.redditclonebackend.payload.vote.VotePayloadRequest;
import com.example.redditclonebackend.payload.vote.VotePayloadResponse;
import com.example.redditclonebackend.repository.PostRepository;
import com.example.redditclonebackend.repository.UserRepository;
import com.example.redditclonebackend.service.auth.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.reset;

@AutoConfigureTestDatabase
@WebMvcTest(VoteService.class)
class VoteServiceTest {

    @MockBean
    AuthService authService;

    @MockBean
    UserDetailsService userService;

    @MockBean
    AuthenticationManager authenticationManager;

    @MockBean
    JwtTokenProvider jwtTokenProvider;

    @MockBean
    UserRepository userRepository;

    @MockBean
    PostRepository postRepository;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    VoteService voteService;

    @Autowired
    ObjectMapper objectMapper;

    @AfterEach
    void tearDown() {
        reset(voteService);
    }
    @Test
    void votePostWithoutVotesPositive() {
        User user = buildUser();
        Post post = getPost(user);
        user.getPosts().add(post);

        userRepository.save(user);
        postRepository.save(post);
        Vote vote = Vote.builder().voteType(VoteType.VOTE_UP).author(user).post(post).build();
        VotePayloadRequest build = VotePayloadRequest.builder().postId(post.getId()).authorUsername(user.getUsername())
                .voteValue(vote.getVoteType().getDirection()).build();
        System.out.println(build);
        VotePayloadResponse votePayloadResponse = voteService.votePost(build);

        assertEquals(1, votePayloadResponse.getScore());

    }

    private Post getPost(User user) {
        return Post.builder()
                .id(1L)
                .voteCount(0)
                .title("Test post")
                .user(user)
                .build();
    }

    private User buildUser() {
        return User.builder()
                .id(1L)
                .username("admin")
                .password("admin")
                .email("admin.admin@gmail.com")
                .enabled(true)
                .posts(new HashSet<>())
                .build();
    }
}