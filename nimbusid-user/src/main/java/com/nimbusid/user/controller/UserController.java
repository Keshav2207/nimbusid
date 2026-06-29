package com.nimbusid.user.controller;

import com.nimbusid.platform.factory.ResponseFactory;
import com.nimbusid.platform.response.ApiResponse;
import com.nimbusid.user.dto.MeResponse;
import com.nimbusid.user.dto.RegisterUserRequest;
import com.nimbusid.user.dto.UserResponse;
import com.nimbusid.user.entity.User;
import com.nimbusid.user.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    private final ResponseFactory responseFactory;

    public UserController(UserService userService,
                          ResponseFactory responseFactory) {

        this.userService = userService;
        this.responseFactory = responseFactory;
    }

    @PostMapping
    public ApiResponse<UserResponse> registerUser(
            @RequestBody RegisterUserRequest request) {

        UserResponse response = userService.registerUser(request);

        return responseFactory.ok(response);
    }
}
