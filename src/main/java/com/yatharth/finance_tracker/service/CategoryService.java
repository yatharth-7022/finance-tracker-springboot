package com.yatharth.finance_tracker.service;

import com.yatharth.finance_tracker.dto.CategoryRequest;
import com.yatharth.finance_tracker.dto.CategoryResponse;
import com.yatharth.finance_tracker.dto.DeleteResponse;

import java.util.List;

public interface CategoryService {
    CategoryResponse createCategory(CategoryRequest categoryRequest);
    DeleteResponse deleteCategory(Long id);
    List<CategoryResponse> getAllCategories();
}
