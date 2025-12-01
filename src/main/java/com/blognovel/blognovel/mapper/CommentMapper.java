package com.blognovel.blognovel.mapper;

import com.blognovel.blognovel.dto.response.CommentResponse;
import com.blognovel.blognovel.dto.response.UserResponse;
import com.blognovel.blognovel.model.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(target = "user", source = "user")
    @Mapping(target = "replies", expression = "java(new java.util.ArrayList<>())") // Empty list to avoid loops
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    @Mapping(target = "likeCount", source = "likeCount")
    @Mapping(target = "parentId", expression = "java(comment.getParent() != null ? comment.getParent().getId() : null)")
    CommentResponse toResponse(Comment comment);

    List<CommentResponse> toResponseList(List<Comment> comments);

    default UserResponse mapUser(com.blognovel.blognovel.model.User user) {
        if (user == null) {
            return null;
        }
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .bio(user.getBio())
                .avatarUrl(user.getAvatarUrl())
                .role(user.getRole().name())
                .status(user.getStatus().name())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .createdBy(user.getCreatedBy())
                .updatedBy(user.getUpdatedBy())
                .build();
    }
}
