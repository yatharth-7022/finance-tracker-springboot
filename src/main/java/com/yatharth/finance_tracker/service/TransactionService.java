package com.yatharth.finance_tracker.service;

import com.yatharth.finance_tracker.dto.AuthResponse;
import com.yatharth.finance_tracker.dto.TransactionRequest;
import com.yatharth.finance_tracker.dto.TransactionResponse;

import java.util.List;

public interface TransactionService {
    TransactionResponse createTransaction(TransactionRequest transactionRequest);
    List<TransactionResponse> getAllTransactions();
    void deleteTransaction(Long id);
}
