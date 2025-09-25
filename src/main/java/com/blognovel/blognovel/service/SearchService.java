package com.blognovel.blognovel.service;

import com.blognovel.blognovel.dto.response.PagedResponse;
import com.blognovel.blognovel.dto.response.SearchResultResponse;
import com.blognovel.blognovel.dto.response.SearchSuggestionResponse;
import org.springframework.data.domain.Pageable;

public interface SearchService {
    SearchResultResponse globalSearch(String query, Pageable pageable);

    PagedResponse<SearchResultResponse.PostSearchResult> searchPosts(String query, Pageable pageable);

    PagedResponse<SearchResultResponse.NovelSearchResult> searchNovels(String query, Pageable pageable);

    SearchSuggestionResponse getSearchSuggestions(String query);
}
