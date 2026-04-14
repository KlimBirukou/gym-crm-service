package com.epam.gym.crm.service.auth.login;

import com.epam.gym.crm.client.auth.BruteForceStatusResponse;
import com.epam.gym.crm.client.auth.GenerateTokenRequest;
import com.epam.gym.crm.client.auth.IAuthClient;
import com.epam.gym.crm.controller.rest.auth.dto.request.LoginRequest;
import com.epam.gym.crm.controller.rest.auth.dto.response.LoginResponse;
import com.epam.gym.crm.domain.user.User;
import com.epam.gym.crm.exception.auth.AccountTemporarilyBlockedException;
import com.epam.gym.crm.exception.auth.InvalidCredentialsException;
import com.epam.gym.crm.service.auth.password.IPasswordService;
import com.epam.gym.crm.service.user.IUserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class BearerLoginServiceTest {

    private static final UUID USER_UID = UUID.fromString("a1b2c3d4-e5f6-7890-abcd-ef1234567890");
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String TOKEN = "type";
    private static final String TOKEN_TYPE = "token_type";
    private static final String HASHED_PASSWORD = "hashed_password";
    private static final long MINUTES_LEFT = 10L;

    @Mock
    private IUserService userService;
    @Mock
    private IPasswordService passwordService;
    @Mock
    private IAuthClient authClient;

    @InjectMocks
    private BearerLoginService testObject;

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(userService, passwordService, authClient);
    }

    @Test
    void login_shouldReturnLoginResponse_whenCredentialsAreValidAndUserIsNotBlocked() {
        var user = buildUser();
        var request = buildRequest();
        var tokenRequest = buildTokenRequest();
        var loginResponse = buildLoginResponse();
        doReturn(user).when(userService).getByUsername(USERNAME);
        doReturn(BruteForceStatusResponse.notBlocked()).when(authClient).getBruteForceStatus(USER_UID);
        doReturn(true).when(passwordService).checkPassword(PASSWORD, HASHED_PASSWORD);
        doReturn(loginResponse).when(authClient).generateToken(tokenRequest);

        var result = testObject.login(request);

        assertNotNull(result);
        assertEquals(loginResponse, result);
        verify(userService).getByUsername(USERNAME);
        verify(authClient).getBruteForceStatus(USER_UID);
        verify(passwordService).checkPassword(PASSWORD, HASHED_PASSWORD);
        verify(authClient).generateToken(tokenRequest);
    }

    @Test
    void login_shouldThrowAccountTemporarilyBlockedException_whenUserIsBlocked() {
        var user = buildUser();
        var request = buildRequest();
        doReturn(user).when(userService).getByUsername(USERNAME);
        doReturn(BruteForceStatusResponse.blocked(MINUTES_LEFT)).when(authClient).getBruteForceStatus(USER_UID);

        assertThrows(AccountTemporarilyBlockedException.class, () -> testObject.login(request));

        verify(userService).getByUsername(USERNAME);
        verify(authClient).getBruteForceStatus(USER_UID);
    }

    @Test
    void login_shouldThrowInvalidCredentialsExceptionAndRecordFailedAttempt_whenPasswordIsWrong() {
        var user = buildUser();
        var request = buildRequest();
        doReturn(user).when(userService).getByUsername(USERNAME);
        doReturn(BruteForceStatusResponse.notBlocked()).when(authClient).getBruteForceStatus(USER_UID);
        doReturn(false).when(passwordService).checkPassword(PASSWORD, HASHED_PASSWORD);
        doNothing().when(authClient).recordFailedAttempt(USER_UID);

        assertThrows(InvalidCredentialsException.class, () -> testObject.login(request));

        verify(userService).getByUsername(USERNAME);
        verify(authClient).getBruteForceStatus(USER_UID);
        verify(passwordService).checkPassword(PASSWORD, HASHED_PASSWORD);
        verify(authClient).recordFailedAttempt(USER_UID);
    }

    @ParameterizedTest
    @NullSource
    void login_shouldThrowNullPointerException_whenRequestIsNull(LoginRequest request) {
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

    private static GenerateTokenRequest buildTokenRequest() {
        return GenerateTokenRequest.builder()
            .userUid(USER_UID)
            .username(USERNAME)
            .build();
    }

    private static LoginResponse buildLoginResponse() {
        return LoginResponse.builder()
            .accessToken(TOKEN)
            .tokenType(TOKEN_TYPE)
            .expiresIn(MINUTES_LEFT)
            .build();
    }
}
