package com.blognovel.blognovel.controller;

import com.blognovel.blognovel.dto.request.ReadingProgressRequest;
import com.blognovel.blognovel.dto.response.ApiResponse;
import com.blognovel.blognovel.service.NovelService;
import com.blognovel.blognovel.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/saved-novels")
@RequiredArgsConstructor
@Tag(name = "Saved Novels", description = "Endpoints for managing saved novels and reading progress")
public class SavedNovelController {

    private final NovelService novelService;
    private final UserService userService;

    @PatchMapping("/{id}/progress")
    @Operation(summary = "Update reading progress", description = "Updates the reading progress for a saved novel.")
    public ApiResponse<Void> updateReadingProgress(
            @Parameter(description = "Novel ID") @PathVariable Long id,
            @Valid @RequestBody ReadingProgressRequest request,
            Principal principal) {
        Long userId = userService.getCurrentUser(principal.getName()).getId();
        novelService.updateReadingProgress(id, userId, request);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Reading progress updated successfully")
                .build();
    }
}
