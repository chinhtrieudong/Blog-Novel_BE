package com.blognovel.blognovel.service;

import com.blognovel.blognovel.dto.request.CommentRequest;
import com.blognovel.blognovel.dto.response.CommentResponse;

import java.util.List;

public interface CommentService {
    List<CommentResponse> getCommentsForPost(Long postId);

    List<CommentResponse> getCommentsForNovel(Long novelId);

    CommentResponse addCommentToPost(Long postId, CommentRequest request, Long userId);

    CommentResponse addCommentToNovel(Long novelId, CommentRequest request, Long userId);

    CommentResponse updateComment(Long commentId, CommentRequest request, Long userId);

    void deleteComment(Long commentId, Long userId);

    void likeComment(Long commentId);
}
