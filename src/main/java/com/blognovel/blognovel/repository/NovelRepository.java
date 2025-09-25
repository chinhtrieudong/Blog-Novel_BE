package com.blognovel.blognovel.repository;

import com.blognovel.blognovel.enums.NovelStatus;
import com.blognovel.blognovel.model.Novel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NovelRepository extends JpaRepository<Novel, Long>, JpaSpecificationExecutor<Novel> {

    long countByStatus(NovelStatus status);

    @Query("SELECT n FROM Novel n WHERE " +
            "(LOWER(n.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(n.description) LIKE LOWER(CONCAT('%', :query, '%')))")
    Page<Novel> searchNovels(@Param("query") String query, Pageable pageable);
}
