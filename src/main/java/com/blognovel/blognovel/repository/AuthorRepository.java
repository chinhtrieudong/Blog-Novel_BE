package com.blognovel.blognovel.repository;

import com.blognovel.blognovel.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
    Optional<Author> findByName(String name);

    Optional<Author> findByUserId(Long userId);

    boolean existsByName(String name);
}
