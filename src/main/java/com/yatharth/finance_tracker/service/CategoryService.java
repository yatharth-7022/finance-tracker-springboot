package com.yatharth.finance_tracker.service;

import com.yatharth.finance_tracker.dto.CategoryRequest;
import com.yatharth.finance_tracker.dto.CategoryResponse;
import com.yatharth.finance_tracker.dto.DeleteResponse;
import com.yatharth.finance_tracker.entity.Category;
import com.yatharth.finance_tracker.entity.User;
import com.yatharth.finance_tracker.repository.CategoryRepository;
import com.yatharth.finance_tracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public CategoryResponse createCategory(CategoryRequest categoryRequest) {
        User user = getCurrentUser();
        Category category = Category.builder().name(categoryRequest.getName()).type(categoryRequest.getType()).user(user).build();
        Category savedCategory = categoryRepository.save(category);
        return new CategoryResponse(savedCategory.getId(), savedCategory.getName(), savedCategory.getType());
    }

    public DeleteResponse deleteCategory(Long id) {
        User user = getCurrentUser();
        Category category = categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Category Not Found"));
        if (!category.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You are not authorized  to delete this category");
        }
        categoryRepository.deleteById(id);
        return new DeleteResponse(200, "Category deleted successfully");
    }

    public List<CategoryResponse> getAllCategories() {
        User user = getCurrentUser();
        return categoryRepository.findByUser(user).stream().map(category ->
                new CategoryResponse(category.getId(),category.getName(),category.getType())).collect(Collectors.toList());
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User Not Found"));

    }

}
