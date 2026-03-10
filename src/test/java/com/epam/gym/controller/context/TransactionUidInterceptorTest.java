package com.epam.gym.controller.context;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.mock.http.client.MockClientHttpRequest;

import java.io.IOException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class TransactionUidInterceptorTest {

    private static final String REQUEST_MDC_KEY = "requestUid";
    private static final String REQUEST_HEADER_NAME = "X-Request-Uid";

    private final TransactionUidInterceptor testObject = new TransactionUidInterceptor();

    @AfterEach
    void tearDown() {
        MDC.clear();
    }

    @Test
    void intercept_shouldAddHeaderFromMdc() throws IOException {
        var uid = UUID.randomUUID().toString();
        MDC.put(REQUEST_MDC_KEY, uid);
        var request = new MockClientHttpRequest();
        var body = new byte[0];
        var execution = mock(ClientHttpRequestExecution.class);

        testObject.intercept(request, body, execution);

        assertEquals(uid, request.getHeaders().getFirst(REQUEST_HEADER_NAME));
        verify(execution).execute(any(), any());
    }
}
