package com.yatharth.finance_tracker.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardSummaryResponse {
    private Double totalIncome;
    private Double totalExpense;
    private Double balance;
}
