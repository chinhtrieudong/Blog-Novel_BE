package com.blognovel.blognovel.mapper;

import com.blognovel.blognovel.dto.request.NovelRequest;
import com.blognovel.blognovel.dto.response.NovelResponse;
import com.blognovel.blognovel.exception.AppException;
import com.blognovel.blognovel.exception.ErrorCode;
import com.blognovel.blognovel.model.Author;
import com.blognovel.blognovel.model.Genre;
import com.blognovel.blognovel.model.Novel;
import com.blognovel.blognovel.service.CloudinaryService;
import com.blognovel.blognovel.service.util.SlugGenerator;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class NovelMapper {

    @Mapping(target = "slug", expression = "java(generateSlug(novelRequest.getTitle()))")
    @Mapping(target = "author.id", expression = "java(getFirstAuthorId(novelRequest.getAuthorIds()))")
    @Mapping(target = "genres", expression = "java(createGenres(novelRequest.getGenreIds()))")
    @Mapping(target = "coverImage", expression = "java(uploadImage(novelRequest.getCoverImage(), cloudinaryService))")
    public abstract Novel toEntity(NovelRequest novelRequest, @Context CloudinaryService cloudinaryService);

    @Mapping(target = "title", expression = "java(processTitle(novel.getTitle()))")
    @Mapping(target = "description", expression = "java(processDescription(novel.getDescription()))")
    @Mapping(target = "coverImage", expression = "java(processCoverImage(novel.getCoverImage()))")
    public abstract NovelResponse toResponse(Novel novel);

    @Mapping(target = "slug", expression = "java(generateSlug(novelRequest.getTitle()))")
    @Mapping(target = "author.id", expression = "java(getFirstAuthorId(novelRequest.getAuthorIds()))")
    @Mapping(target = "genres", expression = "java(createGenres(novelRequest.getGenreIds()))")
    @Mapping(target = "coverImage", expression = "java(uploadImage(novelRequest.getCoverImage(), cloudinaryService))")
    @Mapping(target = "updatedAt", ignore = true)
    public abstract void updateNovelFromDto(NovelRequest novelRequest, @MappingTarget Novel novel,
            @Context CloudinaryService cloudinaryService);

    @Named("generateSlug")
    protected String generateSlug(String title) {
        try {
            return SlugGenerator.makeSlug(title);
        } catch (IllegalArgumentException e) {
            // If title is null or empty, generate a fallback slug
            return "novel-" + System.currentTimeMillis();
        }
    }

    @Named("getFirstAuthorId")
    protected Long getFirstAuthorId(List<Long> authorIds) {
        return authorIds != null && !authorIds.isEmpty() ? authorIds.get(0) : null;
    }

    protected Author createAuthor(Long authorId) {
        return Author.builder()
                .id(authorId)
                .build();
    }

    @Named("createGenres")
    protected Set<Genre> createGenres(List<Long> genreIds) {
        Set<Genre> genres = new HashSet<>();
        if (genreIds != null) {
            for (Long genreId : genreIds) {
                if (genreId != null) {
                    Genre genre = Genre.builder()
                            .id(genreId)
                            .build();
                    genres.add(genre);
                }
            }
        }
        return genres;
    }

    @Named("uploadImage")
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

    @Named("processCoverImage")
    protected String processCoverImage(String coverImage) {
        if (coverImage == null || coverImage.isEmpty()) {
            return null;
        }
        if (coverImage.startsWith("http")) {
            // If it starts with http but missing ://, fix it
            if (coverImage.startsWith("httpsres") || coverImage.startsWith("httpres")) {
                return "https://" + coverImage.substring(coverImage.startsWith("httpsres") ? 5 : 4);
            }
            return coverImage;
        }
        // If it's a filename, assume it's a Cloudinary public ID or something
        // For now, return as is, or construct URL
        // Since we don't have the base URL, perhaps return null or the filename
        // But to fix, perhaps assume it's a public ID and construct URL
        // But Cloudinary URLs are complex.
        // For now, if not starting with http, return null or the string
        return coverImage; // Keep as is for now
    }

    @Named("processTitle")
    protected String processTitle(String title) {
        if (title == null || title.isEmpty()) {
            return title;
        }
        // If it looks like a slug (contains - and no spaces), try to unslug it
        if (title.contains("-") && !title.contains(" ")) {
            String[] parts = title.split("-");
            StringBuilder sb = new StringBuilder();
            for (String part : parts) {
                if (!part.isEmpty()) {
                    sb.append(part.substring(0, 1).toUpperCase()).append(part.substring(1)).append(" ");
                }
            }
            return sb.toString().trim();
        }
        return title;
    }

    @Named("processDescription")
    protected String processDescription(String description) {
        if (description == null || description.isEmpty()) {
            return description;
        }
        // If it looks like a slug (contains - and no spaces), try to unslug it
        if (description.contains("-") && !description.contains(" ")) {
            return description.replace("-", " ");
        }
        return description;
    }
}
