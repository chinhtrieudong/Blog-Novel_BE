package com.blognovel.blognovel.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnalyticsPostsResponse {
    private long totalPosts;
    private long publishedPosts;
    private long draftPosts;
    private long totalComments;
}
