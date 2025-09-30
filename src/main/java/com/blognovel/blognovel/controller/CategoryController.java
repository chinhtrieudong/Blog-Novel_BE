package com.blognovel.blognovel.controller;

import com.blognovel.blognovel.dto.request.CategoryRequest;
import com.blognovel.blognovel.dto.response.ApiResponse;
import com.blognovel.blognovel.dto.response.CategoryResponse;
import com.blognovel.blognovel.mapper.CategoryMapper;
import com.blognovel.blognovel.model.Category;
import com.blognovel.blognovel.repository.CategoryRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Tag(name = "Categories", description = "Endpoints for managing categories")
public class CategoryController {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @GetMapping
    @Operation(summary = "Get all categories", description = "Retrieves a list of all categories")
    public ApiResponse<List<CategoryResponse>> getAllCategories() {
        List<CategoryResponse> categories = categoryRepository.findAll()
                .stream()
                .map(categoryMapper::toResponse)
                .toList();

        return ApiResponse.<List<CategoryResponse>>builder()
                .code(200)
                .message("Categories retrieved successfully")
                .data(categories)
                .build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get category by ID", description = "Retrieves a specific category by its unique identifier")
    public ApiResponse<CategoryResponse> getCategoryById(
            @Parameter(description = "Category ID") @PathVariable Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));

        CategoryResponse categoryResponse = categoryMapper.toResponse(category);
        return ApiResponse.<CategoryResponse>builder()
                .code(200)
                .message("Category retrieved successfully")
                .data(categoryResponse)
                .build();
    }

    @PostMapping
    @Operation(summary = "Create a new category", description = "Creates a new category")
    public ApiResponse<CategoryResponse> createCategory(@RequestBody CategoryRequest request) {
        Category category = Category.builder()
                .name(request.getName())
                .build();

        Category savedCategory = categoryRepository.save(category);
        CategoryResponse categoryResponse = categoryMapper.toResponse(savedCategory);

        return ApiResponse.<CategoryResponse>builder()
                .code(201)
                .message("Category created successfully")
                .data(categoryResponse)
                .build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update category", description = "Updates an existing category")
    public ApiResponse<CategoryResponse> updateCategory(
            @Parameter(description = "Category ID") @PathVariable Long id,
            @RequestBody CategoryRequest request) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));

        category.setName(request.getName());

        Category updatedCategory = categoryRepository.save(category);
        CategoryResponse categoryResponse = categoryMapper.toResponse(updatedCategory);

        return ApiResponse.<CategoryResponse>builder()
                .code(200)
                .message("Category updated successfully")
                .data(categoryResponse)
                .build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete category", description = "Deletes a category")
    public ApiResponse<Void> deleteCategory(@Parameter(description = "Category ID") @PathVariable Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new RuntimeException("Category not found with id: " + id);
        }

        categoryRepository.deleteById(id);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Category deleted successfully")
                .build();
    }
}
