package com.yatharth.finance_tracker.service.category;

import com.yatharth.finance_tracker.dto.category.CategoryRequest;
import com.yatharth.finance_tracker.dto.category.CategoryResponse;
import com.yatharth.finance_tracker.dto.api.DeleteResponse;

import java.util.List;

public interface CategoryService {
    CategoryResponse createCategory(CategoryRequest categoryRequest);
    DeleteResponse deleteCategory(Long id);
    List<CategoryResponse> getAllCategories();
}
