package com.epam.gym.crm.controller.context;

import com.epam.gym.crm.configuration.properties.RequestUidProperties;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TransactionUidInterceptor implements ClientHttpRequestInterceptor {

    private final RequestUidProperties requestUidProperties;

    @Override
    public ClientHttpResponse intercept(HttpRequest request,
                                        byte[] body,
                                        ClientHttpRequestExecution execution) throws IOException {
        Optional.ofNullable(MDC.get(requestUidProperties.mdcKey()))
            .ifPresent(uid -> request.getHeaders().add(requestUidProperties.headerName(), uid));
        return execution.execute(request, body);
    }
}
