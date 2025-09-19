package com.blognovel.blognovel.dto.response;

import lombok.Data;

@Data
public class ChapterResponse {
    private Long id;
    private String title;
    private String content;
    private Integer chapterNumber;
    // Add other relevant fields
}