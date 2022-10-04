package com.example.redditclonebackend.mapper;

import com.example.redditclonebackend.entity.Post;
import com.example.redditclonebackend.entity.User;
import com.example.redditclonebackend.entity.Vote;
import com.example.redditclonebackend.payload.post.PostPayloadResponse;
import com.example.redditclonebackend.repository.VoteRepository;
import com.example.redditclonebackend.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostPayloadResponseMapper {
    private final AuthService authService;
    private final VoteRepository voteRepository;
    public  PostPayloadResponse mapToPostPayloadResponse(Post post) {
        return PostPayloadResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .voteCount(post.getVoteCount())
                .createdAt(post.getCreatedAt())
                .lastUpdated(post.getLastUpdated())
                .subredditName(post.getSubreddit().getTitle())
                .authorUsername(post.getUser().getUsername())
                .commentsNumber(post.getComments().size())
                .likedStatus(getLikedStatus(post))
                .postImageLink(post.getImageLink())
                .profileImageLink(post.getUser().getProfileImageLink())
                .build();
    }

    private Integer getLikedStatus( Post post) {


//        Vote v = voteRepository.findAll()
//                .stream()
//                .filter((vote -> vote.getPost().getId().equals(post.getId())
//                        && vote.getAuthor().getId().equals(currentUser.getId())))
//                .findFirst()
//                .orElseGet(() -> null);

        if(authService.isLoggedIn()){
            User currentUser = authService.getCurrentUser();
            Optional<Vote> byPostAndAuthor = voteRepository.findByPostAndAuthor(post, currentUser);

            if (byPostAndAuthor.isPresent()) {
                switch (byPostAndAuthor.get().getVoteType()) {
                    case VOTE_DOWN -> {
                        return Integer.valueOf(-1);
                    }
                    case VOTE_UP -> {
                        return Integer.valueOf(1);
                    }
                }
            }
        }
        return 0;
    }
}
