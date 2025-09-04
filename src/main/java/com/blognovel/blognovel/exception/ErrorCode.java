package com.blognovel.blognovel.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    USER_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "User already exists"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User not found"),
    INVALID_STATUS(HttpStatus.BAD_REQUEST, "Invalid status"),
    INVALID_ROLE(HttpStatus.BAD_REQUEST, "Invalid role"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "Invalid token"),
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "Post not found"),
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "Category not found"),
    TAG_NOT_FOUND(HttpStatus.NOT_FOUND, "Tag not found"),
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "Comment not found"),;

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}