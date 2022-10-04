package com.example.redditclonebackend.repository;

import com.example.redditclonebackend.entity.Subreddit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubredditRepository extends JpaRepository<Subreddit, Long> {
    Optional<Subreddit> findByTitle(@Param("title") String title);

    @Query(value = "SELECT * FROM subreddit LIMIT :quantity", nativeQuery = true )
    List<Subreddit> findWithQuantity(@Param("quantity") Long quantity);

    Page<Subreddit> findByTitleContainingOrDescriptionContaining(@Param("keyword") String keyword,
                                                                 @Param("keyword") String secondKeyword,
                                                                 Pageable pageable);
}
