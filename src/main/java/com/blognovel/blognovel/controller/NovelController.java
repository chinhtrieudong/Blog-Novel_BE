package com.blognovel.blognovel.controller;

import com.blognovel.blognovel.dto.request.NovelRequest;
import com.blognovel.blognovel.dto.response.ApiResponse;
import com.blognovel.blognovel.dto.response.NovelResponse;
import com.blognovel.blognovel.dto.response.PagedResponse;
import com.blognovel.blognovel.service.NovelService;
import com.blognovel.blognovel.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/novels")
@RequiredArgsConstructor
@Tag(name = "Novels", description = "Endpoints for managing novels")
public class NovelController {

        private final NovelService novelService;
        private final UserService userService;

        @GetMapping
        @Operation(summary = "Get all novels", description = "Retrieves a paginated list of novels, with optional filtering by title, genre, and author.")
        public ApiResponse<PagedResponse<NovelResponse>> getAllNovels(
                        @Parameter(description = "Filter by novel title") @RequestParam(required = false) String title,
                        @Parameter(description = "Filter by genre ID") @RequestParam(required = false) Long genreId,
                        @Parameter(description = "Filter by author name") @RequestParam(required = false) String author,
                        @Parameter(description = "Page number (default is 0)") @RequestParam(defaultValue = "0") int page,
                        @Parameter(description = "Page size (default is 10)") @RequestParam(defaultValue = "10") int size) {
                Pageable pageable = PageRequest.of(page, size);
                PagedResponse<NovelResponse> novels = novelService.getAllNovels(title, genreId, author, pageable);
                return ApiResponse.<PagedResponse<NovelResponse>>builder()
                                .code(200)
                                .message("All novels retrieved successfully")
                                .data(novels)
                                .build();
        }

        @GetMapping("/user/{userId}")
        @Operation(summary = "Get novels by user", description = "Retrieves a paginated list of novels by a specific user.")
        public ApiResponse<PagedResponse<NovelResponse>> getNovelsByUser(
                        @Parameter(description = "User ID") @PathVariable Long userId,
                        @Parameter(description = "Page number (default is 0)") @RequestParam(defaultValue = "0") int page,
                        @Parameter(description = "Page size (default is 10)") @RequestParam(defaultValue = "10") int size) {
                Pageable pageable = PageRequest.of(page, size);
                PagedResponse<NovelResponse> novels = novelService.getNovelsByAuthor(userId, pageable);
                return ApiResponse.<PagedResponse<NovelResponse>>builder()
                                .code(200)
                                .message("Novels by user retrieved successfully")
                                .data(novels)
                                .build();
        }

        @GetMapping("/{id}")
        @Operation(summary = "Get novel by ID", description = "Retrieves a novel by its unique identifier.")
        public ApiResponse<NovelResponse> getNovelById(@Parameter(description = "Novel ID") @PathVariable Long id) {
                NovelResponse novel = novelService.getNovelById(id);
                return ApiResponse.<NovelResponse>builder()
                                .code(200)
                                .message("Novel retrieved successfully")
                                .data(novel)
                                .build();
        }

        @PostMapping
        @Operation(summary = "Create a new novel", description = "Creates a new novel. Requires authentication.")
        public ApiResponse<NovelResponse> createNovel(@ModelAttribute NovelRequest novelRequest, Principal principal) {
                Long userId = userService.getCurrentUser(principal.getName()).getId();
                NovelResponse createdNovel = novelService.createNovel(novelRequest, userId);
                return ApiResponse.<NovelResponse>builder()
                                .code(201)
                                .message("Novel created successfully")
                                .data(createdNovel)
                                .build();
        }

        @PutMapping("/{id}")
        @Operation(summary = "Update novel (Admin only)", description = "Updates an existing novel. Requires ADMIN role.")
        public ApiResponse<NovelResponse> updateNovel(
                        @Parameter(description = "Novel ID") @PathVariable Long id,
                        @ModelAttribute NovelRequest novelRequest) {
                return ApiResponse.<NovelResponse>builder()
                                .code(200)
                                .message("Novel updated successfully")
                                .data(novelService.updateNovel(id, novelRequest))
                                .build();
        }

        @DeleteMapping("/{id}")
        @Operation(summary = "Delete novel (Admin only)", description = "Deletes a novel. Requires ADMIN role.")
        public ApiResponse<Void> deleteNovel(@Parameter(description = "Novel ID") @PathVariable Long id) {
                novelService.deleteNovel(id);
                return ApiResponse.<Void>builder()
                                .code(200)
                                .message("Novel deleted successfully")
                                .build();
        }

        @PostMapping("/{id}/like")
        @Operation(summary = "Like a novel", description = "Likes a novel for the currently logged-in user.")
        public ApiResponse<Void> likeNovel(@Parameter(description = "Novel ID") @PathVariable Long id,
                        Principal principal) {
                Long userId = userService.getCurrentUser(principal.getName()).getId();
                novelService.likeNovel(id, userId);
                return ApiResponse.<Void>builder()
                                .code(200)
                                .message("Novel liked successfully")
                                .build();
        }

        @PostMapping("/{id}/favorite")
        @Operation(summary = "Favorite a novel", description = "Adds a novel to the user's favorites.")
        public ApiResponse<Void> favoriteNovel(@Parameter(description = "Novel ID") @PathVariable Long id,
                        Principal principal) {
                Long userId = userService.getCurrentUser(principal.getName()).getId();
                novelService.favoriteNovel(id, userId);
                return ApiResponse.<Void>builder()
                                .code(200)
                                .message("Novel favorited successfully")
                                .build();
        }

        @GetMapping("/genres")
        @Operation(summary = "Get all genres", description = "Retrieves a list of all available genres.")
        public ApiResponse<Object> getAllGenres() {
                return ApiResponse.builder()
                                .code(200)
                                .message("Genres retrieved successfully")
                                .data(novelService.getAllGenres())
                                .build();
        }

        @PostMapping("/{id}/rating")
        @Operation(summary = "Rate a novel", description = "Rates a novel for the currently logged-in user.")
        public ApiResponse<Void> rateNovel(
                        @Parameter(description = "Novel ID") @PathVariable Long id,
                        @Parameter(description = "Rating value") @RequestParam int rating,
                        Principal principal) {
                Long userId = userService.getCurrentUser(principal.getName()).getId();
                novelService.rateNovel(id, rating, userId);
                return ApiResponse.<Void>builder()
                                .code(200)
                                .message("Novel rated successfully")
                                .build();
        }

        @PatchMapping("/{id}/status")
        @Operation(summary = "Update novel status", description = "Updates the status of a specific novel. Requires authentication.")
        public ApiResponse<NovelResponse> updateNovelStatus(
                        @Parameter(description = "Novel ID") @PathVariable Long id,
                        @Parameter(description = "New status (ONGOING, COMPLETED, HIATUS, CANCELLED)") @RequestParam String status) {
                NovelResponse updatedNovel = novelService.updateNovelStatus(id, status);
                return ApiResponse.<NovelResponse>builder()
                                .code(200)
                                .message("Novel status updated successfully")
                                .data(updatedNovel)
                                .build();
        }
}
