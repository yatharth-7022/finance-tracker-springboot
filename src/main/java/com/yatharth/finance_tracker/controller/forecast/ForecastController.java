package com.yatharth.finance_tracker.controller.forecast;

import com.yatharth.finance_tracker.dto.ForecastResponse;
import com.yatharth.finance_tracker.dto.api.ApiResponse;
import com.yatharth.finance_tracker.entity.Transaction;
import com.yatharth.finance_tracker.repository.transaction.TransactionRepository;
import com.yatharth.finance_tracker.service.gemini.GeminiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/forecast")
public class ForecastController {
    private final GeminiService geminiService;
    private final TransactionRepository transactionRepository;

    @GetMapping("/monthly/{userId}")
    public ResponseEntity<ApiResponse<ForecastResponse>> getMonthlyForecast(@PathVariable Long userId) {
        List<Transaction> expenses = transactionRepository.findCurrentMonthExpenseByUser(userId);
        
        // Handle case when no expenses found
        if (expenses.isEmpty()) {
            ForecastResponse emptyResponse = ForecastResponse.builder()
                    .estimatedSpending(0.0)
                    .averageDailySpend(0.0)
                    .timeRange(ForecastResponse.TimeRange.builder()
                            .start(java.time.LocalDate.now().toString())
                            .end(java.time.LocalDate.now().toString())
                            .build())
                    .totalSpentSoFar(0.0)
                    .tipSummary(List.of(
                            "Start tracking your expenses to get personalized insights",
                            "Set up a monthly budget to control your spending",
                            "Record all transactions, even small ones"
                    ))
                    .warnings(List.of("No expense data found for current month"))
                    .generatedAt(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                    .build();
            
            return ResponseEntity.ok(new ApiResponse<>(200, "No expenses found for current month", emptyResponse));
        }
        
        // Get structured forecast from Gemini
        ForecastResponse forecast = geminiService.getExpenseForecast(expenses);
        return ResponseEntity.ok(new ApiResponse<>(200, "Expense forecast generated successfully", forecast));
    }
}