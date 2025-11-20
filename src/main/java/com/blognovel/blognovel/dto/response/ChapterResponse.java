package com.blognovel.blognovel.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChapterResponse {
    private Long id;
    private String title;
    private String content;
    private Integer chapterNumber;
    private Long viewCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long createdBy;
    private Long updatedBy;
    // Add other relevant fields
}
