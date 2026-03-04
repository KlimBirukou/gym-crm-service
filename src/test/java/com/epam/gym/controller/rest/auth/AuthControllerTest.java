package com.epam.gym.controller.rest.auth;

import com.epam.gym.controller.rest.auth.dto.request.ChangePasswordRequest;
import com.epam.gym.controller.rest.auth.dto.request.LoginRequest;
import com.epam.gym.controller.rest.auth.dto.request.RegisterTraineeRequest;
import com.epam.gym.controller.rest.auth.dto.request.RegisterTrainerRequest;
import com.epam.gym.controller.rest.auth.dto.response.RegistrationResponse;
import com.epam.gym.facade.auth.IAuthFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private IAuthFacade authFacade;

    private AuthController testObject;

    @BeforeEach
    void setUp() {
        testObject = new AuthController(authFacade);
    }

    @Test
    void registerTrainee() {
        var request = mock(RegisterTraineeRequest.class);
        var expected = mock(RegistrationResponse.class);
        doReturn(expected).when(authFacade).registerTrainee(request);

        var actual = testObject.registerTrainee(request);

        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(authFacade).registerTrainee(request);
    }

    @Test
    void registerTrainer() {
        var request = mock(RegisterTrainerRequest.class);
        var expected = mock(RegistrationResponse.class);
        doReturn(expected).when(authFacade).registerTrainer(request);

        var actual = testObject.registerTrainer(request);

        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(authFacade).registerTrainer(request);
    }

    @Test
    void login() {
        var request = mock(LoginRequest.class);

        testObject.login(request);

        verify(authFacade).login(request);
    }

    @Test
    void changePassword() {
        var request = mock(ChangePasswordRequest.class);

        testObject.changePassword(request);

        verify(authFacade).changePassword(request);
    }
}
