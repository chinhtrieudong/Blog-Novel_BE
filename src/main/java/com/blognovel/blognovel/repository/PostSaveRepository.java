package com.blognovel.blognovel.repository;

import com.blognovel.blognovel.model.Post;
import com.blognovel.blognovel.model.PostSave;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostSaveRepository extends JpaRepository<PostSave, Long> {
    Optional<PostSave> findByPostIdAndUserId(Long postId, Long userId);

    boolean existsByPostIdAndUserId(Long postId, Long userId);

    long countByPostId(Long postId);

    @Query("SELECT ps.post FROM PostSave ps WHERE ps.user.id = :userId")
    Page<Post> findSavedPostsByUserId(@Param("userId") Long userId, Pageable pageable);
}
