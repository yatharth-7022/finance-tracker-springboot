package com.yatharth.finance_tracker.service.auth;

import com.yatharth.finance_tracker.dto.auth.AuthResponse;
import com.yatharth.finance_tracker.dto.auth.LoginRequest;
import com.yatharth.finance_tracker.dto.auth.RegisterRequest;

public interface AuthService {

    AuthResponse register(RegisterRequest registerRequest);
    AuthResponse login(LoginRequest loginRequest);
}
