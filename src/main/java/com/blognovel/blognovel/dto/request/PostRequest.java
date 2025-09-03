package com.blognovel.blognovel.dto.request;

import lombok.Data;
import java.util.Set;
import com.blognovel.blognovel.enums.PostStatus;

@Data
public class PostRequest {
    private String title;
    private String content;
    private String coverImage;
    private PostStatus status;
    private Set<Long> categoryIds;
    private Set<Long> tagIds;
}
