package com.nimbusid.security.service;

import com.nimbusid.user.entity.User;
import com.nimbusid.user.service.UserService;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class NimbusSecurityUserService implements SecurityUserService {

    private final UserService userService;

    public NimbusSecurityUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public User loadUserById(UUID userId) {

        return userService.findUserById(userId)
                .orElseThrow(() -> new RuntimeException("User not found."));
    }
}
