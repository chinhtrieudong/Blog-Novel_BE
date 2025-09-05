package com.blognovel.blognovel.dto.response;

import com.blognovel.blognovel.enums.NovelStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class NovelResponse {
    private Long id;
    private String title;
    private String slug;
    private String description;
    private String coverImage;
    private UserResponse author;
    private NovelStatus status;
    private Long viewCount;
    private Float avgRating;
    private Set<GenreResponse> genres;
}
