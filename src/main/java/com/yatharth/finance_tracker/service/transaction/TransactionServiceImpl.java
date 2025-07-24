package com.yatharth.finance_tracker.service.transaction;

import com.yatharth.finance_tracker.dto.transaction.TransactionByCategoryResponse;
import com.yatharth.finance_tracker.dto.transaction.TransactionRequest;
import com.yatharth.finance_tracker.dto.transaction.TransactionResponse;
import com.yatharth.finance_tracker.entity.Category;
import com.yatharth.finance_tracker.entity.Transaction;
import com.yatharth.finance_tracker.entity.User;
import com.yatharth.finance_tracker.enums.TransactionType;
import com.yatharth.finance_tracker.repository.category.CategoryRepository;
import com.yatharth.finance_tracker.repository.transaction.TransactionRepository;
import com.yatharth.finance_tracker.repository.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    @Override
    public List<TransactionByCategoryResponse> findCurrentMonthExpenseByUserGroupByCategory() {
        User user = getCurrentUser();
        List<Object[]> rawData = transactionRepository.findCurrentMonthExpenseByUserGroupByCategory(user.getId());
        List<TransactionByCategoryResponse> responses = new ArrayList<>();
        for(Object[] row:rawData){
            TransactionByCategoryResponse response = new TransactionByCategoryResponse(
                    (Long) row[0],
                    (String) row[1],
                    (Double) row[2],
                    (Long) row[3]
            );
            responses.add(response);

        }
        return responses;
    }

    @Override
    public List<TransactionResponse> findTransactionByType(String type) {
        User user = getCurrentUser();
        TransactionType transactionType = TransactionType.valueOf(type.toUpperCase());
        List<Transaction> transactions = transactionRepository.findByUserAndType(user, transactionType);
        
        List<TransactionResponse> responses = new ArrayList<>();
        for(Transaction transaction: transactions){
            TransactionResponse response = new TransactionResponse(
                    transaction.getId(),
                    transaction.getDescription(),
                    transaction.getAmount(),
                    transaction.getDate(),
                    transaction.getType().name(),
                    transaction.getCategory().getId()
            );
            responses.add(response);
        }
        return responses;
    }

    @Override
    public List<TransactionResponse> findTransactionByCategoryId(Long categoryId) {
        User user = getCurrentUser();
        List<Transaction> transactions = transactionRepository.findByUserAndCategoryId(user.getId(), categoryId);
        List<TransactionResponse> responses = new ArrayList<>();
        for(Transaction transaction: transactions){
            TransactionResponse response = new TransactionResponse(
                    transaction.getId(),
                    transaction.getDescription(),
                    transaction.getAmount(),
                    transaction.getDate(),
                    transaction.getType().name(),
                    transaction.getCategory().getId()
            );
            responses.add(response);
        }
        return responses;
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User Not Found"));

    }
}