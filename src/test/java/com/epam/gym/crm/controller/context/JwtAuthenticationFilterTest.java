package com.epam.gym.crm.controller.context;

import com.epam.gym.crm.client.auth.IAuthClient;
import com.epam.gym.crm.client.auth.ValidateResponse;
import com.epam.gym.crm.configuration.properties.JwtProperties;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    private static final String AUTH_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer";
    private static final String TOKEN = "token";
    private static final String USERNAME = "username";

    @Mock
    private IAuthClient authClient;
    @Mock
    private JwtProperties jwtProperties;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JwtAuthenticationFilter testObject;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
        verifyNoMoreInteractions(authClient, jwtProperties, request, response, filterChain);
    }

    @Test
    void doFilterInternal_shouldAuthenticate_whenTokenIsValid() throws ServletException, IOException {
        doReturn(AUTH_HEADER).when(jwtProperties).authHeader();
        doReturn(BEARER_PREFIX).when(jwtProperties).bearerPrefix();
        doReturn(BEARER_PREFIX + TOKEN).when(request).getHeader(AUTH_HEADER);
        doReturn(ValidateResponse.builder().valid(true).username(USERNAME).build()).when(authClient).validate(BEARER_PREFIX + TOKEN);

        testObject.doFilterInternal(request, response, filterChain);

        var authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(authentication);
        assertEquals(USERNAME, authentication.getPrincipal());
        verify(jwtProperties).authHeader();
        verify(jwtProperties).bearerPrefix();
        verify(request).getHeader(AUTH_HEADER);
        verify(authClient).validate(BEARER_PREFIX + TOKEN);
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_shouldNotAuthenticate_whenHeaderIsMissing() throws ServletException, IOException {
        doReturn(AUTH_HEADER).when(jwtProperties).authHeader();
        doReturn(null).when(request).getHeader(AUTH_HEADER);

        testObject.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(jwtProperties).authHeader();
        verify(request).getHeader(AUTH_HEADER);
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_shouldNotAuthenticate_whenHeaderHasNoPrefix() throws ServletException, IOException {
        doReturn(AUTH_HEADER).when(jwtProperties).authHeader();
        doReturn(BEARER_PREFIX).when(jwtProperties).bearerPrefix();
        doReturn(TOKEN).when(request).getHeader(AUTH_HEADER);

        testObject.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(jwtProperties).authHeader();
        verify(jwtProperties).bearerPrefix();
        verify(request).getHeader(AUTH_HEADER);
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_shouldNotAuthenticate_whenValidateResponseIsInvalid() throws ServletException, IOException {
        doReturn(AUTH_HEADER).when(jwtProperties).authHeader();
        doReturn(BEARER_PREFIX).when(jwtProperties).bearerPrefix();
        doReturn(BEARER_PREFIX + TOKEN).when(request).getHeader(AUTH_HEADER);
        doReturn(ValidateResponse.builder().valid(false).build()).when(authClient).validate(BEARER_PREFIX + TOKEN);

        testObject.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(jwtProperties).authHeader();
        verify(jwtProperties).bearerPrefix();
        verify(request).getHeader(AUTH_HEADER);
        verify(authClient).validate(BEARER_PREFIX + TOKEN);
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_shouldNotAuthenticate_whenAuthClientThrowsException() throws ServletException, IOException {
        doReturn(AUTH_HEADER).when(jwtProperties).authHeader();
        doReturn(BEARER_PREFIX).when(jwtProperties).bearerPrefix();
        doReturn(BEARER_PREFIX + TOKEN).when(request).getHeader(AUTH_HEADER);
        doThrow(new RuntimeException()).when(authClient).validate(BEARER_PREFIX + TOKEN);

        testObject.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(jwtProperties).authHeader();
        verify(jwtProperties).bearerPrefix();
        verify(request).getHeader(AUTH_HEADER);
        verify(authClient).validate(BEARER_PREFIX + TOKEN);
        verify(filterChain).doFilter(request, response);
    }
}
