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
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final UserRepository userRepository;

    @GetMapping
    public ApiResponse<PagedResponse<PostResponse>> getAllPosts(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long tagId,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        PagedResponse<PostResponse> posts = postService.getAllPosts(title, categoryId, tagId, status, pageable);
        return ApiResponse.<PagedResponse<PostResponse>>builder()
                .code(200)
                .message("All posts retrieved successfully")
                .data(posts)
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<PostResponse> getPostById(@PathVariable Long id) {
        PostResponse post = postService.getPostById(id);
        return ApiResponse.<PostResponse>builder()
                .code(200)
                .message("Post retrieved successfully")
                .data(post)
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ApiResponse<PostResponse> createPost(@RequestBody PostRequest request, Principal principal) {
        Long authorId = getCurrentUserId(principal);
        PostResponse createdPost = postService.createPost(request, authorId);
        return ApiResponse.<PostResponse>builder()
                .code(201)
                .message("Post created successfully")
                .data(createdPost)
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ApiResponse<PostResponse> updatePost(@PathVariable Long id, @RequestBody PostRequest request) {
        PostResponse updatedPost = postService.updatePost(id, request);
        return ApiResponse.<PostResponse>builder()
                .code(200)
                .message("Post updated successfully")
                .data(updatedPost)
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Post deleted successfully")
                .build();
    }

    @PostMapping("/{id}/views")
    public ApiResponse<Void> incrementViewCount(@PathVariable Long id) {
        postService.incrementViewCount(id);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Post view count incremented successfully")
                .build();
    }

    private Long getCurrentUserId(Principal principal) {
        String username = principal.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        return user.getId();
    }
}