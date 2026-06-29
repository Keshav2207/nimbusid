package com.nimbusid.security.service;

import com.nimbusid.user.entity.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.UUID;

/**
 * Security-specific user lookup service.
 *
 * <p>Why not UserDetailsService?</p>
 *
 * <p>Spring Security's {@link org.springframework.security.core.userdetails.UserDetailsService}
 * is designed for username/password authentication, exposing only:</p>
 *
 * <pre>
 * UserDetails loadUserByUsername(String username)
 * </pre>
 *
 * <p>NimbusID authenticates users differently:</p>
 *
 * <ol>
 *   <li>User credentials are verified inside the auth module.</li>
 *   <li>A JWT is issued with the user's UUID as the subject ("sub").</li>
 *   <li>Subsequent requests are authenticated using that UUID.</li>
 * </ol>
 *
 * <p>Since JWT authentication is UUID-based rather than username-based,
 * introducing a UUID-centric abstraction keeps the architecture aligned with
 * the domain model instead of adapting UUIDs into username strings.</p>
 *
 * <p>This service belongs to the security module because it adapts the user
 * domain to Spring Security infrastructure. It contains no business logic.</p>
 */
public interface SecurityUserService {

    User loadUserById(UUID userId);

}