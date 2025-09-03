package com.blognovel.blognovel.repository;

import com.blognovel.blognovel.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
