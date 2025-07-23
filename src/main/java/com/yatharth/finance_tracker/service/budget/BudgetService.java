package com.yatharth.finance_tracker.service.budget;


import com.yatharth.finance_tracker.dto.budget.BudgetRequest;
import com.yatharth.finance_tracker.dto.budget.BudgetResponse;
import com.yatharth.finance_tracker.entity.Category;
import com.yatharth.finance_tracker.entity.User;

import java.util.List;

public interface BudgetService {
    BudgetResponse createOrUpdateBudget(BudgetRequest budgetRequest);
    List<BudgetResponse> getBudgetOfCurrentUser();
    void deleteBudgetByUserCategoryAndBudget(Long budgetId);


}
