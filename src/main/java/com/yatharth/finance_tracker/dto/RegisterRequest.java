package com.yatharth.finance_tracker.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
@NotBlank
    @Size(min = 4,message = "Username must be at least 4 characters")
    private String username;

@NotBlank
@Email
    private String email;

    @NotBlank
    @Size(min = 6,message = "Password must be at least 6 characters")
    private String password;
}
