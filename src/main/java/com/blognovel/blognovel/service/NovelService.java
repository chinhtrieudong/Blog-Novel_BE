package com.blognovel.blognovel.service;

import com.blognovel.blognovel.dto.request.NovelRequest;
import com.blognovel.blognovel.dto.response.NovelResponse;
import com.blognovel.blognovel.dto.response.PagedResponse;
import java.security.Principal;

import org.springframework.data.domain.Pageable;

public interface NovelService {
    PagedResponse<NovelResponse> getAllNovels(String title, Long genreId, String author, Pageable pageable);

    PagedResponse<NovelResponse> getNovelsByAuthor(Long authorId, Pageable pageable);

    PagedResponse<NovelResponse> getNovelsByAuthorName(String authorName, Pageable pageable);

    PagedResponse<NovelResponse> getNovelsByCreator(Long creatorId, Pageable pageable);

    NovelResponse getNovelById(Long id);

    PagedResponse<NovelResponse> getRelatedNovels(Long novelId, Pageable pageable);

    NovelResponse createNovel(NovelRequest novelRequest, Long currentUserId);

    NovelResponse updateNovel(Long id, NovelRequest novelRequest, Long currentUserId);

    void deleteNovel(Long id);

    void likeNovel(Long id, Long userId);

    void favoriteNovel(Long id, Long userId);

    Object getAllGenres();

    void rateNovel(Long id, int rating, Long userId);

    NovelResponse updateNovelStatus(Long id, String status);

    void incrementViewCount(Long novelId);
}
