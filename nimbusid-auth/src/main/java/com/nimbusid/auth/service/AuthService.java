package com.nimbusid.auth.service;

import com.nimbusid.auth.dto.LoginRequest;
import com.nimbusid.auth.dto.LoginResponse;
import com.nimbusid.auth.dto.RefreshRequest;
import com.nimbusid.auth.exception.AccountDisabledException;
import com.nimbusid.auth.exception.InvalidCredentialsException;
import com.nimbusid.auth.token.JwtTokenService;
import com.nimbusid.auth.entity.RefreshToken;
import com.nimbusid.user.entity.User;
import com.nimbusid.user.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenService jwtTokenService;
    private final RefreshTokenService refreshTokenService;

    public AuthService(UserService userService,
                       PasswordEncoder passwordEncoder, JwtTokenService jwtTokenService, RefreshTokenService refreshTokenService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenService = jwtTokenService;
        this.refreshTokenService = refreshTokenService;
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
        String refreshToken = refreshTokenService.generateRefreshToken(user);

        return new LoginResponse(
                accessToken,
                refreshToken
        );
    }

    public LoginResponse refresh(RefreshRequest request) {
        RefreshToken refreshToken =
                refreshTokenService.validateRefreshToken(
                        request.refreshToken()
                );

        User user = refreshToken.getUser();

        String accessToken =
                jwtTokenService.generateAccessToken(user);

        String newRefreshToken =
                refreshTokenService.rotateRefreshToken(
                        refreshToken
                );

        return new LoginResponse(
                accessToken,
                newRefreshToken
        );
    }

    public void logout(RefreshRequest request) {
        RefreshToken refreshToken =
                refreshTokenService.validateRefreshToken(
                        request.refreshToken()
                );
        refreshTokenService.revokeRefreshToken(refreshToken);
    }
}
