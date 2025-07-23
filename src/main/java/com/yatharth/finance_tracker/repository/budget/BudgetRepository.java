package com.yatharth.finance_tracker.repository.budget;

import com.yatharth.finance_tracker.entity.Budget;
import com.yatharth.finance_tracker.entity.Category;
import com.yatharth.finance_tracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface BudgetRepository extends JpaRepository<Budget,Long> {
    List<Budget> findByUser(User user);
    Optional<Budget> findByUserAndCategoryAndMonthAndYear(User user, Category category, int month, int year);

}
