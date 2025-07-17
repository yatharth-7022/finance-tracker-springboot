package com.yatharth.finance_tracker.repository;

import com.yatharth.finance_tracker.entity.Category;
import com.yatharth.finance_tracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {
    List<Category> findByUser(User user);
}
