package com.blognovel.blognovel.controller;

import com.blognovel.blognovel.dto.request.PostRequest;
import com.blognovel.blognovel.dto.response.PagedResponse;
import com.blognovel.blognovel.dto.response.PostResponse;
import com.blognovel.blognovel.dto.response.CategoryResponse;
import com.blognovel.blognovel.dto.response.TagResponse;
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
import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private PostService postService;
    private UserRepository userRepository;

    @GetMapping
    public PagedResponse<PostResponse> getAllPosts(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long tagId,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Principal principal) {
        Long currentUserId = principal != null ? getCurrentUserId(principal) : null;
        Pageable pageable = PageRequest.of(page, size);
        return postService.getAllPosts(title, categoryId, tagId, status, pageable, currentUserId);
    }

    @GetMapping("/{id}")
    public PostResponse getPostById(@PathVariable Long id, Principal principal) {
        Long currentUserId = principal != null ? getCurrentUserId(principal) : null;
        return postService.getPostById(id, currentUserId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public PostResponse createPost(@RequestBody PostRequest request, Principal principal) {
        Long authorId = getCurrentUserId(principal);
        return postService.createPost(request, authorId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public PostResponse updatePost(@PathVariable Long id, @RequestBody PostRequest request) {
        return postService.updatePost(id, request);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void deletePost(@PathVariable Long id) {
        postService.deletePost(id);
    }

    @PostMapping("/{id}/like")
    public boolean likeOrUnlikePost(@PathVariable Long id, Principal principal) {
        Long userId = getCurrentUserId(principal);
        return postService.likeOrUnlikePost(id, userId);
    }

    @GetMapping("/categories")
    public List<CategoryResponse> getAllCategories() {
        return postService.getAllCategories();
    }

    @GetMapping("/tags")
    public List<TagResponse> getAllTags() {
        return postService.getAllTags();
    }

    @PostMapping("/{id}/views")
    public void incrementViewCount(@PathVariable Long id) {
        postService.incrementViewCount(id);
    }

    private Long getCurrentUserId(Principal principal) {
        String username = principal.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        return user.getId();
    }
}
