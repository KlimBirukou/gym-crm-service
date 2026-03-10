package com.epam.gym.controller.context;

import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class RequestUidMdcFilterTest {

    private static final String REQUEST_HEADER_NAME = "X-Request-Uid";

    private final RequestUidMdcFilter testObject = new RequestUidMdcFilter();

    @AfterEach
    void tearDown() {
        MDC.clear();
    }

    @Test
    void doFilterInternal_shouldUseHeaderIfPresent() throws Exception {
        var existingUid = UUID.randomUUID().toString();
        var request = new MockHttpServletRequest();
        request.addHeader(REQUEST_HEADER_NAME, existingUid);
        var response = new MockHttpServletResponse();
        var chain = mock(FilterChain.class);

        testObject.doFilterInternal(request, response, chain);

        assertEquals(existingUid, response.getHeader(REQUEST_HEADER_NAME));
        verify(chain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_shouldGenerateUidIfHeaderMissing() throws Exception {
        var request = new MockHttpServletRequest();
        var response = new MockHttpServletResponse();
        var chain = mock(FilterChain.class);

        testObject.doFilterInternal(request, response, chain);

        assertNotNull(response.getHeader(REQUEST_HEADER_NAME));
        verify(chain).doFilter(request, response);
    }
}
