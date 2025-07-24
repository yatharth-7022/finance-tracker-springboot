package com.yatharth.finance_tracker.dto.transaction;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionByCategoryResponse {
    private Long categoryId;
    private String categoryName;
    private Double totalAmount;
    private Long transactionCount;
}
