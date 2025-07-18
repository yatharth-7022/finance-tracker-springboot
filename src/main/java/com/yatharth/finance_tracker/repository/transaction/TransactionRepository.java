package com.yatharth.finance_tracker.repository.transaction;

import com.yatharth.finance_tracker.entity.Transaction;
import com.yatharth.finance_tracker.entity.User;
import com.yatharth.finance_tracker.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByUser(User user);
    List<Transaction> findByUserOrderByDateDesc(User user);
    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.user = :user AND t.type = :type AND MONTH(t.date) = :month AND YEAR(t.date) = :year")
    Double sumByUserAndTypeAndMonthAndYear(User user, TransactionType type, int month, int year);
}
