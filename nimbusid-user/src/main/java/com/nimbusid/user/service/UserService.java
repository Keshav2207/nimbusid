package com.nimbusid.user.service;

import com.nimbusid.user.dto.RegisterUserRequest;
import com.nimbusid.user.dto.UserResponse;
import com.nimbusid.user.entity.User;
import com.nimbusid.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final Clock clock;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, Clock clock) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.clock = clock;
    }

    public UserResponse registerUser(RegisterUserRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists.");
        }

        Instant now = Instant.now(clock);
        User user = new User(
                UUID.randomUUID(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                true,
                now,
                now
        );
        userRepository.save(user);

        return new UserResponse(
                user.getId(),
                user.getEmail()
        );
    }

    public Optional<User> findUserById(UUID id) {
        return userRepository.findById(id);
    }

    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
