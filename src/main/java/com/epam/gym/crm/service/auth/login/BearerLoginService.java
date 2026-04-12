package com.epam.gym.crm.service.auth.login;

import com.epam.gym.crm.client.auth.GenerateTokenRequest;
import com.epam.gym.crm.client.auth.IAuthClient;
import com.epam.gym.crm.controller.rest.auth.dto.request.LoginRequest;
import com.epam.gym.crm.controller.rest.auth.dto.response.LoginResponse;
import com.epam.gym.crm.exception.auth.AccountTemporarilyBlockedException;
import com.epam.gym.crm.exception.auth.InvalidCredentialsException;
import com.epam.gym.crm.service.auth.password.IPasswordService;
import com.epam.gym.crm.service.user.IUserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Primary
public class BearerLoginService implements ILoginService {

    private final IUserService userService;
    private final IPasswordService passwordService;
    private final IAuthClient authClient;

    @Override
    public LoginResponse login(@NonNull LoginRequest request) {
        var user = userService.getByUsername(request.username());
        var bruteForceStatus = authClient.getBruteForceStatus(user.getUid());
        if (bruteForceStatus.blocked()) {
            throw new AccountTemporarilyBlockedException(bruteForceStatus.minutesLeft());
        }
        if (!passwordService.checkPassword(request.password(), user.getPassword())) {
            authClient.recordFailedAttempt(user.getUid());
            throw new InvalidCredentialsException();
        }
        var tokenRequest = GenerateTokenRequest.builder()
            .userUid(user.getUid())
            .username(user.getUsername())
            .build();
        return authClient.generateToken(tokenRequest);
    }
}
