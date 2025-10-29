package com.blognovel.blognovel.repository;

import com.blognovel.blognovel.model.NovelRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NovelRatingRepository extends JpaRepository<NovelRating, Long> {
    Optional<NovelRating> findByNovelIdAndUserId(Long novelId, Long userId);

    boolean existsByNovelIdAndUserId(Long novelId, Long userId);

    List<NovelRating> findByNovelId(Long novelId);

    @Query("SELECT AVG(r.rating) FROM NovelRating r WHERE r.novel.id = :novelId")
    Double findAverageRatingByNovelId(@Param("novelId") Long novelId);

    long countByNovelId(Long novelId);
}
