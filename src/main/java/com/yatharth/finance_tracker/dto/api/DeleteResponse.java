package com.yatharth.finance_tracker.dto.api;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DeleteResponse {
    private int status;
    private String message;

}
