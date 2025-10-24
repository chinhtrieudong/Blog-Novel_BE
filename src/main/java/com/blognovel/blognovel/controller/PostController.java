package com.blognovel.blognovel.controller;

import com.blognovel.blognovel.dto.request.PostRequest;
import com.blognovel.blognovel.dto.response.ApiResponse;
import com.blognovel.blognovel.dto.response.PagedResponse;
import com.blognovel.blognovel.dto.response.PostResponse;
import com.blognovel.blognovel.exception.AppException;
import com.blognovel.blognovel.exception.ErrorCode;
import com.blognovel.blognovel.model.User;
import com.blognovel.blognovel.repository.UserRepository;
import com.blognovel.blognovel.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
@Tag(name = "Posts", description = "Endpoints for managing blog posts")
public class PostController {

        private final PostService postService;
        private final UserRepository userRepository;

        @GetMapping
        @Operation(summary = "Get all posts", description = "Retrieves a paginated list of posts, with optional filtering.")
        public ApiResponse<PagedResponse<PostResponse>> getAllPosts(
                        @Parameter(description = "Filter by post title") @RequestParam(required = false) String title,
                        @Parameter(description = "Filter by category ID") @RequestParam(required = false) Long categoryId,
                        @Parameter(description = "Filter by tag ID") @RequestParam(required = false) Long tagId,
                        @Parameter(description = "Filter by post status") @RequestParam(required = false) String status,
                        @Parameter(description = "Page number (default is 0)") @RequestParam(defaultValue = "0") int page,
                        @Parameter(description = "Page size (default is 10)") @RequestParam(defaultValue = "10") int size) {
                Pageable pageable = PageRequest.of(page, size);
                PagedResponse<PostResponse> posts = postService.getAllPosts(title, categoryId, tagId, status, pageable);
                return ApiResponse.<PagedResponse<PostResponse>>builder()
                                .code(200)
                                .message("All posts retrieved successfully")
                                .data(posts)
                                .build();
        }

        @GetMapping("/user/{userId}")
        @Operation(summary = "Get posts by user", description = "Retrieves a paginated list of posts by a specific user.")
        public ApiResponse<PagedResponse<PostResponse>> getPostsByUser(
                        @Parameter(description = "User ID") @PathVariable Long userId,
                        @Parameter(description = "Page number (default is 0)") @RequestParam(defaultValue = "0") int page,
                        @Parameter(description = "Page size (default is 10)") @RequestParam(defaultValue = "10") int size) {
                Pageable pageable = PageRequest.of(page, size);
                PagedResponse<PostResponse> posts = postService.getPostsByAuthor(userId, pageable);
                return ApiResponse.<PagedResponse<PostResponse>>builder()
                                .code(200)
                                .message("Posts by user retrieved successfully")
                                .data(posts)
                                .build();
        }

        @GetMapping("/{id}")
        @Operation(summary = "Get post by ID", description = "Retrieves a specific post by its unique identifier.")
        public ApiResponse<PostResponse> getPostById(@Parameter(description = "Post ID") @PathVariable Long id) {
                PostResponse post = postService.getPostById(id);
                return ApiResponse.<PostResponse>builder()
                                .code(200)
                                .message("Post retrieved successfully")
                                .data(post)
                                .build();
        }

        @PostMapping
        @Operation(summary = "Create a new post", description = "Creates a new blog post. Requires authentication.")
        public ApiResponse<PostResponse> createPost(
                        @ModelAttribute PostRequest request,
                        @Parameter(hidden = true) Principal principal) {
                Long authorId = getCurrentUserId(principal);
                PostResponse createdPost = postService.createPost(request, authorId);
                return ApiResponse.<PostResponse>builder()
                                .code(201)
                                .message("Post created successfully")
                                .data(createdPost)
                                .build();
        }

        @PutMapping("/{id}")
        @Operation(summary = "Update post (Admin only)", description = "Updates an existing blog post. Requires ADMIN role.")
        public ApiResponse<PostResponse> updatePost(
                        @Parameter(description = "Post ID") @PathVariable Long id,
                        @ModelAttribute PostRequest request) {
                PostResponse updatedPost = postService.updatePost(id, request);
                return ApiResponse.<PostResponse>builder()
                                .code(200)
                                .message("Post updated successfully")
                                .data(updatedPost)
                                .build();
        }

        @DeleteMapping("/{id}")
        @Operation(summary = "Delete post (Admin only)", description = "Deletes a blog post. Requires ADMIN role.")
        public ApiResponse<Void> deletePost(@Parameter(description = "Post ID") @PathVariable Long id) {
                postService.deletePost(id);
                return ApiResponse.<Void>builder()
                                .code(200)
                                .message("Post deleted successfully")
                                .build();
        }

        @PostMapping("/{id}/views")
        @Operation(summary = "Increment view count", description = "Increments the view count for a specific post.")
        public ApiResponse<Void> incrementViewCount(@Parameter(description = "Post ID") @PathVariable Long id) {
                postService.incrementViewCount(id);
                return ApiResponse.<Void>builder()
                                .code(200)
                                .message("Post view count incremented successfully")
                                .build();
        }

        @PatchMapping("/{id}/status")
        @Operation(summary = "Update post status", description = "Updates the status of a specific post. Requires authentication.")
        public ApiResponse<PostResponse> updatePostStatus(
                        @Parameter(description = "Post ID") @PathVariable Long id,
                        @Parameter(description = "New status (DRAFT, PUBLISHED, ARCHIVED, PENDING_REVIEW)") @RequestParam String status) {
                PostResponse updatedPost = postService.updatePostStatus(id, status);
                return ApiResponse.<PostResponse>builder()
                                .code(200)
                                .message("Post status updated successfully")
                                .data(updatedPost)
                                .build();
        }

        private Long getCurrentUserId(Principal principal) {
                String username = principal.getName();
                User user = userRepository.findByUsername(username)
                                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
                return user.getId();
        }
}
