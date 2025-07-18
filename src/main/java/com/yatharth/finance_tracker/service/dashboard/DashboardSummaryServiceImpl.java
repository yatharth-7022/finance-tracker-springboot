package com.yatharth.finance_tracker.service.dashboard;

import com.yatharth.finance_tracker.dto.dashboard.DashboardSummaryResponse;
import com.yatharth.finance_tracker.entity.User;
import com.yatharth.finance_tracker.enums.TransactionType;
import com.yatharth.finance_tracker.exception.UserNotFoundException;
import com.yatharth.finance_tracker.repository.transaction.TransactionRepository;
import com.yatharth.finance_tracker.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class DashboardSummaryServiceImpl implements DashboardSummaryService {
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private User getCurrentUser(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User Not Found"));

    }

    @Override
    public DashboardSummaryResponse getDashboardSummary() {
        User user = getCurrentUser();
        int currentMonth = LocalDate.now().getMonthValue();
        int currentYear = LocalDate.now().getYear();
        Double totalIncome = transactionRepository.sumByUserAndTypeAndMonthAndYear(user, TransactionType.INCOME,currentMonth,currentYear);
        Double totalExpense = transactionRepository.sumByUserAndTypeAndMonthAndYear(user, TransactionType.EXPENSE,currentMonth,currentYear);
        totalIncome=totalIncome!=null?totalIncome:0.0;
        totalExpense=totalExpense!=null?totalExpense:0.0;
        Double balance = totalIncome - totalExpense;
        return new DashboardSummaryResponse(totalIncome,totalExpense,balance);


    }
}
