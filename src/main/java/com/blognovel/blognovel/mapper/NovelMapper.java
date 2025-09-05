package com.blognovel.blognovel.mapper;

import com.blognovel.blognovel.dto.request.NovelRequest;
import com.blognovel.blognovel.dto.response.NovelResponse;
import com.blognovel.blognovel.model.Novel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface NovelMapper {

    @Mapping(source = "authorId", target = "author.id")
    Novel toEntity(NovelRequest novelRequest);

    NovelResponse toResponse(Novel novel);

    @Mapping(source = "authorId", target = "author.id")
    void updateNovelFromDto(NovelRequest novelRequest, @MappingTarget Novel novel);
}