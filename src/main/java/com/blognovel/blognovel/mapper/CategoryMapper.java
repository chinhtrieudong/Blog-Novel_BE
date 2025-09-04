package com.blognovel.blognovel.mapper;

import com.blognovel.blognovel.dto.response.CategoryResponse;
import com.blognovel.blognovel.model.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryResponse toResponse(Category category);
}