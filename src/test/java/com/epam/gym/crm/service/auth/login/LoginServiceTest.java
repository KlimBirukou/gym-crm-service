package com.epam.gym.crm.service.auth.login;

import com.epam.gym.crm.controller.rest.auth.dto.request.LoginRequest;
import com.epam.gym.crm.domain.auth.LoginAttempt;
import com.epam.gym.crm.domain.user.User;
import com.epam.gym.crm.exception.auth.AccountTemporarilyBlockedException;
import com.epam.gym.crm.exception.auth.InvalidCredentialsException;
import com.epam.gym.repository.domain.auth.ILoginAttemptRepository;
import com.epam.gym.service.auth.jwt.JwtService;
import com.epam.gym.crm.service.auth.password.IPasswordService;
import com.epam.gym.crm.service.user.IUserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {

    private static final int ATTEMPTS_COUNT = 3;
    private static final Duration BLOCK_DURATION = Duration.ofMinutes(15);
    private static final UUID USER_UID = UUID.randomUUID();
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String HASHED_PASSWORD = "hashed_password";
    private static final String TOKEN = "token";
    private static final long JWT_EXPIRATION = 3600L;
    public static final String BEARER = "Bearer";
    public static final String SECRET = "Secret";

    @Mock
    private ILoginAttemptRepository loginAttemptRepository;
    @Mock
    private IUserService userService;
    @Mock
    private IPasswordService passwordService;
    @Mock
    private JwtService jwtService;
    @Mock
    private AuthProperties authProperties;

    @Captor
    private ArgumentCaptor<LoginAttempt> attemptCaptor;

    @InjectMocks
    private OldBearerLoginService testObject;

    @BeforeEach
    void setUp() {
        lenient().when(authProperties.maxLoginAttempts()).thenReturn(ATTEMPTS_COUNT);
        lenient().when(authProperties.blockDuration()).thenReturn(BLOCK_DURATION);
        lenient().when(authProperties.jwtExpiration()).thenReturn(JWT_EXPIRATION);
    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(
            loginAttemptRepository,
            userService,
            passwordService,
            jwtService
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
        doReturn(JWT_EXPIRATION).when(authProperties).jwtExpiration();

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

        testObject.login(request);

        verify(loginAttemptRepository).deleteByUserUid(USER_UID);
    }

    @Test
    void login_shouldReturnToken_whenBlockExpiredAndCredentialsValid() {
        var user = buildUser();
        var request = buildRequest();
        var expiredAttempt = buildAttempt(ATTEMPTS_COUNT,
            LocalDateTime.now().minus(BLOCK_DURATION).minusMinutes(1));
        doReturn(user).when(userService).getByUsername(USERNAME);
        doReturn(Optional.of(expiredAttempt)).when(loginAttemptRepository).findByUserUid(USER_UID);
        doReturn(true).when(passwordService).checkPassword(PASSWORD, HASHED_PASSWORD);
        doReturn(TOKEN).when(jwtService).generateToken(USERNAME);

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
        var expiredAttempt = buildAttempt(ATTEMPTS_COUNT,
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
        var blockedAttempt = buildAttempt(ATTEMPTS_COUNT, LocalDateTime.now().minusMinutes(1));
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
