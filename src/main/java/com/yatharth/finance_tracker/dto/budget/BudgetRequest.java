package com.yatharth.finance_tracker.dto.budget;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder

public class BudgetRequest {
    @NotNull(message = "Amount is required")
    @Min(value = 0, message = "Amount must be positive")
    private double amount;
    @NotNull(message = "Month is required")
    private int month;
    @NotNull(message = "Year is required")
    private int year;
    @NotNull(message = "Category is required")
    private Long categoryId;
}
