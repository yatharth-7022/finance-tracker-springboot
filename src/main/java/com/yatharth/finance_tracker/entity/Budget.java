package com.yatharth.finance_tracker.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name="budgets",uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id","category_id","month","year"})
})

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Budget {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private double amount;

    @ManyToOne(optional = false)
    @JoinColumn(name = "category_id")
    private Category category;

    private int month;
    private int year;

    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}

