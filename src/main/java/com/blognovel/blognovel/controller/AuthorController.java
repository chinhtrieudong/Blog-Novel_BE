package com.blognovel.blognovel.controller;

import com.blognovel.blognovel.dto.request.AuthorRequest;
import com.blognovel.blognovel.dto.response.ApiResponse;
import com.blognovel.blognovel.dto.response.AuthorResponse;
import com.blognovel.blognovel.service.AuthorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/authors")
@RequiredArgsConstructor
@Tag(name = "Authors", description = "Endpoints for managing authors")
public class AuthorController {

    private final AuthorService authorService;

    @PostMapping
    @Operation(summary = "Create a new author", description = "Creates a new author with the given details.")
    public ApiResponse<AuthorResponse> createAuthor(@ModelAttribute AuthorRequest request) {
        return ApiResponse.<AuthorResponse>builder()
                .code(201)
                .message("Author created successfully")
                .data(authorService.createAuthor(request))
                .build();
    }

    @GetMapping
    @Operation(summary = "Get all authors", description = "Retrieves a list of all authors.")
    public ApiResponse<List<AuthorResponse>> getAllAuthors() {
        return ApiResponse.<List<AuthorResponse>>builder()
                .code(200)
                .message("All authors retrieved successfully")
                .data(authorService.getAllAuthors())
                .build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get author by ID", description = "Retrieves a specific author by its unique identifier.")
    public ApiResponse<AuthorResponse> getAuthorById(@Parameter(description = "Author ID") @PathVariable Long id) {
        return ApiResponse.<AuthorResponse>builder()
                .code(200)
                .message("Author retrieved successfully")
                .data(authorService.getAuthorResponseById(id))
                .build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update author", description = "Updates an existing author.")
    public ApiResponse<AuthorResponse> updateAuthor(
            @Parameter(description = "Author ID") @PathVariable Long id,
            @ModelAttribute AuthorRequest request) {
        return ApiResponse.<AuthorResponse>builder()
                .code(200)
                .message("Author updated successfully")
                .data(authorService.updateAuthor(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete author", description = "Deletes an author.")
    public ApiResponse<Void> deleteAuthor(@Parameter(description = "Author ID") @PathVariable Long id) {
        authorService.deleteAuthor(id);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Author deleted successfully")
                .build();
    }
}
