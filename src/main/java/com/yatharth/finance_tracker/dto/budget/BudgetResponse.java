package com.yatharth.finance_tracker.dto.budget;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class BudgetResponse {
    private Long id;
    private double amount;
    private int month;
    private int year;
    private Long categoryId;
    private String categoryName;
}
