package com.blognovel.blognovel.repository;

import com.blognovel.blognovel.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {
}
