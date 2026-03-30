package com.epam.gym.controller.context;

import com.epam.gym.configuration.properties.RequestUidProperties;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.MDC;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.mock.http.client.MockClientHttpRequest;

import java.io.IOException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TransactionUidInterceptorTest {

    private static final String REQUEST_MDC_KEY = "key";
    private static final String REQUEST_HEADER_NAME = "name";

    @Mock
    private RequestUidProperties requestUidProperties;

    @InjectMocks
    private TransactionUidInterceptor testObject;

    @BeforeEach
    void setUp() {
        lenient().doReturn(REQUEST_MDC_KEY).when(requestUidProperties).mdcKey();
        lenient().doReturn(REQUEST_HEADER_NAME).when(requestUidProperties).headerName();
    }

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

    @Test
    void intercept_shouldNotAddHeader_whenMdcIsEmpty() throws IOException {
        var request = new MockClientHttpRequest();
        var execution = mock(ClientHttpRequestExecution.class);

        testObject.intercept(request, new byte[0], execution);

        assertNull(request.getHeaders().getFirst(REQUEST_HEADER_NAME));
        verify(execution).execute(any(), any());
    }
}
