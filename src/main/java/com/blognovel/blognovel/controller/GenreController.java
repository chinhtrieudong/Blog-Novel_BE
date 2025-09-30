package com.blognovel.blognovel.controller;

import com.blognovel.blognovel.dto.request.GenreRequest;
import com.blognovel.blognovel.dto.response.ApiResponse;
import com.blognovel.blognovel.dto.response.GenreResponse;
import com.blognovel.blognovel.mapper.GenreMapper;
import com.blognovel.blognovel.model.Genre;
import com.blognovel.blognovel.repository.GenreRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/genres")
@RequiredArgsConstructor
@Tag(name = "Genres", description = "Endpoints for managing novel genres")
public class GenreController {

    private final GenreRepository genreRepository;
    private final GenreMapper genreMapper;

    @GetMapping
    @Operation(summary = "Get all genres", description = "Retrieves a list of all genres")
    public ApiResponse<List<GenreResponse>> getAllGenres() {
        List<GenreResponse> genres = genreRepository.findAll()
                .stream()
                .map(genreMapper::toResponse)
                .toList();

        return ApiResponse.<List<GenreResponse>>builder()
                .code(200)
                .message("Genres retrieved successfully")
                .data(genres)
                .build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get genre by ID", description = "Retrieves a specific genre by its unique identifier")
    public ApiResponse<GenreResponse> getGenreById(
            @Parameter(description = "Genre ID") @PathVariable Long id) {
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Genre not found with id: " + id));

        GenreResponse genreResponse = genreMapper.toResponse(genre);
        return ApiResponse.<GenreResponse>builder()
                .code(200)
                .message("Genre retrieved successfully")
                .data(genreResponse)
                .build();
    }

    @PostMapping
    @Operation(summary = "Create a new genre", description = "Creates a new genre")
    public ApiResponse<GenreResponse> createGenre(@RequestBody GenreRequest request) {
        Genre genre = Genre.builder()
                .name(request.getName())
                .slug(request.getSlug())
                .build();

        Genre savedGenre = genreRepository.save(genre);
        GenreResponse genreResponse = genreMapper.toResponse(savedGenre);

        return ApiResponse.<GenreResponse>builder()
                .code(201)
                .message("Genre created successfully")
                .data(genreResponse)
                .build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update genre", description = "Updates an existing genre")
    public ApiResponse<GenreResponse> updateGenre(
            @Parameter(description = "Genre ID") @PathVariable Long id,
            @RequestBody GenreRequest request) {

        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Genre not found with id: " + id));

        genre.setName(request.getName());
        genre.setSlug(request.getSlug());

        Genre updatedGenre = genreRepository.save(genre);
        GenreResponse genreResponse = genreMapper.toResponse(updatedGenre);

        return ApiResponse.<GenreResponse>builder()
                .code(200)
                .message("Genre updated successfully")
                .data(genreResponse)
                .build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete genre", description = "Deletes a genre")
    public ApiResponse<Void> deleteGenre(@Parameter(description = "Genre ID") @PathVariable Long id) {
        if (!genreRepository.existsById(id)) {
            throw new RuntimeException("Genre not found with id: " + id);
        }

        genreRepository.deleteById(id);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Genre deleted successfully")
                .build();
    }
}
