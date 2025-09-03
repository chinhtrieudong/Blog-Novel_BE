package com.blognovel.blognovel.dto.response;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
public class PostResponse {
    private Long id;
    private String title;
    private String content;
    private String coverImage;
    private String status;
    private Long viewCount;
    private UserSummary author;
    private Set<CategoryResponse> categories;
    private Set<TagResponse> tags;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean likedByCurrentUser;
    private Long likeCount;

    @Data
    @Builder
    public static class UserSummary {
        private Long id;
        private String username;
        private String avatarUrl;
    }
}
