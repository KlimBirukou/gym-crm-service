package com.epam.gym.controller.context;

import com.epam.gym.service.auth.jwt.IJwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private final IJwtService jwtService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        extractToken(request)
            .filter(jwtService::isTokenValid)
            .ifPresent(this::authenticateUser);
        filterChain.doFilter(request, response);
    }

    private void authenticateUser(String token) {
        var username = jwtService.extractUsername(token);
        var authentication = new UsernamePasswordAuthenticationToken(
            username,
            null,
            List.of()
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.debug("JWT authenticated: username={}", username);
    }

    private Optional<String> extractToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(AUTHORIZATION_HEADER))
            .filter(StringUtils::hasText)
            .filter(header -> header.startsWith(BEARER_PREFIX))
            .map(header -> header.substring(BEARER_PREFIX.length()));
    }
}
