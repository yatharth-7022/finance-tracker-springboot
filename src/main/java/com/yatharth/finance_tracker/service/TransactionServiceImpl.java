package com.yatharth.finance_tracker.service;

import com.yatharth.finance_tracker.dto.TransactionRequest;
import com.yatharth.finance_tracker.dto.TransactionResponse;
import com.yatharth.finance_tracker.entity.Category;
import com.yatharth.finance_tracker.entity.Transaction;
import com.yatharth.finance_tracker.entity.User;
import com.yatharth.finance_tracker.enums.TransactionType;
import com.yatharth.finance_tracker.repository.CategoryRepository;
import com.yatharth.finance_tracker.repository.TransactionRepository;
import com.yatharth.finance_tracker.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public TransactionResponse createTransaction(TransactionRequest request) {
        User user = getCurrentUser();
        Category category = categoryRepository.findById(request.getCategoryId()).orElseThrow(() -> new EntityNotFoundException("Category not found"));
        Transaction transaction = Transaction.builder()
                .description(request.getDescription()).amount(request.getAmount()).type(TransactionType.valueOf(request.getType().toUpperCase())).date(LocalDateTime.now()).category(category).user(user).build();
        Transaction savedTransaction = transactionRepository.save(transaction);
        return new TransactionResponse(savedTransaction.getId(), savedTransaction.getDescription(), savedTransaction.getAmount(), savedTransaction.getDate(), savedTransaction.getType().name(), savedTransaction.getCategory().getId());


    }

    @Override
    public List<TransactionResponse> getAllTransactions() {
       User user = getCurrentUser();
        List<Transaction> transactions = transactionRepository.findByUser(user);
        List<TransactionResponse> responses = new ArrayList<>();
        for(Transaction transaction: transactions){
            TransactionResponse response = new TransactionResponse(
                    transaction.getId(),
                    transaction.getDescription(),
                    transaction.getAmount(),
                    transaction.getDate(),
                    transaction.getType().name(),
                    transaction.getCategory().getId()
            )
                    ;
            responses.add(response);
        }
        return responses;
    }

    @Override
    public void deleteTransaction(Long id) {
        Transaction transaction  = transactionRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Transaction not found"));
        transactionRepository.deleteById(id);
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User Not Found"));

    }
}
