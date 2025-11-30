package com.blognovel.blognovel.service;

import com.blognovel.blognovel.dto.request.NovelRequest;
import com.blognovel.blognovel.dto.request.ReadingProgressRequest;
import com.blognovel.blognovel.dto.response.NovelResponse;
import com.blognovel.blognovel.dto.response.PagedResponse;
import com.blognovel.blognovel.dto.response.SavedNovelsResponse;
import com.blognovel.blognovel.dto.response.UserReadingProgressResponse;
import java.security.Principal;

import org.springframework.data.domain.Pageable;

public interface NovelService {
    PagedResponse<NovelResponse> getAllNovels(String title, Long genreId, String author, Pageable pageable);

    PagedResponse<NovelResponse> getNovelsByAuthor(Long authorId, Pageable pageable);

    PagedResponse<NovelResponse> getNovelsByAuthorName(String authorName, Pageable pageable);

    PagedResponse<NovelResponse> getNovelsByCreator(Long creatorId, Pageable pageable);

    NovelResponse getNovelById(Long id, Long userId);

    PagedResponse<NovelResponse> getRelatedNovels(Long novelId, Pageable pageable);

    NovelResponse createNovel(NovelRequest novelRequest, Long currentUserId);

    NovelResponse updateNovel(Long id, NovelRequest novelRequest, Long currentUserId);

    void deleteNovel(Long id);

    void likeNovel(Long id, Long userId);

    Object getAllGenres();

    void rateNovel(Long id, int rating, Long userId);

    NovelResponse updateNovelStatus(Long id, String status);

    void incrementViewCount(Long novelId);

    void addRelatedNovel(Long novelId, Long relatedNovelId);

    PagedResponse<NovelResponse> getUserFavoriteNovels(Long userId, Pageable pageable);

    void saveNovel(Long novelId, Long userId);

    PagedResponse<NovelResponse> getSavedNovelsByUser(Long userId, Pageable pageable);

    SavedNovelsResponse getSavedNovelsByUserWithStats(Long userId, Pageable pageable);

    void updateReadingProgress(Long novelId, Long userId, ReadingProgressRequest request);

    UserReadingProgressResponse getUserReadingProgress(Long userId);
}
