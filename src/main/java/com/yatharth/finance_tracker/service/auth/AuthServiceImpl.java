package com.yatharth.finance_tracker.service.auth;

import com.yatharth.finance_tracker.dto.auth.AuthResponse;
import com.yatharth.finance_tracker.dto.auth.LoginRequest;
import com.yatharth.finance_tracker.dto.auth.RegisterRequest;
import com.yatharth.finance_tracker.entity.User;
import com.yatharth.finance_tracker.exception.InvalidCredentialsException;
import com.yatharth.finance_tracker.exception.UserNotFoundException;
import com.yatharth.finance_tracker.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public AuthResponse register(RegisterRequest request) {
        var user = User.builder().username(request.getUsername()).email(request.getEmail()).password(passwordEncoder.encode(request.getPassword())).build();
        userRepository.save(user);
        String token = jwtService.generateToken(user);
        return new AuthResponse(token, user.getUsername());
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsernameOrEmail())
                .or(() -> userRepository.findByEmail(request.getUsernameOrEmail()))
                .orElseThrow(() -> new UserNotFoundException("User Not Found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            System.out.println("password did not match");
            throw new InvalidCredentialsException("Invalid credentials");
        }

        String token = jwtService.generateToken(user);
        return new AuthResponse(token, user.getUsername());
    }

}
