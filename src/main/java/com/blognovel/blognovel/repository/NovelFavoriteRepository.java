package com.blognovel.blognovel.repository;

import com.blognovel.blognovel.model.NovelFavorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NovelFavoriteRepository extends JpaRepository<NovelFavorite, Long> {
    Optional<NovelFavorite> findByNovelIdAndUserId(Long novelId, Long userId);

    boolean existsByNovelIdAndUserId(Long novelId, Long userId);

    long countByNovelId(Long novelId);

    List<NovelFavorite> findByUserId(Long userId);
}
