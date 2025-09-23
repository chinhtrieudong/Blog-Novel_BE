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
                        @Parameter(description = "Post ID") @PathVariable Long postId) {
                List<CommentResponse> comments = commentService.getCommentsForPost(postId);
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
                        @Parameter(description = "Novel ID") @PathVariable Long novelId) {
                List<CommentResponse> comments = commentService.getCommentsForNovel(novelId);
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
        public ApiResponse<Void> likeComment(@Parameter(description = "Comment ID") @PathVariable Long commentId) {
                commentService.likeComment(commentId);
                return ApiResponse.<Void>builder()
                                .code(200)
                                .message("Comment liked successfully")
                                .build();
        }
}
