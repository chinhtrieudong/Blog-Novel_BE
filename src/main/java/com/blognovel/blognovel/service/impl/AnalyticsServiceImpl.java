package com.blognovel.blognovel.service.impl;

import com.blognovel.blognovel.dto.response.AnalyticsDashboardResponse;
import com.blognovel.blognovel.dto.response.AnalyticsNovelsResponse;
import com.blognovel.blognovel.dto.response.AnalyticsPostsResponse;
import com.blognovel.blognovel.dto.response.AnalyticsTrafficResponse;
import com.blognovel.blognovel.dto.response.AnalyticsUsersResponse;
import com.blognovel.blognovel.enums.NovelStatus;
import com.blognovel.blognovel.enums.PostStatus;
import com.blognovel.blognovel.enums.Role;
import com.blognovel.blognovel.enums.Status;
import com.blognovel.blognovel.repository.ChapterRepository;
import com.blognovel.blognovel.repository.CommentRepository;
import com.blognovel.blognovel.repository.NovelRepository;
import com.blognovel.blognovel.repository.PostRepository;
import com.blognovel.blognovel.repository.UserRepository;
import com.blognovel.blognovel.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnalyticsServiceImpl implements AnalyticsService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final NovelRepository novelRepository;
    private final ChapterRepository chapterRepository;
    private final CommentRepository commentRepository;

    @Override
    public AnalyticsDashboardResponse getDashboardAnalytics() {
        long totalUsers = userRepository.count();
        long totalPosts = postRepository.count();
        long totalNovels = novelRepository.count();
        long totalComments = commentRepository.count();
        long activeUsers = userRepository.countByStatus(Status.ACTIVE);
        long publishedPosts = postRepository.countByStatus(PostStatus.PUBLISHED);
        long publishedNovels = novelRepository.countByStatus(NovelStatus.COMPLETED);

        return AnalyticsDashboardResponse.builder()
                .totalUsers(totalUsers)
                .totalPosts(totalPosts)
                .totalNovels(totalNovels)
                .totalComments(totalComments)
                .activeUsers(activeUsers)
                .publishedPosts(publishedPosts)
                .publishedNovels(publishedNovels)
                .build();
    }

    @Override
    public AnalyticsPostsResponse getPostsAnalytics() {
        long totalPosts = postRepository.count();
        long publishedPosts = postRepository.countByStatus(PostStatus.PUBLISHED);
        long draftPosts = postRepository.countByStatus(PostStatus.DRAFT);
        long totalComments = commentRepository.count();

        return AnalyticsPostsResponse.builder()
                .totalPosts(totalPosts)
                .publishedPosts(publishedPosts)
                .draftPosts(draftPosts)
                .totalComments(totalComments)
                .build();
    }

    @Override
    public AnalyticsNovelsResponse getNovelsAnalytics() {
        long totalNovels = novelRepository.count();
        long ongoingNovels = novelRepository.countByStatus(NovelStatus.ONGOING);
        long completedNovels = novelRepository.countByStatus(NovelStatus.COMPLETED);
        long hiatusNovels = novelRepository.countByStatus(NovelStatus.HIATUS);
        long cancelledNovels = novelRepository.countByStatus(NovelStatus.CANCELLED);
        long totalChapters = chapterRepository.count();

        return AnalyticsNovelsResponse.builder()
                .totalNovels(totalNovels)
                .ongoingNovels(ongoingNovels)
                .completedNovels(completedNovels)
                .hiatusNovels(hiatusNovels)
                .cancelledNovels(cancelledNovels)
                .totalChapters(totalChapters)
                .build();
    }

    @Override
    public AnalyticsUsersResponse getUsersAnalytics() {
        long totalUsers = userRepository.count();
        long activeUsers = userRepository.countByStatus(Status.ACTIVE);
        long inactiveUsers = userRepository.countByStatus(Status.BANNED);
        long adminUsers = userRepository.countByRole(Role.ADMIN);

        return AnalyticsUsersResponse.builder()
                .totalUsers(totalUsers)
                .activeUsers(activeUsers)
                .inactiveUsers(inactiveUsers)
                .adminUsers(adminUsers)
                .build();
    }

    @Override
    public AnalyticsTrafficResponse getTrafficAnalytics() {
        // Placeholder implementation since no traffic tracking is implemented
        return AnalyticsTrafficResponse.builder()
                .totalPageViews(0)
                .uniqueVisitors(0)
                .totalSessions(0)
                .build();
    }
}
