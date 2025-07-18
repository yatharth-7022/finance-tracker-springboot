package com.yatharth.finance_tracker.service;

import com.yatharth.finance_tracker.dto.AuthResponse;
import com.yatharth.finance_tracker.dto.LoginRequest;
import com.yatharth.finance_tracker.dto.RegisterRequest;

public interface AuthService {

    AuthResponse register(RegisterRequest registerRequest);
    AuthResponse login(LoginRequest loginRequest);
}
