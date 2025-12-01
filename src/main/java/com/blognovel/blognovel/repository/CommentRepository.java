package com.blognovel.blognovel.repository;

import com.blognovel.blognovel.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>, JpaSpecificationExecutor<Comment> {

    // Remove LEFT JOIN FETCH to avoid nested reply loops
    @Query("SELECT c FROM Comment c WHERE c.post.id = :postId AND c.parent IS NULL ORDER BY c.createdAt ASC")
    List<Comment> findByPostIdAndParentIsNullOrderByCreatedAtAsc(Long postId);

    @Query("SELECT c FROM Comment c WHERE c.novel.id = :novelId AND c.parent IS NULL ORDER BY c.createdAt ASC")
    List<Comment> findByNovelIdAndParentIsNullOrderByCreatedAtAsc(Long novelId);

    @Query("SELECT c FROM Comment c WHERE c.chapter.id = :chapterId AND c.parent IS NULL ORDER BY c.createdAt ASC")
    List<Comment> findByChapterIdAndParentIsNullOrderByCreatedAtAsc(Long chapterId);

    @Modifying
    @Query("UPDATE Comment c SET c.likeCount = c.likeCount + 1 WHERE c.id = :commentId")
    void incrementLikeCount(@Param("commentId") Long commentId);

    @Modifying
    @Query("UPDATE Comment c SET c.likeCount = c.likeCount - 1 WHERE c.id = :commentId AND c.likeCount > 0")
    void decrementLikeCount(@Param("commentId") Long commentId);
}
