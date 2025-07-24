package com.yatharth.finance_tracker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ForecastResponse {
    private double estimatedSpending;
    private double averageDailySpend;
    private TimeRange timeRange;
    private double totalSpentSoFar;
    private List<String> tipSummary;
    private List<String> warnings;
    private String generatedAt;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TimeRange {
        private String start;
        private String end;
    }
}