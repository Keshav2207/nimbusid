package com.nimbusid.auth.controller;

import com.nimbusid.auth.dto.LoginRequest;
import com.nimbusid.auth.dto.LoginResponse;
import com.nimbusid.auth.service.AuthService;
import com.nimbusid.platform.factory.ResponseFactory;
import com.nimbusid.platform.response.ApiResponse;
import com.nimbusid.user.dto.MeResponse;
import com.nimbusid.user.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final ResponseFactory responseFactory;

    public AuthController(AuthService authService,
                          ResponseFactory responseFactory) {
        this.authService = authService;
        this.responseFactory = responseFactory;
    }

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(
            @RequestBody LoginRequest request) {

        LoginResponse response = authService.login(request);

        return responseFactory.ok(response);
    }

    @GetMapping("/me")
    public ApiResponse<MeResponse> me(Authentication authentication) {

        User user = (User) authentication.getPrincipal();

        MeResponse response = new MeResponse(
                user.getId(),
                user.getEmail(),
                user.isEnabled()
        );

        return responseFactory.ok(response);
    }
}
