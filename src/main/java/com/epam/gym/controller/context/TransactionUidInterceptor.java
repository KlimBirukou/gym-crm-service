package com.epam.gym.controller.context;

import org.slf4j.MDC;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
public class TransactionUidInterceptor implements ClientHttpRequestInterceptor {

    public static final String MDC_KEY = "requestUid";
    public static final String HEADER_NAME = "X-Request-Uid";

    @Override
    public ClientHttpResponse intercept(HttpRequest request,
                                        byte[] body,
                                        ClientHttpRequestExecution execution) throws IOException {
        Optional.ofNullable(MDC.get(MDC_KEY))
            .ifPresent(uid -> request.getHeaders().add(HEADER_NAME, uid));
        return execution.execute(request, body);
    }
}
