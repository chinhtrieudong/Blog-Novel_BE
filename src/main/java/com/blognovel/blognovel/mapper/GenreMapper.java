package com.blognovel.blognovel.mapper;

import com.blognovel.blognovel.dto.response.GenreResponse;
import com.blognovel.blognovel.model.Genre;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GenreMapper {
    GenreResponse toResponse(Genre genre);
}
