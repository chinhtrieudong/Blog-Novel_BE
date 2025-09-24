package com.blognovel.blognovel.service;

import com.blognovel.blognovel.dto.response.AnalyticsDashboardResponse;
import com.blognovel.blognovel.dto.response.AnalyticsNovelsResponse;
import com.blognovel.blognovel.dto.response.AnalyticsPostsResponse;
import com.blognovel.blognovel.dto.response.AnalyticsTrafficResponse;
import com.blognovel.blognovel.dto.response.AnalyticsUsersResponse;

public interface AnalyticsService {
    AnalyticsDashboardResponse getDashboardAnalytics();

    AnalyticsPostsResponse getPostsAnalytics();

    AnalyticsNovelsResponse getNovelsAnalytics();

    AnalyticsUsersResponse getUsersAnalytics();

    AnalyticsTrafficResponse getTrafficAnalytics();
}
