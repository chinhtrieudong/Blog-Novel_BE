package com.blognovel.blognovel.controller;

import com.blognovel.blognovel.dto.response.ApiResponse;
import com.blognovel.blognovel.dto.response.PagedResponse;
import com.blognovel.blognovel.dto.response.SearchResultResponse;
import com.blognovel.blognovel.dto.response.SearchSuggestionResponse;
import com.blognovel.blognovel.service.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
@Tag(name = "Search", description = "Endpoints for searching posts and novels")
public class SearchController {

        private final SearchService searchService;

        @GetMapping
        @Operation(summary = "Global search across posts and novels", description = "Searches for posts and novels matching the query.")
        public ApiResponse<SearchResultResponse> globalSearch(
                        @Parameter(description = "Search query") @RequestParam String q,
                        @Parameter(description = "Page number (default is 0)") @RequestParam(defaultValue = "0") int page,
                        @Parameter(description = "Page size (default is 10)") @RequestParam(defaultValue = "10") int size) {
                Pageable pageable = PageRequest.of(page, size);
                SearchResultResponse results = searchService.globalSearch(q, pageable);
                return ApiResponse.<SearchResultResponse>builder()
                                .code(200)
                                .message("Search completed successfully")
                                .data(results)
                                .build();
        }

        @GetMapping("/posts")
        @Operation(summary = "Search blog posts only", description = "Searches for blog posts matching the query.")
        public ApiResponse<PagedResponse<SearchResultResponse.PostSearchResult>> searchPosts(
                        @Parameter(description = "Search query") @RequestParam(required = false) String q,
                        @Parameter(description = "Page number (default is 0)") @RequestParam(defaultValue = "0") int page,
                        @Parameter(description = "Page size (default is 10)") @RequestParam(defaultValue = "10") int size) {
                Pageable pageable = PageRequest.of(page, size);
                PagedResponse<SearchResultResponse.PostSearchResult> results = searchService.searchPosts(q, pageable);
                return ApiResponse.<PagedResponse<SearchResultResponse.PostSearchResult>>builder()
                                .code(200)
                                .message("Post search completed successfully")
                                .data(results)
                                .build();
        }

        @GetMapping("/novels")
        @Operation(summary = "Search novels only", description = "Searches for novels matching the query.")
        public ApiResponse<PagedResponse<SearchResultResponse.NovelSearchResult>> searchNovels(
                        @Parameter(description = "Search query") @RequestParam(required = false) String q,
                        @Parameter(description = "Page number (default is 0)") @RequestParam(defaultValue = "0") int page,
                        @Parameter(description = "Page size (default is 10)") @RequestParam(defaultValue = "10") int size) {
                Pageable pageable = PageRequest.of(page, size);
                PagedResponse<SearchResultResponse.NovelSearchResult> results = searchService.searchNovels(q, pageable);
                return ApiResponse.<PagedResponse<SearchResultResponse.NovelSearchResult>>builder()
                                .code(200)
                                .message("Novel search completed successfully")
                                .data(results)
                                .build();
        }

        @GetMapping("/suggestions")
        @Operation(summary = "Get search suggestions", description = "Returns search suggestions based on the query.")
        public ApiResponse<SearchSuggestionResponse> getSearchSuggestions(
                        @Parameter(description = "Search query") @RequestParam String q) {
                SearchSuggestionResponse suggestions = searchService.getSearchSuggestions(q);
                return ApiResponse.<SearchSuggestionResponse>builder()
                                .code(200)
                                .message("Search suggestions retrieved successfully")
                                .data(suggestions)
                                .build();
        }
}
