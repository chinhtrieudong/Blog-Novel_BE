package com.blognovel.blognovel.mapper;

import com.blognovel.blognovel.dto.request.NovelRequest;
import com.blognovel.blognovel.dto.response.NovelResponse;
import com.blognovel.blognovel.exception.AppException;
import com.blognovel.blognovel.exception.ErrorCode;
import com.blognovel.blognovel.model.Novel;
import com.blognovel.blognovel.service.CloudinaryService;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class NovelMapper {

    @Mapping(target = "author.id", source = "authorId")
    @Mapping(target = "coverImage", expression = "java(uploadImage(novelRequest.getCoverImage(), cloudinaryService))")
    public abstract Novel toEntity(NovelRequest novelRequest, @Context CloudinaryService cloudinaryService);

    public abstract NovelResponse toResponse(Novel novel);

    @Mapping(target = "author.id", source = "authorId")
    @Mapping(target = "coverImage", expression = "java(uploadImage(novelRequest.getCoverImage(), cloudinaryService))")
    @Mapping(target = "updatedAt", ignore = true)
    public abstract void updateNovelFromDto(NovelRequest novelRequest, @MappingTarget Novel novel,
            @Context CloudinaryService cloudinaryService);

    protected String uploadImage(MultipartFile file, @Context CloudinaryService cloudinaryService) {
        if (file == null || file.isEmpty()) {
            return null;
        }
        try {
            return cloudinaryService.uploadImage(file);
        } catch (IOException e) {
            throw new AppException(ErrorCode.UPLOAD_FAILED);
        }
    }
}
