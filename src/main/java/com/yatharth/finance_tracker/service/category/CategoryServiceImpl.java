package com.yatharth.finance_tracker.service.category;

import com.yatharth.finance_tracker.dto.category.CategoryRequest;
import com.yatharth.finance_tracker.dto.category.CategoryResponse;
import com.yatharth.finance_tracker.dto.api.DeleteResponse;
import com.yatharth.finance_tracker.entity.Category;
import com.yatharth.finance_tracker.entity.User;
import com.yatharth.finance_tracker.repository.category.CategoryRepository;
import com.yatharth.finance_tracker.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Override
    public CategoryResponse createCategory(CategoryRequest categoryRequest) {
        User user = getCurrentUser();
        Category category = Category.builder().name(categoryRequest.getName()).type(categoryRequest.getType()).user(user).build();
        Category savedCategory = categoryRepository.save(category);
        return new CategoryResponse(savedCategory.getId(), savedCategory.getName(), savedCategory.getType());
    }

    @Override
    public DeleteResponse deleteCategory(Long id) {
        User user = getCurrentUser();
        Category category = categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Category Not Found"));
        if (!category.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You are not authorized  to delete this category");
        }
        categoryRepository.deleteById(id);
        return new DeleteResponse(200, "Category deleted successfully");
    }

    @Override

    public List<CategoryResponse> getAllCategories() {
        User user = getCurrentUser();
        return categoryRepository.findByUser(user).stream().map(category ->
                new CategoryResponse(category.getId(), category.getName(), category.getType())).collect(Collectors.toList());
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User Not Found"));

    }

}
