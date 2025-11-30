package com.blognovel.blognovel.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class ReadingProgressResponse {
    private Long novelId;
    private String novelTitle;
    private String novelSlug;
    private String coverImage;
    private Integer totalChapters;
    private Integer readChapters;
    private String savedAt;
    private String lastRead;
    private String status; // "unread", "reading", "completed"
    private Integer progressPercentage;
}
