package com.epam.gym.controller.context;

import com.epam.gym.GymApplication;
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

    @Override
    public ClientHttpResponse intercept(HttpRequest request,
                                        byte[] body,
                                        ClientHttpRequestExecution execution) throws IOException {
        Optional.ofNullable(MDC.get(GymApplication.REQUEST_MDC_KEY))
            .ifPresent(uid -> request.getHeaders().add(GymApplication.REQUEST_HEADER_NAME, uid));
        return execution.execute(request, body);
    }
}
