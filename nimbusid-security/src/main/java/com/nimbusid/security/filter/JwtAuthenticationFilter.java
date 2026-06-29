package com.nimbusid.security.filter;

import com.nimbusid.auth.token.JwtTokenService;
import com.nimbusid.security.service.NimbusSecurityUserService;
import com.nimbusid.security.service.SecurityUserService;
import com.nimbusid.user.entity.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtTokenService jwtTokenService;
    private final SecurityUserService securityUserService;

    public JwtAuthenticationFilter(
            JwtTokenService jwtTokenService,
            SecurityUserService securityUserService) {

        this.jwtTokenService = jwtTokenService;
        this.securityUserService = securityUserService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

//        System.out.println(">>> JwtAuthenticationFilter invoked");
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authorization == null || !authorization.startsWith(BEARER_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorization.substring(BEARER_PREFIX.length());

        try {

            UUID userId = jwtTokenService.extractUserId(token);

            User user = securityUserService.loadUserById(userId);

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            user,
                            null,
                            AuthorityUtils.NO_AUTHORITIES
                    );

            authentication.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (Exception e) {
            System.out.println("JWT Authentication failed:");
            e.printStackTrace();
        }

        filterChain.doFilter(request, response);
    }
}
