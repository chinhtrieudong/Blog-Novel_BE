package com.blognovel.blognovel.repository;

import com.blognovel.blognovel.model.NovelLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NovelLikeRepository extends JpaRepository<NovelLike, Long> {
    Optional<NovelLike> findByNovelIdAndUserId(Long novelId, Long userId);

    boolean existsByNovelIdAndUserId(Long novelId, Long userId);

    long countByNovelId(Long novelId);
}
