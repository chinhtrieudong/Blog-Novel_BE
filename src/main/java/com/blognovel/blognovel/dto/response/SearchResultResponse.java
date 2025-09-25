package com.blognovel.blognovel.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchResultResponse {
    private List<PostSearchResult> posts;
    private List<NovelSearchResult> novels;
    private long totalResults;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostSearchResult {
        private Long id;
        private String title;
        private String slug;
        private String coverImage;
        private String authorName;
        private String excerpt;
        @Builder.Default
        private String type = "post";
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NovelSearchResult {
        private Long id;
        private String title;
        private String slug;
        private String coverImage;
        private String authorName;
        private String description;
        private Float avgRating;
        @Builder.Default
        private String type = "novel";
    }
}
