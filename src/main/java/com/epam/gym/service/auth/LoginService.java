package com.epam.gym.service.auth;

import com.epam.gym.controller.rest.auth.dto.request.LoginRequest;
import com.epam.gym.controller.rest.auth.dto.response.LoginResponse;
import com.epam.gym.domain.auth.LoginAttempt;
import com.epam.gym.domain.user.User;
import com.epam.gym.exception.auth.AccountTemporarilyBlockedException;
import com.epam.gym.exception.auth.InvalidCredentialsException;
import com.epam.gym.repository.domain.auth.ILoginAttemptRepository;
import com.epam.gym.security.JwtProperties;
import com.epam.gym.security.service.JwtService;
import com.epam.gym.service.user.IUserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.epam.gym.GymApplication.MAX_LOGIN_ATTEMPTS_COUNT;
import static com.epam.gym.GymApplication.BLOCK_DURATION;


@Service
@RequiredArgsConstructor
public class LoginService implements ILoginService {

    private final ILoginAttemptRepository loginAttemptRepository;
    private final IUserService userService;
    private final IPasswordService passwordService;
    private final JwtService jwtService;
    private final JwtProperties jwtProperties;

    @Override
    @Transactional(
        noRollbackFor = {
            InvalidCredentialsException.class,
            AccountTemporarilyBlockedException.class
        }
    )
    public LoginResponse login(@NonNull LoginRequest request) {
        var user = userService.getByUsername(request.username());
        var attempt = getOrCreateAttempt(user);
        checkBruteForce(attempt);
        verifyPassword(request, user, attempt);
        loginAttemptRepository.deleteByUserUid(user.getUid());
        return LoginResponse.builder()
            .tokenType("Bearer")
            .accessToken(jwtService.generateToken(user.getUsername()))
            .expiresIn(jwtProperties.getExpiration())
            .build();
    }

    private LoginAttempt getOrCreateAttempt(@NonNull User user) {
        return loginAttemptRepository.findByUserUid(user.getUid())
            .orElseGet(() -> LoginAttempt.builder()
                .userUid(user.getUid())
                .build()
            );
    }

    private void checkBruteForce(@NonNull LoginAttempt attempt) {
        Optional.of(attempt)
            .filter(a -> a.isBlocked(MAX_LOGIN_ATTEMPTS_COUNT, BLOCK_DURATION))
            .map(a -> a.minutesUntilUnblock(BLOCK_DURATION))
            .ifPresent(minutes -> {
                throw new AccountTemporarilyBlockedException(minutes);
            });
        Optional.of(attempt)
            .filter(a -> a.isExpired(MAX_LOGIN_ATTEMPTS_COUNT, BLOCK_DURATION))
            .ifPresent(LoginAttempt::reset);
    }

    private void verifyPassword(@NonNull LoginRequest request,
                                @NonNull User user,
                                @NonNull LoginAttempt attempt) {
        Optional.of(request)
            .filter(r -> !passwordService.checkPassword(r.password(), user.getPassword()))
            .ifPresent(r -> {
                attempt.recordFailure();
                loginAttemptRepository.save(attempt);
                throw new InvalidCredentialsException();
            });
    }
}
