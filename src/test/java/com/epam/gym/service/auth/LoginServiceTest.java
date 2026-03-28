package com.epam.gym.service.auth;

import com.epam.gym.controller.rest.auth.dto.request.LoginRequest;
import com.epam.gym.domain.auth.LoginAttempt;
import com.epam.gym.domain.user.User;
import com.epam.gym.exception.auth.AccountTemporarilyBlockedException;
import com.epam.gym.exception.auth.InvalidCredentialsException;
import com.epam.gym.repository.domain.auth.ILoginAttemptRepository;
import com.epam.gym.security.JwtProperties;
import com.epam.gym.security.service.JwtService;
import com.epam.gym.service.user.IUserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static com.epam.gym.GymApplication.BLOCK_DURATION;
import static com.epam.gym.GymApplication.MAX_LOGIN_ATTEMPTS_COUNT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {

    private static final UUID USER_UID = UUID.randomUUID();
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String HASHED_PASSWORD = "hashed_password";
    private static final String TOKEN = "token";
    private static final long EXPIRATION = 3600L;
    public static final String BEARER = "Bearer";

    @Mock
    private ILoginAttemptRepository loginAttemptRepository;
    @Mock
    private IUserService userService;
    @Mock
    private IPasswordService passwordService;
    @Mock
    private JwtService jwtService;
    @Mock
    private JwtProperties jwtProperties;

    @Captor
    private ArgumentCaptor<LoginAttempt> attemptCaptor;

    @InjectMocks
    private LoginService testObject;

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(
            loginAttemptRepository,
            userService,
            passwordService,
            jwtService,
            jwtProperties
        );
    }

    @Test
    void login_shouldReturnToken_whenCredentialsValidAndNoAttempts() {
        var user = buildUser();
        var request = buildRequest();
        doReturn(user).when(userService).getByUsername(USERNAME);
        doReturn(Optional.empty()).when(loginAttemptRepository).findByUserUid(USER_UID);
        doReturn(true).when(passwordService).checkPassword(PASSWORD, HASHED_PASSWORD);
        doReturn(TOKEN).when(jwtService).generateToken(USERNAME);
        doReturn(EXPIRATION).when(jwtProperties).getExpiration();

        var result = testObject.login(request);

        assertNotNull(result);
        assertEquals(TOKEN, result.accessToken());
        assertEquals(BEARER, result.tokenType());
        verify(loginAttemptRepository).deleteByUserUid(USER_UID);
    }

    @Test
    void login_shouldReturnToken_andClearAttempts_whenCredentialsValidAndAttemptsExist() {
        var user = buildUser();
        var request = buildRequest();
        var existingAttempt = buildAttempt(1, LocalDateTime.now().minusMinutes(1));
        doReturn(user).when(userService).getByUsername(USERNAME);
        doReturn(Optional.of(existingAttempt)).when(loginAttemptRepository).findByUserUid(USER_UID);
        doReturn(true).when(passwordService).checkPassword(PASSWORD, HASHED_PASSWORD);
        doReturn(TOKEN).when(jwtService).generateToken(USERNAME);
        doReturn(EXPIRATION).when(jwtProperties).getExpiration();

        testObject.login(request);

        verify(loginAttemptRepository).deleteByUserUid(USER_UID);
    }

    @Test
    void login_shouldReturnToken_whenBlockExpiredAndCredentialsValid() {
        var user = buildUser();
        var request = buildRequest();
        var expiredAttempt = buildAttempt(MAX_LOGIN_ATTEMPTS_COUNT,
            LocalDateTime.now().minus(BLOCK_DURATION).minusMinutes(1));
        doReturn(user).when(userService).getByUsername(USERNAME);
        doReturn(Optional.of(expiredAttempt)).when(loginAttemptRepository).findByUserUid(USER_UID);
        doReturn(true).when(passwordService).checkPassword(PASSWORD, HASHED_PASSWORD);
        doReturn(TOKEN).when(jwtService).generateToken(USERNAME);
        doReturn(EXPIRATION).when(jwtProperties).getExpiration();

        testObject.login(request);

        verify(loginAttemptRepository).deleteByUserUid(USER_UID);
    }

    @Test
    void login_shouldThrowInvalidCredentials_andSaveAttempt_whenNoExistingAttempt() {
        var user = buildUser();
        var request = buildRequest();
        doReturn(user).when(userService).getByUsername(USERNAME);
        doReturn(Optional.empty()).when(loginAttemptRepository).findByUserUid(USER_UID);
        doReturn(false).when(passwordService).checkPassword(PASSWORD, HASHED_PASSWORD);

        assertThrows(InvalidCredentialsException.class, () -> testObject.login(request));

        verify(loginAttemptRepository).save(attemptCaptor.capture());
        var saved = attemptCaptor.getValue();
        assertEquals(USER_UID, saved.getUserUid());
        assertEquals(1, saved.getFailedAttempts());
        assertNotNull(saved.getLastFailedAt());
    }

    @Test
    void login_shouldThrowInvalidCredentials_andIncrementAttempts_whenAttemptExists() {
        var user = buildUser();
        var request = buildRequest();
        var existingAttempt = buildAttempt(1, LocalDateTime.now().minusMinutes(1));
        doReturn(user).when(userService).getByUsername(USERNAME);
        doReturn(Optional.of(existingAttempt)).when(loginAttemptRepository).findByUserUid(USER_UID);
        doReturn(false).when(passwordService).checkPassword(PASSWORD, HASHED_PASSWORD);

        assertThrows(InvalidCredentialsException.class, () -> testObject.login(request));

        verify(loginAttemptRepository).save(attemptCaptor.capture());
        assertEquals(2, attemptCaptor.getValue().getFailedAttempts());
    }

    @Test
    void login_shouldThrowInvalidCredentials_andResetCounter_whenBlockExpiredAndWrongPassword() {
        var user = buildUser();
        var request = buildRequest();
        var expiredAttempt = buildAttempt(MAX_LOGIN_ATTEMPTS_COUNT,
            LocalDateTime.now().minus(BLOCK_DURATION).minusMinutes(1));
        doReturn(user).when(userService).getByUsername(USERNAME);
        doReturn(Optional.of(expiredAttempt)).when(loginAttemptRepository).findByUserUid(USER_UID);
        doReturn(false).when(passwordService).checkPassword(PASSWORD, HASHED_PASSWORD);

        assertThrows(InvalidCredentialsException.class, () -> testObject.login(request));

        verify(loginAttemptRepository).save(attemptCaptor.capture());
        assertEquals(1, attemptCaptor.getValue().getFailedAttempts());
    }


    @Test
    void login_shouldThrowAccountBlocked_whenUserIsBlocked() {
        var user = buildUser();
        var request = buildRequest();
        var blockedAttempt = buildAttempt(MAX_LOGIN_ATTEMPTS_COUNT, LocalDateTime.now().minusMinutes(1));
        doReturn(user).when(userService).getByUsername(USERNAME);
        doReturn(Optional.of(blockedAttempt)).when(loginAttemptRepository).findByUserUid(USER_UID);

        assertThrows(AccountTemporarilyBlockedException.class, () -> testObject.login(request));
    }

    @ParameterizedTest
    @NullSource
    void login_shouldThrowException_whenArgumentNull(LoginRequest request) {
        assertThrows(NullPointerException.class, () -> testObject.login(request));
    }

    private static User buildUser() {
        return User.builder()
            .uid(USER_UID)
            .username(USERNAME)
            .password(HASHED_PASSWORD)
            .build();
    }

    private static LoginRequest buildRequest() {
        return new LoginRequest(USERNAME, PASSWORD);
    }

    private static LoginAttempt buildAttempt(int failedAttempts, LocalDateTime lastFailedAt) {
        return LoginAttempt.builder()
            .userUid(USER_UID)
            .failedAttempts(failedAttempts)
            .lastFailedAt(lastFailedAt)
            .build();
    }
}
