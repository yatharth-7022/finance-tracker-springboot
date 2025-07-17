package com.yatharth.finance_tracker.service;

import com.yatharth.finance_tracker.dto.CategoryRequest;
import com.yatharth.finance_tracker.entity.Category;
import com.yatharth.finance_tracker.entity.User;
import com.yatharth.finance_tracker.repository.CategoryRepository;
import com.yatharth.finance_tracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public Category createCategory(CategoryRequest categoryRequest) {
        User user = getCurrentUser();
        Category category = Category.builder().name(categoryRequest.getName()).type(categoryRequest.getType()).user(user).build();
        return categoryRepository.save(category);
    }

    public void deleteCategory(Long id) {
        User user = getCurrentUser();
        Category category = categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Category Not Found"));
        if (!category.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You are not authorized  to delete this category");
        }
        categoryRepository.deleteById(id);
    }

    public List<Category> getAllCategories() {
        User user = getCurrentUser();
        return categoryRepository.findByUser(user);
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User Not Found"));

    }

}
