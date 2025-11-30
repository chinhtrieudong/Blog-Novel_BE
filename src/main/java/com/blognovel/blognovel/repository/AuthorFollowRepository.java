package com.blognovel.blognovel.repository;

import com.blognovel.blognovel.model.AuthorFollow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthorFollowRepository extends JpaRepository<AuthorFollow, Long> {
    Optional<AuthorFollow> findByAuthorIdAndUserId(Long authorId, Long userId);

    boolean existsByAuthorIdAndUserId(Long authorId, Long userId);

    long countByAuthorId(Long authorId);

    List<AuthorFollow> findByUserId(Long userId);
}
