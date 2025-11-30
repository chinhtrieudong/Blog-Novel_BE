package com.blognovel.blognovel.service;

import com.blognovel.blognovel.dto.request.CommentRequest;
import com.blognovel.blognovel.dto.response.CommentResponse;

import java.util.List;

public interface CommentService {
    List<CommentResponse> getCommentsForPost(Long postId, Long userId);

    List<CommentResponse> getCommentsForNovel(Long novelId, Long userId);

    List<CommentResponse> getCommentsForChapter(Long chapterId, Long userId);

    CommentResponse addCommentToPost(Long postId, CommentRequest request, Long userId);

    CommentResponse addCommentToNovel(Long novelId, CommentRequest request, Long userId);

    CommentResponse addCommentToChapter(Long chapterId, CommentRequest request, Long userId);

    CommentResponse updateComment(Long commentId, CommentRequest request, Long userId);

    void deleteComment(Long commentId, Long userId);

    void likeComment(Long commentId, Long userId);
}
