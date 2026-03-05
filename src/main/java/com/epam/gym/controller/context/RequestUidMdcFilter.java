package com.epam.gym.controller.context;

import com.epam.gym.GymApplication;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
public class RequestUidMdcFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        var uid = Optional.ofNullable(request.getHeader(GymApplication.REQUEST_HEADER_NAME))
            .filter(StringUtils::isNotBlank)
            .orElseGet(() -> UUID.randomUUID().toString());
        try (var ignored = MDC.putCloseable(GymApplication.REQUEST_MDC_KEY, uid)) {
            response.addHeader(GymApplication.REQUEST_HEADER_NAME, uid);
            log.info("REST Request: {} {}", request.getMethod(), request.getRequestURI());
            filterChain.doFilter(request, response);
            log.info("REST Response: Status {}", response.getStatus());
        }
    }
}
