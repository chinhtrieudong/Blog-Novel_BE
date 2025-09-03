package com.blognovel.blognovel.service;

import com.blognovel.blognovel.dto.request.PostRequest;
import com.blognovel.blognovel.dto.response.PagedResponse;
import com.blognovel.blognovel.dto.response.PostResponse;
import com.blognovel.blognovel.dto.response.CategoryResponse;
import com.blognovel.blognovel.dto.response.TagResponse;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface PostService {
    PagedResponse<PostResponse> getAllPosts(String title, Long categoryId, Long tagId, String status, Pageable pageable,
            Long currentUserId);

    PostResponse getPostById(Long id, Long currentUserId);

    PostResponse createPost(PostRequest request, Long authorId);

    PostResponse updatePost(Long id, PostRequest request);

    void deletePost(Long id);

    boolean likeOrUnlikePost(Long postId, Long userId);

    void incrementViewCount(Long postId);

    List<CategoryResponse> getAllCategories();

    List<TagResponse> getAllTags();
}
