package com.blognovel.blognovel.mapper;

import com.blognovel.blognovel.dto.response.PostResponse;
import com.blognovel.blognovel.model.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = { CategoryMapper.class, TagMapper.class } // gọi mapper con
)
public interface PostMapper {

    @Mapping(target = "authorName", source = "author.username")
    PostResponse toResponse(Post post);
}
