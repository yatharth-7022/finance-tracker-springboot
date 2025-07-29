package com.yatharth.finance_tracker.ApplicationEvents;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class BudgetExceededEvent extends ApplicationEvent {
    private final Long userId;
    private final Double budgetLimit;
    private final Double currentSpending;
    private final String month;
    private final Double exceededBy;
    private final String eventType = "BUDGET_EXCEEDED";

    public BudgetExceededEvent(Object source, Long userId, Double budgetLimit, Double currentSpending, String month, Double exceededBy) {
        super(source);
        this.userId = userId;
        this.budgetLimit = budgetLimit;
        this.currentSpending = currentSpending;
        this.month = month;
        this.exceededBy = exceededBy;
    }
    public Double getExceededPercentage(){
        return exceededBy/budgetLimit*100;
    };
    public boolean isExceeded(){
        return getExceededPercentage()>50;
    }

    public String getSeverityLevel() {
        double percentage = getExceededPercentage();
        if (percentage > 100) return "CRITICAL";
        if (percentage > 50) return "HIGH";
        if (percentage > 20) return "MEDIUM";
        return "LOW";
    }




}
