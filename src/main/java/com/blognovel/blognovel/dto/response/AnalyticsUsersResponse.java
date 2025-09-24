package com.blognovel.blognovel.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnalyticsUsersResponse {
    private long totalUsers;
    private long activeUsers;
    private long inactiveUsers;
    private long adminUsers;
}
