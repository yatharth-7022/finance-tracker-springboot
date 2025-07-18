package com.yatharth.finance_tracker.dto.api;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor

public class ApiResponse<T> {
    private int status;
    private String message;
    private T data;
}
