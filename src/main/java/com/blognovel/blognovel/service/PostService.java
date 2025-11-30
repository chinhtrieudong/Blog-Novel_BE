package com.blognovel.blognovel.service;

import com.blognovel.blognovel.dto.request.PostRequest;
import com.blognovel.blognovel.dto.response.PagedResponse;
import com.blognovel.blognovel.dto.response.PostResponse;
import org.springframework.data.domain.Pageable;

public interface PostService {
    PagedResponse<PostResponse> getAllPosts(String title, Long categoryId, Long tagId, String status,
            Pageable pageable);

    PagedResponse<PostResponse> getPostsByAuthor(Long authorId, Pageable pageable);

    PostResponse getPostById(Long id);

    PostResponse createPost(PostRequest request, Long currentUserId);

    PostResponse updatePost(Long id, PostRequest request);

    void deletePost(Long id);

    void incrementViewCount(Long postId);

    PostResponse updatePostStatus(Long id, String status);

    void savePost(Long postId, Long userId);

    PagedResponse<PostResponse> getSavedPostsByUser(Long userId, Pageable pageable);
}
