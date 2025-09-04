package com.blognovel.blognovel.dto.request;

import lombok.Data;
import java.util.Set;

@Data
public class PostRequest {
    private String title;
    private String content;
    private String coverImage;
    private Long authorId;
    private String status;
    private Set<Long> categoryIds;
    private Set<Long> tagIds;
}
