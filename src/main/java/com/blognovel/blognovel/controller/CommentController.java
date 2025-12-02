package com.blognovel.blognovel.controller;

import com.blognovel.blognovel.dto.request.CommentRequest;
import com.blognovel.blognovel.dto.response.ApiResponse;
import com.blognovel.blognovel.dto.response.CommentResponse;
import com.blognovel.blognovel.service.CommentService;
import com.blognovel.blognovel.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Comments", description = "Endpoints for managing comments on posts and novels")
public class CommentController {

        private final CommentService commentService;
        private final UserService userService;

        @GetMapping("/posts/{postId}/comments")
        @Operation(summary = "Get comments for a post", description = "Retrieves all top-level comments for a specific post.")
        public ApiResponse<List<CommentResponse>> getCommentsForPost(
                        @Parameter(description = "Post ID") @PathVariable Long postId,
                        Principal principal) {
                Long userId = principal != null ? userService.getCurrentUser(principal.getName()).getId() : null;
                List<CommentResponse> comments = commentService.getCommentsForPost(postId, userId);
                return ApiResponse.<List<CommentResponse>>builder()
                                .code(200)
                                .message("Comments retrieved successfully")
                                .data(comments)
                                .build();
        }

        @PostMapping("/posts/{postId}/comments")
        @Operation(summary = "Add comment to a post", description = "Adds a new comment to a specific post.")
        public ApiResponse<CommentResponse> addCommentToPost(
                        @Parameter(description = "Post ID") @PathVariable Long postId,
                        @Valid @RequestBody CommentRequest request,
                        Principal principal) {
                Long userId = userService.getCurrentUser(principal.getName()).getId();
                CommentResponse comment = commentService.addCommentToPost(postId, request, userId);
                return ApiResponse.<CommentResponse>builder()
                                .code(201)
                                .message("Comment added successfully")
                                .data(comment)
                                .build();
        }

        @GetMapping("/novels/{novelId}/comments")
        @Operation(summary = "Get comments for a novel", description = "Retrieves all top-level comments for a specific novel.")
        public ApiResponse<List<CommentResponse>> getCommentsForNovel(
                        @Parameter(description = "Novel ID") @PathVariable Long novelId,
                        Principal principal) {
                Long userId = principal != null ? userService.getCurrentUser(principal.getName()).getId() : null;
                List<CommentResponse> comments = commentService.getCommentsForNovel(novelId, userId);
                return ApiResponse.<List<CommentResponse>>builder()
                                .code(200)
                                .message("Comments retrieved successfully")
                                .data(comments)
                                .build();
        }

        @PostMapping("/novels/{novelId}/comments")
        @Operation(summary = "Add comment to a novel", description = "Adds a new comment to a specific novel.")
        public ApiResponse<CommentResponse> addCommentToNovel(
                        @Parameter(description = "Novel ID") @PathVariable Long novelId,
                        @Valid @RequestBody CommentRequest request,
                        Principal principal) {
                Long userId = userService.getCurrentUser(principal.getName()).getId();
                CommentResponse comment = commentService.addCommentToNovel(novelId, request, userId);
                return ApiResponse.<CommentResponse>builder()
                                .code(201)
                                .message("Comment added successfully")
                                .data(comment)
                                .build();
        }

        @GetMapping("/novels/{novelId}/chapters/{chapterId}/comments")
        @Operation(summary = "Get comments for a chapter", description = "Retrieves all top-level comments for a specific chapter.")
        public ApiResponse<List<CommentResponse>> getCommentsForChapter(
                        @Parameter(description = "Novel ID") @PathVariable Long novelId,
                        @Parameter(description = "Chapter ID") @PathVariable Long chapterId,
                        Principal principal) {
                Long userId = principal != null ? userService.getCurrentUser(principal.getName()).getId() : null;
                List<CommentResponse> comments = commentService.getCommentsForChapter(chapterId, userId);
                return ApiResponse.<List<CommentResponse>>builder()
                                .code(200)
                                .message("Chapter comments retrieved successfully")
                                .data(comments)
                                .build();
        }

        @PostMapping("/novels/{novelId}/chapters/{chapterId}/comments")
        @Operation(summary = "Add comment to a chapter", description = "Adds a new comment to a specific chapter.")
        public ApiResponse<CommentResponse> addCommentToChapter(
                        @Parameter(description = "Novel ID") @PathVariable Long novelId,
                        @Parameter(description = "Chapter ID") @PathVariable Long chapterId,
                        @Valid @RequestBody CommentRequest request,
                        Principal principal) {
                Long userId = userService.getCurrentUser(principal.getName()).getId();
                CommentResponse comment = commentService.addCommentToChapter(chapterId, request, userId);
                return ApiResponse.<CommentResponse>builder()
                                .code(201)
                                .message("Chapter comment added successfully")
                                .data(comment)
                                .build();
        }

        @PutMapping("/comments/{commentId}")
        @Operation(summary = "Update comment", description = "Updates a comment. Only the author can update their comment.")
        public ApiResponse<CommentResponse> updateComment(
                        @Parameter(description = "Comment ID") @PathVariable Long commentId,
                        @Valid @RequestBody CommentRequest request,
                        Principal principal) {
                Long userId = userService.getCurrentUser(principal.getName()).getId();
                CommentResponse comment = commentService.updateComment(commentId, request, userId);
                return ApiResponse.<CommentResponse>builder()
                                .code(200)
                                .message("Comment updated successfully")
                                .data(comment)
                                .build();
        }

        @DeleteMapping("/comments/{commentId}")
        @Operation(summary = "Delete comment", description = "Deletes a comment. Only the author or admin can delete.")
        public ApiResponse<Void> deleteComment(
                        @Parameter(description = "Comment ID") @PathVariable Long commentId,
                        Principal principal) {
                Long userId = userService.getCurrentUser(principal.getName()).getId();
                commentService.deleteComment(commentId, userId);
                return ApiResponse.<Void>builder()
                                .code(200)
                                .message("Comment deleted successfully")
                                .build();
        }

        @PostMapping("/comments/{commentId}/like")
        @Operation(summary = "Like/unlike a comment", description = "Likes a comment.")
        public ApiResponse<Void> likeComment(@Parameter(description = "Comment ID") @PathVariable Long commentId,
                        Principal principal) {
                Long userId = userService.getCurrentUser(principal.getName()).getId();
                commentService.likeComment(commentId, userId);
                return ApiResponse.<Void>builder()
                                .code(200)
                                .message("Comment liked successfully")
                                .build();
        }
}
