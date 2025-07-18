package com.yatharth.finance_tracker.dto.transaction;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionResponse {
    private Long id;
    private String description;
    private Double amount;
    private LocalDateTime date;
    private String type;
    private Long categoryId;
}
