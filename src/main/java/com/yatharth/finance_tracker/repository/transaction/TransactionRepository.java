package com.yatharth.finance_tracker.repository.transaction;

import com.yatharth.finance_tracker.dto.transaction.TransactionByCategoryResponse;
import com.yatharth.finance_tracker.entity.Transaction;
import com.yatharth.finance_tracker.entity.User;
import com.yatharth.finance_tracker.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByUser(User user);

    List<Transaction> findByUserOrderByDateDesc(User user);

    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.user = :user AND t.type = :type AND MONTH(t.date) = :month AND YEAR(t.date) = :year")
    Double sumByUserAndTypeAndMonthAndYear(User user, TransactionType type, int month, int year);

    @Query("SELECT t FROM Transaction t WHERE t.user.id = :userId AND t.type = 'EXPENSE' AND MONTH(t.date) = MONTH(CURRENT_DATE)")
    List<Transaction> findCurrentMonthExpenseByUser(@Param("userId") Long userId);

    @Query("SELECT t FROM Transaction t WHERE t.user.id= :userId AND t.type='EXPENSE' AND t.category.id=:categoryId AND MONTH(t.date) = MONTH(CURRENT_DATE) ")
    List<Transaction> findCurrentMonthExpenseByUserAndCategory(@Param("userId") Long userId, @Param("categoryId") Long categoryId);

    @Query("SELECT t.category.id,t.category.name,SUM(t.amount),COUNT(t) FROM Transaction t WHERE t.user.id=:userId AND t.type='EXPENSE' AND MONTH(t.date)=MONTH(CURRENT_DATE) AND YEAR (t.date)=YEAR(CURRENT_DATE) GROUP BY t.category.id,t.category.name")
    List<Object[]> findCurrentMonthExpenseByUserGroupByCategory(@Param("userId") Long userId);

    List<Transaction> findByUserAndType(User user, TransactionType type);

    @Query("SELECT t FROM Transaction t WHERE t.user.id = :userId AND t.category.id = :categoryId")
    List<Transaction> findByUserAndCategoryId(@Param("userId") Long userId, @Param("categoryId") Long categoryId);
}