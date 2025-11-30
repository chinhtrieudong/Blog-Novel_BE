package com.blognovel.blognovel.service.impl;

import com.blognovel.blognovel.dto.request.CommentRequest;
import com.blognovel.blognovel.dto.response.CommentResponse;
import com.blognovel.blognovel.exception.AppException;
import com.blognovel.blognovel.exception.ErrorCode;
import com.blognovel.blognovel.mapper.CommentMapper;
import com.blognovel.blognovel.model.Chapter;
import com.blognovel.blognovel.model.Comment;
import com.blognovel.blognovel.model.CommentLike;
import com.blognovel.blognovel.model.Novel;
import com.blognovel.blognovel.model.Post;
import com.blognovel.blognovel.model.User;
import com.blognovel.blognovel.repository.ChapterRepository;
import com.blognovel.blognovel.repository.CommentLikeRepository;
import com.blognovel.blognovel.repository.CommentRepository;
import com.blognovel.blognovel.repository.NovelRepository;
import com.blognovel.blognovel.repository.PostRepository;
import com.blognovel.blognovel.repository.UserRepository;
import com.blognovel.blognovel.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

        private final CommentRepository commentRepository;
        private final PostRepository postRepository;
        private final NovelRepository novelRepository;
        private final ChapterRepository chapterRepository;
        private final UserRepository userRepository;
        private final CommentMapper commentMapper;
        private final CommentLikeRepository commentLikeRepository;

        @Override
        public List<CommentResponse> getCommentsForPost(Long postId, Long userId) {
                List<Comment> comments = commentRepository.findByPostIdAndParentIsNullOrderByCreatedAtAsc(postId);
                List<CommentResponse> responses = commentMapper.toResponseList(comments);
                setLikeStatuses(responses, userId);
                return responses;
        }

        @Override
        public List<CommentResponse> getCommentsForNovel(Long novelId, Long userId) {
                List<Comment> comments = commentRepository.findByNovelIdAndParentIsNullOrderByCreatedAtAsc(novelId);
                List<CommentResponse> responses = commentMapper.toResponseList(comments);
                setLikeStatuses(responses, userId);
                return responses;
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
        public List<CommentResponse> getCommentsForChapter(Long chapterId, Long userId) {
                List<Comment> comments = commentRepository.findByChapterIdAndParentIsNullOrderByCreatedAtAsc(chapterId);
                List<CommentResponse> responses = commentMapper.toResponseList(comments);
                setLikeStatuses(responses, userId);
                return responses;
        }

        @Override
        @Transactional
        public CommentResponse addCommentToChapter(Long chapterId, CommentRequest request, Long userId) {
                Chapter chapter = chapterRepository.findById(chapterId)
                                .orElseThrow(() -> new AppException(ErrorCode.CHAPTER_NOT_FOUND));
                User user = userRepository.findById(userId)
                                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

                Comment comment = Comment.builder()
                                .content(request.getContent())
                                .user(user)
                                .chapter(chapter)
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
        public void likeComment(Long commentId, Long userId) {
                Comment comment = commentRepository.findById(commentId)
                                .orElseThrow(() -> new AppException(ErrorCode.COMMENT_NOT_FOUND));
                User user = userRepository.findById(userId)
                                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

                Optional<CommentLike> existingLike = commentLikeRepository.findByCommentIdAndUserId(commentId, userId);

                if (existingLike.isPresent()) {
                        // Unlike: remove the like
                        commentLikeRepository.delete(existingLike.get());
                        comment.setLikeCount(comment.getLikeCount() - 1);
                } else {
                        // Like: create new like
                        CommentLike like = CommentLike.builder()
                                        .comment(comment)
                                        .user(user)
                                        .build();
                        commentLikeRepository.save(like);
                        comment.setLikeCount(comment.getLikeCount() + 1);
                }

                commentRepository.save(comment);
        }

        private void setLikeStatuses(List<CommentResponse> responses, Long userId) {
                for (CommentResponse response : responses) {
                        boolean isLiked = userId != null
                                        && commentLikeRepository.findByCommentIdAndUserId(response.getId(), userId)
                                                        .isPresent();
                        response.setIsLiked(isLiked);

                        // Recursively set for replies
                        if (response.getReplies() != null && !response.getReplies().isEmpty()) {
                                setLikeStatuses(response.getReplies(), userId);
                        }
                }
        }
}
