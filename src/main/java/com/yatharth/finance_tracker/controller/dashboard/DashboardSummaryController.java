package com.yatharth.finance_tracker.controller.dashboard;

import com.yatharth.finance_tracker.dto.api.ApiResponse;
import com.yatharth.finance_tracker.dto.dashboard.DashboardSummaryResponse;
import com.yatharth.finance_tracker.service.dashboard.DashboardSummaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor
public class DashboardSummaryController {
    private final DashboardSummaryService dashboardSummaryService;

    @GetMapping("/summary")
   public ResponseEntity<ApiResponse<DashboardSummaryResponse>> getDashboardSummary(){
        DashboardSummaryResponse summary = dashboardSummaryService.getDashboardSummary();
    return ResponseEntity.ok(new ApiResponse<>(200, "Dashboard summary retrieved successfully", summary));
    }
}
