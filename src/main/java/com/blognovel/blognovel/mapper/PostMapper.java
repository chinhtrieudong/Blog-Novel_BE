package com.blognovel.blognovel.mapper;

import com.blognovel.blognovel.dto.response.PostResponse;
import com.blognovel.blognovel.model.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", uses = { CategoryMapper.class,
        TagMapper.class }, unmappedTargetPolicy = ReportingPolicy.IGNORE // gọi mapper con
)
public interface PostMapper {

    @Mapping(target = "authorId", source = "author.id")
    @Mapping(target = "authorName", source = "author.username")
    PostResponse toResponse(Post post);
}
