package com.blognovel.blognovel.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnalyticsNovelsResponse {
    private long totalNovels;
    private long ongoingNovels;
    private long completedNovels;
    private long hiatusNovels;
    private long cancelledNovels;
    private long totalChapters;
}
