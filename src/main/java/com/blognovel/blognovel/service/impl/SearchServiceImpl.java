package com.blognovel.blognovel.service.impl;

import com.blognovel.blognovel.dto.response.PagedResponse;
import com.blognovel.blognovel.dto.response.SearchResultResponse;
import com.blognovel.blognovel.dto.response.SearchSuggestionResponse;
import com.blognovel.blognovel.model.Novel;
import com.blognovel.blognovel.model.Post;
import com.blognovel.blognovel.repository.NovelRepository;
import com.blognovel.blognovel.repository.PostRepository;
import com.blognovel.blognovel.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {

        private final PostRepository postRepository;
        private final NovelRepository novelRepository;

        @Override
        public SearchResultResponse globalSearch(String query, Pageable pageable) {
                List<SearchResultResponse.PostSearchResult> postResults = new ArrayList<>();
                List<SearchResultResponse.NovelSearchResult> novelResults = new ArrayList<>();

                if (StringUtils.hasText(query)) {
                        // Search posts
                        Page<Post> posts = postRepository.searchPublishedPosts(query, pageable);
                        postResults = posts.getContent().stream()
                                        .map(this::mapToPostSearchResult)
                                        .collect(Collectors.toList());

                        // Search novels
                        Page<Novel> novels = novelRepository.searchNovels(query, pageable);
                        novelResults = novels.getContent().stream()
                                        .map(this::mapToNovelSearchResult)
                                        .collect(Collectors.toList());
                }

                long totalResults = postResults.size() + novelResults.size();

                return SearchResultResponse.builder()
                                .posts(postResults)
                                .novels(novelResults)
                                .totalResults(totalResults)
                                .build();
        }

        @Override
        public PagedResponse<SearchResultResponse.PostSearchResult> searchPosts(String query, Pageable pageable) {
                Page<Post> posts = StringUtils.hasText(query) ? postRepository.searchPublishedPosts(query, pageable)
                                : postRepository.findAll(pageable);

                List<SearchResultResponse.PostSearchResult> postResults = posts.getContent().stream()
                                .map(this::mapToPostSearchResult)
                                .collect(Collectors.toList());

                return PagedResponse.<SearchResultResponse.PostSearchResult>builder()
                                .content(postResults)
                                .page(posts.getNumber())
                                .size(posts.getSize())
                                .totalElements(posts.getTotalElements())
                                .totalPages(posts.getTotalPages())
                                .last(posts.isLast())
                                .build();
        }

        @Override
        public PagedResponse<SearchResultResponse.NovelSearchResult> searchNovels(String query, Pageable pageable) {
                Page<Novel> novels = StringUtils.hasText(query) ? novelRepository.searchNovels(query, pageable)
                                : novelRepository.findAll(pageable);

                List<SearchResultResponse.NovelSearchResult> novelResults = novels.getContent().stream()
                                .map(this::mapToNovelSearchResult)
                                .collect(Collectors.toList());

                return PagedResponse.<SearchResultResponse.NovelSearchResult>builder()
                                .content(novelResults)
                                .page(novels.getNumber())
                                .size(novels.getSize())
                                .totalElements(novels.getTotalElements())
                                .totalPages(novels.getTotalPages())
                                .last(novels.isLast())
                                .build();
        }

        @Override
        public SearchSuggestionResponse getSearchSuggestions(String query) {
                List<String> suggestions = new ArrayList<>();

                if (StringUtils.hasText(query)) {
                        // Get post title suggestions
                        List<Post> posts = postRepository.searchPublishedPosts(query, Pageable.ofSize(5)).getContent();
                        suggestions.addAll(posts.stream()
                                        .map(Post::getTitle)
                                        .collect(Collectors.toList()));

                        // Get novel title suggestions
                        List<Novel> novels = novelRepository.searchNovels(query, Pageable.ofSize(5)).getContent();
                        suggestions.addAll(novels.stream()
                                        .map(Novel::getTitle)
                                        .collect(Collectors.toList()));
                }

                // Remove duplicates and limit to 10 suggestions
                suggestions = suggestions.stream()
                                .distinct()
                                .limit(10)
                                .collect(Collectors.toList());

                return SearchSuggestionResponse.builder()
                                .suggestions(suggestions)
                                .build();
        }

        private SearchResultResponse.PostSearchResult mapToPostSearchResult(Post post) {
                String excerpt = post.getContent();
                if (excerpt != null && excerpt.length() > 200) {
                        excerpt = excerpt.substring(0, 200) + "...";
                }

                return SearchResultResponse.PostSearchResult.builder()
                                .id(post.getId())
                                .title(post.getTitle())
                                .slug(post.getSlug())
                                .coverImage(post.getCoverImage())
                                .authorName(post.getAuthor().getFullName())
                                .excerpt(excerpt)
                                .build();
        }

        private SearchResultResponse.NovelSearchResult mapToNovelSearchResult(Novel novel) {
                return SearchResultResponse.NovelSearchResult.builder()
                                .id(novel.getId())
                                .title(novel.getTitle())
                                .slug(novel.getSlug())
                                .coverImage(novel.getCoverImage())
                                .authorName(novel.getAuthor().getName())
                                .description(novel.getDescription())
                                .avgRating(novel.getAvgRating())
                                .build();
        }
}
