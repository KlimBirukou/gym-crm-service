package com.epam.gym.controller.context;

import org.junit.jupiter.api.BeforeEach;
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

    private TransactionUidInterceptor testObject;

    @BeforeEach
    void setUp() {
        testObject = new TransactionUidInterceptor();
    }

    @Test
    void intercept_shouldAddHeaderFromMdc() throws IOException {
        var uid = UUID.randomUUID().toString();
        MDC.put(TransactionUidInterceptor.MDC_KEY, uid);
        var request = new MockClientHttpRequest();
        var body = new byte[0];
        var execution = mock(ClientHttpRequestExecution.class);

        testObject.intercept(request, body, execution);

        assertEquals(uid, request.getHeaders().getFirst(TransactionUidInterceptor.HEADER_NAME));
        verify(execution).execute(any(), any());
        MDC.clear();
    }
}
