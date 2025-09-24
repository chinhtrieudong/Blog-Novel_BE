package com.blognovel.blognovel.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnalyticsDashboardResponse {
    private long totalUsers;
    private long totalPosts;
    private long totalNovels;
    private long totalComments;
    private long activeUsers;
    private long publishedPosts;
    private long publishedNovels;
}
