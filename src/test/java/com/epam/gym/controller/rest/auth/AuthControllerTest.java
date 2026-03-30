package com.epam.gym.controller.rest.auth;

import com.epam.gym.controller.rest.auth.dto.request.ChangePasswordRequest;
import com.epam.gym.controller.rest.auth.dto.request.LoginRequest;
import com.epam.gym.controller.rest.auth.dto.request.RegisterTraineeRequest;
import com.epam.gym.controller.rest.auth.dto.request.RegisterTrainerRequest;
import com.epam.gym.controller.rest.auth.dto.response.LoginResponse;
import com.epam.gym.controller.rest.auth.dto.response.RegistrationResponse;
import com.epam.gym.facade.auth.IAuthFacade;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    private static final LocalDate DATE = LocalDate.of(2000, 1, 1);
    private static final String FIRST_NAME = "firstname";
    private static final String LAST_NAME = "last_name";
    private static final String ADDRESS = "address";
    private static final String SPECIALIZATION_NAME = "specialization_name";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String TOKEN = "token";
    private static final String BEARER = "Bearer";

    @Mock
    private IAuthFacade authFacade;

    @InjectMocks
    private AuthController testObject;

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(authFacade);
    }

    @Test
    void registerTrainee() {
        var request = buildRegisterTraineeRequest();
        var expected = buildRegistrationResponse();
        doReturn(expected).when(authFacade).registerTrainee(request);

        var actual = testObject.registerTrainee(request);

        assertEquals(expected, actual);
        verify(authFacade).registerTrainee(request);
    }

    @Test
    void registerTrainer() {
        var request = buildRegisterTrainerRequest();
        var expected = buildRegistrationResponse();
        doReturn(expected).when(authFacade).registerTrainer(request);

        var actual = testObject.registerTrainer(request);

        assertEquals(expected, actual);
        verify(authFacade).registerTrainer(request);
    }

    @Test
    void login() {
        var request = buildLoginRequest();
        var expected = buildLoginResponse();
        doReturn(expected).when(authFacade).login(request);

        var actual = testObject.login(request);

        assertEquals(expected, actual);
        verify(authFacade).login(request);
    }

    @Test
    void logout() {
        assertDoesNotThrow(() -> testObject.logout());
    }

    @Test
    void changePassword() {
        var request = buildChangePasswordRequest();

        testObject.changePassword(request);

        verify(authFacade).changePassword(request);
    }

    private static RegisterTraineeRequest buildRegisterTraineeRequest() {
        return RegisterTraineeRequest.builder()
            .firstName(FIRST_NAME)
            .lastName(LAST_NAME)
            .address(ADDRESS)
            .birthdate(DATE)
            .build();
    }

    private static RegisterTrainerRequest buildRegisterTrainerRequest() {
        return RegisterTrainerRequest.builder()
            .firstName(FIRST_NAME)
            .lastName(LAST_NAME)
            .specialization(SPECIALIZATION_NAME)
            .build();
    }

    private static RegistrationResponse buildRegistrationResponse() {
        return RegistrationResponse.builder()
            .username(USERNAME)
            .password(PASSWORD)
            .build();
    }

    private static LoginRequest buildLoginRequest() {
        return LoginRequest.builder()
            .username(USERNAME)
            .password(PASSWORD)
            .build();
    }

    private static LoginResponse buildLoginResponse() {
        return LoginResponse.builder()
            .expiresIn(100L)
            .accessToken(TOKEN)
            .tokenType(BEARER)
            .build();
    }

    private static ChangePasswordRequest buildChangePasswordRequest() {
        return ChangePasswordRequest.builder()
            .username(USERNAME)
            .newPassword(PASSWORD)
            .oldPassword(PASSWORD)
            .build();
    }
}
