package com.blognovel.blognovel.repository;

import com.blognovel.blognovel.model.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChapterRepository extends JpaRepository<Chapter, Long> {
    List<Chapter> findByNovelId(Long novelId);
    Optional<Chapter> findByIdAndNovelId(Long id, Long novelId);
}
