package com.yatharth.finance_tracker.service.transaction;

import com.yatharth.finance_tracker.dto.transaction.TransactionByCategoryResponse;
import com.yatharth.finance_tracker.dto.transaction.TransactionRequest;
import com.yatharth.finance_tracker.dto.transaction.TransactionResponse;

import java.util.List;

public interface TransactionService {
    TransactionResponse createTransaction(TransactionRequest transactionRequest);
    List<TransactionResponse> getAllTransactions();
    void deleteTransaction(Long id);
    List<TransactionByCategoryResponse> findCurrentMonthExpenseByUserGroupByCategory();
    List<TransactionResponse> findTransactionByType(String type);
    List<TransactionResponse> findTransactionByCategoryId(Long categoryId);
}