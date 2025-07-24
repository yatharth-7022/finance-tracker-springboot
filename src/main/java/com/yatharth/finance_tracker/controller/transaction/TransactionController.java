package com.yatharth.finance_tracker.controller.transaction;

import com.yatharth.finance_tracker.dto.api.ApiResponse;
import com.yatharth.finance_tracker.dto.transaction.TransactionByCategoryResponse;
import com.yatharth.finance_tracker.dto.transaction.TransactionRequest;
import com.yatharth.finance_tracker.dto.transaction.TransactionResponse;
import com.yatharth.finance_tracker.service.transaction.TransactionService;
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
    public ResponseEntity<ApiResponse<List<TransactionResponse>>> getAllTransactions(@RequestParam(required = false) String type,
                                                                                     @RequestParam(required = false) Long categoryId) {
        List<TransactionResponse> transactions;
        if(type!=null && !type.isEmpty()){
            transactions=transactionService.findTransactionByType(type);
        }
        else if(categoryId!=null){
            transactions=transactionService.findTransactionByCategoryId(categoryId);
        }
        else{
            transactions=transactionService.getAllTransactions();
        }

        return ResponseEntity.ok(new ApiResponse<>(200, "Transactions retrieved successfully",transactions));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<String>> deleteTransaction(@PathVariable Long id) {
        transactionService.deleteTransaction(id);
        return ResponseEntity.ok(new ApiResponse<>(200, "Transaction deleted successfully", null));
    }

    @GetMapping("/category")

    public ResponseEntity<ApiResponse<List<TransactionByCategoryResponse>>> getTransactionByCategory() {
        return ResponseEntity.ok(new ApiResponse<>(200, "Transactions retrieved successfully", transactionService.findCurrentMonthExpenseByUserGroupByCategory()));
    }

}
