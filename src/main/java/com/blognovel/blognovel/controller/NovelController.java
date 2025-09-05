package com.blognovel.blognovel.controller;

import com.blognovel.blognovel.dto.request.NovelRequest;
import com.blognovel.blognovel.dto.response.ApiResponse;
import com.blognovel.blognovel.dto.response.NovelResponse;
import com.blognovel.blognovel.dto.response.PagedResponse;
import com.blognovel.blognovel.service.NovelService;
import com.blognovel.blognovel.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/novels")
@RequiredArgsConstructor
public class NovelController {

    private final NovelService novelService;
    private final UserService userService;

    @GetMapping
    public ApiResponse<PagedResponse<NovelResponse>> getAllNovels(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Long genreId,
            @RequestParam(required = false) String author,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        PagedResponse<NovelResponse> novels = novelService.getAllNovels(title, genreId, author, pageable);
        return ApiResponse.<PagedResponse<NovelResponse>>builder()
                .code(200)
                .message("All novels retrieved successfully")
                .data(novels)
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<NovelResponse> getNovelById(@PathVariable Long id) {
        NovelResponse novel = novelService.getNovelById(id);
        return ApiResponse.<NovelResponse>builder()
                .code(200)
                .message("Novel retrieved successfully")
                .data(novel)
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ApiResponse<NovelResponse> createNovel(@RequestBody NovelRequest novelRequest, Principal principal) {
        Long userId = userService.getCurrentUser(principal.getName()).getId();
        NovelResponse createdNovel = novelService.createNovel(novelRequest, userId);
        return ApiResponse.<NovelResponse>builder()
                .code(201)
                .message("Novel created successfully")
                .data(createdNovel)
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ApiResponse<NovelResponse> updateNovel(@PathVariable Long id, @RequestBody NovelRequest novelRequest) {
        return ApiResponse.<NovelResponse>builder()
                .code(200)
                .message("Novel updated successfully")
                .data(novelService.updateNovel(id, novelRequest))
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteNovel(@PathVariable Long id) {
        novelService.deleteNovel(id);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Novel deleted successfully")
                .build();
    }

    @PostMapping("/{id}/like")
    public ApiResponse<Void> likeNovel(@PathVariable Long id, Principal principal) {
        Long userId = userService.getCurrentUser(principal.getName()).getId();
        novelService.likeNovel(id, userId);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Novel liked successfully")
                .build();
    }

    @PostMapping("/{id}/favorite")
    public ApiResponse<Void> favoriteNovel(@PathVariable Long id, Principal principal) {
        Long userId = userService.getCurrentUser(principal.getName()).getId();
        novelService.favoriteNovel(id, userId);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Novel favorited successfully")
                .build();
    }

    @GetMapping("/genres")
    public ApiResponse<Object> getAllGenres() {
        return ApiResponse.builder()
                .code(200)
                .message("Genres retrieved successfully")
                .data(novelService.getAllGenres())
                .build();
    }

    @PostMapping("/{id}/rating")
    public ApiResponse<Void> rateNovel(@PathVariable Long id, @RequestParam int rating, Principal principal) {
        Long userId = userService.getCurrentUser(principal.getName()).getId();
        novelService.rateNovel(id, rating, userId);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Novel rated successfully")
                .build();
    }
}