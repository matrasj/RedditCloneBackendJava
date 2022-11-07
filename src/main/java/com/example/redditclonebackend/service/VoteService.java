package com.example.redditclonebackend.service;

import com.example.redditclonebackend.entity.Post;
import com.example.redditclonebackend.entity.User;
import com.example.redditclonebackend.entity.Vote;
import com.example.redditclonebackend.exception.PostNotFoundException;
import com.example.redditclonebackend.payload.vote.VotePayloadRequest;
import com.example.redditclonebackend.payload.vote.VotePayloadResponse;
import com.example.redditclonebackend.repository.PostRepository;
import com.example.redditclonebackend.repository.UserRepository;
import com.example.redditclonebackend.repository.VoteRepository;
import com.example.redditclonebackend.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.example.redditclonebackend.entity.VoteType.VOTE_DOWN;
import static com.example.redditclonebackend.entity.VoteType.VOTE_UP;

@Service
@RequiredArgsConstructor
@Slf4j
public class VoteService {
    private static final String NOT_FOUND_POST_MESSAGE = "Not found post with id %d";
    private static final String NOT_FOUND_USER_MESSAGE = "Not found user with id %s";
    private final VoteRepository voteRepository;
    private final PostRepository postRepository;

    private final UserRepository userRepository;

    @Transactional
    public VotePayloadResponse votePost(@NotNull VotePayloadRequest votePayloadRequest) {
        Post post = postRepository.findById(votePayloadRequest.getPostId())
                .orElseThrow(() -> new PostNotFoundException(String.format(NOT_FOUND_POST_MESSAGE, votePayloadRequest.getPostId())));

        User user = userRepository.findByUsername(votePayloadRequest.getAuthorUsername())
                .orElseThrow(() -> new UsernameNotFoundException(String.format(NOT_FOUND_USER_MESSAGE, votePayloadRequest.getAuthorUsername())));


        Optional<Vote> voteByPostAndAuthor = voteRepository.findByPostAndAuthor(post, user);
        System.out.println(voteByPostAndAuthor);

        if (voteByPostAndAuthor.isPresent() && votePayloadRequest.getVoteValue().equals(
                voteByPostAndAuthor.get().getVoteType().getDirection()
        )) {
            if (voteByPostAndAuthor.get().getVoteType().equals(VOTE_DOWN)) {
                post.setVoteCount(post.getVoteCount() + 1);
            } else {
                post.setVoteCount(post.getVoteCount() - 1);
            }
            log.info("First path");
            System.out.println(voteByPostAndAuthor.get().getId());
            try {
                voteRepository.deleteById(voteByPostAndAuthor.get().getId());
            } catch (NullPointerException nullPointerException) {
                System.out.println(nullPointerException.getMessage());
            }
            System.out.println("AFTER");

        }

        else if(voteByPostAndAuthor.isPresent() && !votePayloadRequest.getVoteValue().equals(
                voteByPostAndAuthor.get().getVoteType().getDirection())
        ){
            log.warn("SECOND PATH");

            voteRepository.delete(voteByPostAndAuthor.get());
            System.out.println(voteRepository.findAll().size());
            Vote vote = Vote.builder()
                    .author(user)
                    .post(post)
                    .build();

            if (voteByPostAndAuthor.get().getVoteType().equals(VOTE_DOWN)) {
                post.setVoteCount(post.getVoteCount() + 1);
            } else {
                post.setVoteCount(post.getVoteCount() - 1);
            }

            giveVote(votePayloadRequest, post, vote, user);
        }

        else if (voteByPostAndAuthor.isEmpty()) {
            log.info("THIRD PATH");
            Vote vote = Vote.builder()
                            .author(user).post(post).build();
            giveVote(votePayloadRequest, post, vote, user);
        }

        return new VotePayloadResponse(post.getVoteCount());
    }

    private void giveVote(VotePayloadRequest votePayloadRequest, Post post, Vote vote, User user) {
        System.out.println("HERE");
        if (votePayloadRequest.getVoteValue() > 0) {
            vote.setVoteType(VOTE_UP);
            post.setVoteCount(post.getVoteCount() + 1);
        } else {
            post.setVoteCount(post.getVoteCount() - 1);
            vote.setVoteType(VOTE_DOWN);
        }



        user.getVotes().add(vote);
        post.getVotes().add(vote);

        voteRepository.save(vote);

    }
}
