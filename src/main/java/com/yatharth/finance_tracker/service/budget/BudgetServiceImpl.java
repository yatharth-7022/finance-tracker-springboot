package com.yatharth.finance_tracker.service.budget;

import com.yatharth.finance_tracker.dto.budget.BudgetRequest;
import com.yatharth.finance_tracker.dto.budget.BudgetResponse;
import com.yatharth.finance_tracker.entity.Budget;
import com.yatharth.finance_tracker.entity.Category;
import com.yatharth.finance_tracker.entity.User;
import com.yatharth.finance_tracker.repository.budget.BudgetRepository;
import com.yatharth.finance_tracker.repository.category.CategoryRepository;
import com.yatharth.finance_tracker.repository.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BudgetServiceImpl implements BudgetService {
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final BudgetRepository budgetRepository;

    @Override
    public BudgetResponse createOrUpdateBudget(BudgetRequest budgetRequest) {
        User currentUser = getCurrentUser();
        Category category = categoryRepository.findById(budgetRequest.getCategoryId()).orElseThrow(()->new  EntityNotFoundException("Category not found"));
        Optional<Budget> existingBudget = budgetRepository.findByUserAndCategoryAndMonthAndYear(currentUser,category,budgetRequest.getMonth(),budgetRequest.getYear());
        Budget budget;
        if(existingBudget.isPresent()){
            budget = existingBudget.get();
            budget.setAmount(budgetRequest.getAmount());

        }
        else{
            budget = Budget.builder().user(currentUser).category(category).month(budgetRequest.getMonth()).year(budgetRequest.getYear()).amount(budgetRequest.getAmount()).build();
        }
        Budget savedBudget = budgetRepository.save(budget);
        return new BudgetResponse(savedBudget.getId(),savedBudget.getAmount(),savedBudget.getMonth(),savedBudget.getYear(),savedBudget.getCategory().getId(),savedBudget.getCategory().getName());

    }

    @Override
    public List<BudgetResponse> getBudgetOfCurrentUser() {
        User user = getCurrentUser();
        List<Budget> budgets = budgetRepository.findByUser(user);
        return budgets.stream().map(this::mapToResponse).toList();
    }


    @Override
    public void deleteBudgetByUserCategoryAndBudget( Long budgetId) {
        Budget budget = budgetRepository.findById(budgetId).orElseThrow(() -> new EntityNotFoundException("Budget not found"));
         User currentUser = getCurrentUser();
        if(!budget.getUser().getId().equals(currentUser.getId())){
            throw new SecurityException("You are not authorized to delete this budget");
        }
        budgetRepository.delete(budget);


    }


    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User Not Found"));

    }
    private BudgetResponse mapToResponse(Budget budget) {
        return BudgetResponse.builder()
                .id(budget.getId())
                .amount(budget.getAmount())
                .categoryId(budget.getCategory().getId())
                .categoryName(budget.getCategory().getName())
                .month(budget.getMonth())
                .year(budget.getYear())
                .build();
    }

}
