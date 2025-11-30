package com.blognovel.blognovel.repository;

import com.blognovel.blognovel.model.RelatedNovel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RelatedNovelRepository extends JpaRepository<RelatedNovel, Long> {
    Optional<RelatedNovel> findByNovelIdAndRelatedNovelId(Long novelId, Long relatedNovelId);

    boolean existsByNovelIdAndRelatedNovelId(Long novelId, Long relatedNovelId);

    List<RelatedNovel> findByNovelId(Long novelId);
}
