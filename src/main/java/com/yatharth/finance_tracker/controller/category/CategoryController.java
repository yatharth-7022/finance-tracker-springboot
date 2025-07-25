package com.yatharth.finance_tracker.controller.category;

import com.yatharth.finance_tracker.dto.api.ApiResponse;
import com.yatharth.finance_tracker.dto.category.CategoryRequest;
import com.yatharth.finance_tracker.dto.category.CategoryResponse;
import com.yatharth.finance_tracker.dto.api.DeleteResponse;
import com.yatharth.finance_tracker.service.category.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/v1/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<CategoryResponse>> createCategory(@Valid @RequestBody CategoryRequest categoryRequest) {
        return ResponseEntity.ok(new ApiResponse<>(201, "Category created successfully", categoryService.createCategory(categoryRequest)));
    }
    @GetMapping()
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getAllCategories(){
        return ResponseEntity.ok(new ApiResponse<>(200, "Categories retrieved successfully", categoryService.getAllCategories()));
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<DeleteResponse> deleteCategory(@PathVariable Long id){
        return ResponseEntity.ok(categoryService.deleteCategory(id));
    }



}
