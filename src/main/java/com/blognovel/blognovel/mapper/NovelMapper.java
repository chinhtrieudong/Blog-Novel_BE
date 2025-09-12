package com.blognovel.blognovel.mapper;

import com.blognovel.blognovel.dto.request.NovelRequest;
import com.blognovel.blognovel.dto.response.NovelResponse;
import com.blognovel.blognovel.model.Novel;
import com.blognovel.blognovel.service.CloudinaryService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Mapper(componentModel = "spring")
public abstract class NovelMapper {

    @Autowired
    private CloudinaryService cloudinaryService;

    @Mapping(target = "author.id", source = "authorId")
    @Mapping(target = "coverImage", expression = "java(uploadImage(novelRequest.getCoverImage()))")
    public abstract Novel toEntity(NovelRequest novelRequest);

    public abstract NovelResponse toResponse(Novel novel);

    @Mapping(target = "author.id", source = "authorId")
    @Mapping(target = "coverImage", expression = "java(uploadImage(novelRequest.getCoverImage()))")
    public abstract void updateNovelFromDto(NovelRequest novelRequest, @MappingTarget Novel novel);

    protected String uploadImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }
        try {
            return cloudinaryService.uploadImage(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}