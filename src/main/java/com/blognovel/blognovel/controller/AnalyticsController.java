package com.blognovel.blognovel.controller;

import com.blognovel.blognovel.dto.response.AnalyticsDashboardResponse;
import com.blognovel.blognovel.dto.response.AnalyticsNovelsResponse;
import com.blognovel.blognovel.dto.response.AnalyticsPostsResponse;
import com.blognovel.blognovel.dto.response.AnalyticsTrafficResponse;
import com.blognovel.blognovel.dto.response.AnalyticsUsersResponse;
import com.blognovel.blognovel.dto.response.ApiResponse;
import com.blognovel.blognovel.service.AnalyticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
@Tag(name = "Analytics", description = "Endpoints for retrieving analytics data (Admin only)")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get dashboard statistics", description = "Retrieves overall dashboard statistics for admin users.")
    public ApiResponse<AnalyticsDashboardResponse> getDashboardAnalytics() {
        return ApiResponse.<AnalyticsDashboardResponse>builder()
                .code(200)
                .message("Dashboard analytics retrieved successfully")
                .data(analyticsService.getDashboardAnalytics())
                .build();
    }

    @GetMapping("/posts")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get post analytics", description = "Retrieves analytics data for posts.")
    public ApiResponse<AnalyticsPostsResponse> getPostsAnalytics() {
        return ApiResponse.<AnalyticsPostsResponse>builder()
                .code(200)
                .message("Post analytics retrieved successfully")
                .data(analyticsService.getPostsAnalytics())
                .build();
    }

    @GetMapping("/novels")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get novel analytics", description = "Retrieves analytics data for novels.")
    public ApiResponse<AnalyticsNovelsResponse> getNovelsAnalytics() {
        return ApiResponse.<AnalyticsNovelsResponse>builder()
                .code(200)
                .message("Novel analytics retrieved successfully")
                .data(analyticsService.getNovelsAnalytics())
                .build();
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get user analytics", description = "Retrieves analytics data for users.")
    public ApiResponse<AnalyticsUsersResponse> getUsersAnalytics() {
        return ApiResponse.<AnalyticsUsersResponse>builder()
                .code(200)
                .message("User analytics retrieved successfully")
                .data(analyticsService.getUsersAnalytics())
                .build();
    }

    @GetMapping("/traffic")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get traffic analytics", description = "Retrieves traffic analytics data.")
    public ApiResponse<AnalyticsTrafficResponse> getTrafficAnalytics() {
        return ApiResponse.<AnalyticsTrafficResponse>builder()
                .code(200)
                .message("Traffic analytics retrieved successfully")
                .data(analyticsService.getTrafficAnalytics())
                .build();
    }
}
