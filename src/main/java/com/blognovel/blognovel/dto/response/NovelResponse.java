package com.blognovel.blognovel.dto.response;

import com.blognovel.blognovel.enums.NovelStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
public class NovelResponse {
    private Long id;
    private String title;
    private String slug;
    private String description;
    private String coverImage;
    private AuthorResponse author;
    private NovelStatus status;
    private Long viewCount;
    private Float avgRating;
    private Set<GenreResponse> genres;
    private Integer totalChapters;
    private Long likeCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long createdBy;
    private Long updatedBy;

    // Reading progress fields (only populated if user has saved this novel)
    private Boolean isSaved;
    private Integer readChapters;
    private Integer progressPercentage;
    private String lastRead;
    private Long novelSaveId;
}
