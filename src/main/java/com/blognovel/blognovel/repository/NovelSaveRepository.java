package com.blognovel.blognovel.repository;

import com.blognovel.blognovel.model.Novel;
import com.blognovel.blognovel.model.NovelSave;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NovelSaveRepository extends JpaRepository<NovelSave, Long> {
    Optional<NovelSave> findByNovelIdAndUserId(Long novelId, Long userId);

    boolean existsByNovelIdAndUserId(Long novelId, Long userId);

    long countByNovelId(Long novelId);

    @Query("SELECT ns FROM NovelSave ns WHERE ns.user.id = :userId")
    List<NovelSave> findAllByUserId(@Param("userId") Long userId);

    @Query("SELECT ns.novel FROM NovelSave ns WHERE ns.user.id = :userId")
    Page<Novel> findSavedNovelsByUserId(@Param("userId") Long userId, Pageable pageable);
}
