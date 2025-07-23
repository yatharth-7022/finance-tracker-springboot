package com.yatharth.finance_tracker.controller.budget;

import com.yatharth.finance_tracker.dto.api.ApiResponse;
import com.yatharth.finance_tracker.dto.budget.BudgetRequest;
import com.yatharth.finance_tracker.dto.budget.BudgetResponse;
import com.yatharth.finance_tracker.service.budget.BudgetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/v1/budget")
@RequiredArgsConstructor
public class BudgetController {
    private final BudgetService budgetService;

    @PostMapping
    public ResponseEntity<ApiResponse<BudgetResponse>> createOrUpdateBudget(@RequestBody BudgetRequest budgetRequest){
        BudgetResponse response = budgetService.createOrUpdateBudget(budgetRequest);
        return ResponseEntity.ok(new ApiResponse<>(200, "Budget created successfully", response));

    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<BudgetResponse>>> getBudgetOfCurrentUser(){
        return ResponseEntity.ok(new ApiResponse<>(200, "Budget retrieved successfully", budgetService.getBudgetOfCurrentUser()));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<String>> deleteBudget(@PathVariable Long id){
        budgetService.deleteBudgetByUserCategoryAndBudget(id);
        return ResponseEntity.ok(new ApiResponse<>(200, "Budget deleted successfully", null));
    }

}
