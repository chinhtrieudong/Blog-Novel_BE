package com.blognovel.blognovel.mapper;

import com.blognovel.blognovel.dto.response.TagResponse;
import com.blognovel.blognovel.model.Tag;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TagMapper {
    TagResponse toResponse(Tag tag);
}
