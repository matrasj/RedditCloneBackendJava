package com.example.redditclonebackend.service;

import com.example.redditclonebackend.entity.Subreddit;
import com.example.redditclonebackend.entity.User;
import com.example.redditclonebackend.mapper.SubredditPayloadResponseMapper;
import com.example.redditclonebackend.payload.subreddit.SubredditPayloadRequest;
import com.example.redditclonebackend.payload.subreddit.SubredditPayloadResponse;
import com.example.redditclonebackend.repository.SubredditRepository;
import com.example.redditclonebackend.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubredditService {
    private final SubredditRepository subredditRepository;
    private final AuthService authService;

    @Transactional
    public SubredditPayloadResponse createSubreddit(SubredditPayloadRequest subredditPayloadRequest) {
        User user = authService.getCurrentUser();
        Subreddit subreddit = Subreddit.builder()
                .title(subredditPayloadRequest.getTitle())
                .description(subredditPayloadRequest.getDescription())
                .author(user)
                .build();

        user.getSubreddits().add(subreddit);

        subredditRepository.save(subreddit);

        return SubredditPayloadResponse.builder().id(subreddit.getId())
                .title(subreddit.getTitle())
                .description(subreddit.getDescription())
                .authorUsername(authService.getCurrentUser().getUsername())
                .createdAt(subreddit.getCreatedAt())
                .lastUpdated(subreddit.getLastUpdated())
                .build();
    }


    public Page<SubredditPayloadResponse> findSubredditsPage(int pageNumber, int pageSize) {
        Page<Subreddit> subredditPage = subredditRepository.findAll(PageRequest.of(pageNumber, pageSize));

        return new PageImpl<>(
                subredditPage.stream()
                        .map(SubredditPayloadResponseMapper::mapToSubredditPayloadResponse)
                        .collect(Collectors.toList()),
                PageRequest.of(pageNumber, pageSize),
                subredditPage.getTotalElements()
        );
    }

    public List<SubredditPayloadResponse> findSubredditsByLimit(Long limit) {
        List<Subreddit> subredditsWithLimit = subredditRepository.findWithQuantity(limit);

        return subredditsWithLimit
                .stream()
                .map(SubredditPayloadResponseMapper::mapToSubredditPayloadResponse)
                .collect(Collectors.toList());
    }

    public List<SubredditPayloadResponse> findAllSubreddits() {
        return subredditRepository.findAll()
                .stream()
                .map(SubredditPayloadResponseMapper::mapToSubredditPayloadResponse)
                .collect(Collectors.toList());
    }

    public Page<SubredditPayloadResponse> findSubredditsPageByKeyword(String keyword, int pageNumber, int pageSize) {
        Page<Subreddit> subredditsByTitleOrDescriptionContaining
                = subredditRepository.findByTitleContainingOrDescriptionContaining(keyword, keyword, PageRequest.of(pageNumber, pageSize));

        return new PageImpl<>(
                subredditsByTitleOrDescriptionContaining
                        .stream()
                        .map(SubredditPayloadResponseMapper::mapToSubredditPayloadResponse)
                        .collect(Collectors.toList()),
                PageRequest.of(pageNumber, pageSize),
                subredditsByTitleOrDescriptionContaining.getTotalElements()
        );
    }
}
