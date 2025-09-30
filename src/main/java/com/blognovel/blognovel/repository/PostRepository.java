package com.blognovel.blognovel.repository;

import com.blognovel.blognovel.model.Post;
import com.blognovel.blognovel.model.Category;
import com.blognovel.blognovel.model.Tag;
import com.blognovel.blognovel.enums.PostStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface PostRepository extends JpaRepository<Post, Long>, JpaSpecificationExecutor<Post> {
        @Query("SELECT p FROM Post p WHERE (:status IS NULL OR p.status = :status) " +
                        "AND (:title IS NULL OR LOWER(p.title) LIKE LOWER(CONCAT('%', :title, '%'))) " +
                        "AND (:category IS NULL OR :category MEMBER OF p.categories) " +
                        "AND (:tag IS NULL OR :tag MEMBER OF p.tags)")
        Page<Post> searchPosts(@Param("status") PostStatus status,
                        @Param("title") String title,
                        @Param("category") Category category,
                        @Param("tag") Tag tag,
                        Pageable pageable);

        @Modifying
        @Query("UPDATE Post p SET p.viewCount = p.viewCount + 1 WHERE p.id = :postId")
        void incrementViewCount(@Param("postId") Long postId);

        long countByStatus(PostStatus status);

        @Query("SELECT p FROM Post p WHERE p.status = 'PUBLISHED' AND " +
                        "(LOWER(p.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
                        "LOWER(CAST(p.content AS STRING)) LIKE LOWER(CONCAT('%', :query, '%')))")
        Page<Post> searchPublishedPosts(@Param("query") String query, Pageable pageable);

        Page<Post> findByAuthorId(Long authorId, Pageable pageable);
}
