package com.blognovel.blognovel.repository;

import com.blognovel.blognovel.enums.NovelStatus;
import com.blognovel.blognovel.model.Novel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface NovelRepository extends JpaRepository<Novel, Long>, JpaSpecificationExecutor<Novel> {

    long countByStatus(NovelStatus status);
}
