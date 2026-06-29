package com.nimbusid.auth.service;

import com.nimbusid.auth.dto.LoginRequest;
import com.nimbusid.auth.dto.LoginResponse;
import com.nimbusid.auth.exception.AccountDisabledException;
import com.nimbusid.auth.exception.InvalidCredentialsException;
import com.nimbusid.auth.token.JwtTokenService;
import com.nimbusid.user.entity.User;
import com.nimbusid.user.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenService jwtTokenService;

    public AuthService(UserService userService,
                       PasswordEncoder passwordEncoder, JwtTokenService jwtTokenService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenService = jwtTokenService;
    }

    public LoginResponse login(LoginRequest request) {

        User user = userService.findUserByEmail(request.getEmail())
                .orElseThrow(InvalidCredentialsException::new);

        if (!user.isEnabled()) {
            throw new AccountDisabledException();
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new InvalidCredentialsException();
        }

        String accessToken = jwtTokenService.generateAccessToken(user);
        String refreshToken = jwtTokenService.generateRefreshToken(user);

        return new LoginResponse(
                accessToken,
                refreshToken
        );
    }
}
