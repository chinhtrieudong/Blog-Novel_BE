package com.blognovel.blognovel.controller;

import com.blognovel.blognovel.dto.request.ChapterRequest;
import com.blognovel.blognovel.dto.response.ApiResponse;
import com.blognovel.blognovel.dto.response.ChapterResponse;
import com.blognovel.blognovel.service.ChapterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/novels/{novelId}/chapters")
@RequiredArgsConstructor
public class ChapterController {

    private final ChapterService chapterService;

    @GetMapping
    @Operation(summary = "Get all chapters for a novel")
    public ApiResponse<List<ChapterResponse>> getAllChapters(
            @Parameter(description = "Novel ID") @PathVariable Long novelId) {
        List<ChapterResponse> chapters = chapterService.getAllChaptersByNovelId(novelId);
        return ApiResponse.<List<ChapterResponse>>builder()
                .code(200)
                .message("Chapters retrieved successfully")
                .data(chapters)
                .build();
    }

    @GetMapping("/{chapterId}")
    @Operation(summary = "Get a single chapter")
    public ApiResponse<ChapterResponse> getChapter(
            @Parameter(description = "Novel ID") @PathVariable Long novelId,
            @Parameter(description = "Chapter ID") @PathVariable Long chapterId) {
        ChapterResponse chapter = chapterService.getChapterById(novelId, chapterId);
        return ApiResponse.<ChapterResponse>builder()
                .code(200)
                .message("Chapter retrieved successfully")
                .data(chapter)
                .build();
    }

    @PostMapping
    @Operation(summary = "Create a new chapter (Admin only)")
    @ResponseStatus(HttpStatus.CREATED) // Use ResponseStatus to set the status code
    public ApiResponse<ChapterResponse> createChapter(
            @Parameter(description = "Novel ID") @PathVariable Long novelId,
            @Valid @RequestBody ChapterRequest chapterRequest) {
        // TODO: Add Admin Role check here
        ChapterResponse chapter = chapterService.createChapter(novelId, chapterRequest);
        return ApiResponse.<ChapterResponse>builder()
                .code(201) // Code is already implied by ResponseStatus, but keep it for consistency
                .message("Chapter created successfully")
                .data(chapter)
                .build();
    }

    @PutMapping("/{chapterId}")
    @Operation(summary = "Update a chapter (Admin only)")
    public ApiResponse<ChapterResponse> updateChapter(
            @Parameter(description = "Novel ID") @PathVariable Long novelId,
            @Parameter(description = "Chapter ID") @PathVariable Long chapterId,
            @Valid @RequestBody ChapterRequest chapterRequest) {
        // TODO: Add Admin Role check here
        ChapterResponse chapter = chapterService.updateChapter(novelId, chapterId, chapterRequest);
        return ApiResponse.<ChapterResponse>builder()
                .code(200)
                .message("Chapter updated successfully")
                .data(chapter)
                .build();
    }

    @DeleteMapping("/{chapterId}")
    @Operation(summary = "Delete a chapter (Admin only)")
    public ApiResponse<Void> deleteChapter(
            @Parameter(description = "Novel ID") @PathVariable Long novelId,
            @Parameter(description = "Chapter ID") @PathVariable Long chapterId) {
        // TODO: Add Admin Role check here
        chapterService.deleteChapter(novelId, chapterId);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Chapter deleted successfully")
                .build();
    }

    @PostMapping("/{chapterId}/views")
    @Operation(summary = "Increment chapter views")
    public ApiResponse<Void> incrementChapterViews(
            @Parameter(description = "Novel ID") @PathVariable Long novelId,
            @Parameter(description = "Chapter ID") @PathVariable Long chapterId) {
        chapterService.incrementViewCount(novelId, chapterId);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Chapter views incremented")
                .build();
    }
}