package com.blognovel.blognovel.service.impl;

import com.blognovel.blognovel.dto.request.CommentRequest;
import com.blognovel.blognovel.dto.response.CommentResponse;
import com.blognovel.blognovel.exception.AppException;
import com.blognovel.blognovel.exception.ErrorCode;
import com.blognovel.blognovel.mapper.CommentMapper;
import com.blognovel.blognovel.model.Comment;
import com.blognovel.blognovel.model.Novel;
import com.blognovel.blognovel.model.Post;
import com.blognovel.blognovel.model.User;
import com.blognovel.blognovel.repository.CommentRepository;
import com.blognovel.blognovel.repository.NovelRepository;
import com.blognovel.blognovel.repository.PostRepository;
import com.blognovel.blognovel.repository.UserRepository;
import com.blognovel.blognovel.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final NovelRepository novelRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;

    @Override
    public List<CommentResponse> getCommentsForPost(Long postId) {
        List<Comment> comments = commentRepository.findByPostIdAndParentIsNullOrderByCreatedAtAsc(postId);
        return commentMapper.toResponseList(comments);
    }

    @Override
    public List<CommentResponse> getCommentsForNovel(Long novelId) {
        List<Comment> comments = commentRepository.findByNovelIdAndParentIsNullOrderByCreatedAtAsc(novelId);
        return commentMapper.toResponseList(comments);
    }

    @Override
    @Transactional
    public CommentResponse addCommentToPost(Long postId, CommentRequest request, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Comment comment = Comment.builder()
                .content(request.getContent())
                .user(user)
                .post(post)
                .build();

        if (request.getParentId() != null) {
            Comment parent = commentRepository.findById(request.getParentId())
                    .orElseThrow(() -> new AppException(ErrorCode.COMMENT_NOT_FOUND));
            comment.setParent(parent);
        }

        Comment saved = commentRepository.save(comment);
        return commentMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public CommentResponse addCommentToNovel(Long novelId, CommentRequest request, Long userId) {
        Novel novel = novelRepository.findById(novelId)
                .orElseThrow(() -> new AppException(ErrorCode.NOVEL_NOT_FOUND));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Comment comment = Comment.builder()
                .content(request.getContent())
                .user(user)
                .novel(novel)
                .build();

        if (request.getParentId() != null) {
            Comment parent = commentRepository.findById(request.getParentId())
                    .orElseThrow(() -> new AppException(ErrorCode.COMMENT_NOT_FOUND));
            comment.setParent(parent);
        }

        Comment saved = commentRepository.save(comment);
        return commentMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public CommentResponse updateComment(Long commentId, CommentRequest request, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new AppException(ErrorCode.COMMENT_NOT_FOUND));

        if (!comment.getUser().getId().equals(userId)) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        comment.setContent(request.getContent());
        Comment updated = commentRepository.save(comment);
        return commentMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public void deleteComment(Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new AppException(ErrorCode.COMMENT_NOT_FOUND));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        if (!comment.getUser().getId().equals(userId) && !user.getRole().name().equals("ADMIN")) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        commentRepository.delete(comment);
    }

    @Override
    @Transactional
    public void likeComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new AppException(ErrorCode.COMMENT_NOT_FOUND));

        commentRepository.incrementLikeCount(commentId);
    }
}
