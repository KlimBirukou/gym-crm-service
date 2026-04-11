package com.epam.gym.client.auth;

import com.epam.gym.configuration.FeignConfiguration;
import com.epam.gym.controller.rest.auth.dto.response.LoginResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.UUID;

@FeignClient(
    name = "gym-auth-server",
    path = "/internal/v1",
    configuration = FeignConfiguration.class
)
public interface IAuthClient {

    @GetMapping("/protection/{userUid}")
    BruteForceStatusResponse getBruteForceStatus(@PathVariable("userUid") UUID userUid);

    @PostMapping("/protection/{userUid}")
    void recordFailedAttempt(@PathVariable("userUid") UUID userUid);

    @PostMapping("/token")
    LoginResponse generateToken(@RequestBody GenerateTokenRequest request);

    @GetMapping("/token")
    ValidateResponse validate(@RequestHeader("Authorization") String authHeader);
}
