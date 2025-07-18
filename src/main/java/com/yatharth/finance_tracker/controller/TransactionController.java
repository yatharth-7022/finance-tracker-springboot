package com.yatharth.finance_tracker.controller;

import com.yatharth.finance_tracker.dto.ApiResponse;
import com.yatharth.finance_tracker.dto.AuthResponse;
import com.yatharth.finance_tracker.dto.TransactionRequest;
import com.yatharth.finance_tracker.dto.TransactionResponse;
import com.yatharth.finance_tracker.service.TransactionService;
import com.yatharth.finance_tracker.service.TransactionServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/v1/transaction")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<TransactionResponse>> createTransaction(@Valid @RequestBody TransactionRequest transactionRequest) {
        return ResponseEntity.ok(new ApiResponse<>(201, "Transaction created successfully", transactionService.createTransaction(transactionRequest)));

    }

    @GetMapping()
    public ResponseEntity<ApiResponse<List<TransactionResponse>>> getAllTransactions(){
        return ResponseEntity.ok(new ApiResponse<>(200, "Transactions retrieved successfully", transactionService.getAllTransactions()));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<String>> deleteTransaction(@PathVariable Long id){
        transactionService.deleteTransaction(id);
        return ResponseEntity.ok(new ApiResponse<>(200, "Transaction deleted successfully", null));
    }


}
