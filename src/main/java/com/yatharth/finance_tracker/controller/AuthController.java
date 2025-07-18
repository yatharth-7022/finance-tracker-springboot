package com.yatharth.finance_tracker.controller;

import com.yatharth.finance_tracker.dto.ApiResponse;
import com.yatharth.finance_tracker.dto.AuthResponse;
import com.yatharth.finance_tracker.dto.LoginRequest;
import com.yatharth.finance_tracker.dto.RegisterRequest;
import com.yatharth.finance_tracker.service.AuthServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthServiceImpl authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest request){
        return ResponseEntity.ok(new ApiResponse<>(201,"User registered successfully",authService.register(request)));
    }
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request){
        return ResponseEntity.ok(new ApiResponse<>(200,"Login successful",authService.login(request)));
    }

}
