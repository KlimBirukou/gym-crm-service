package com.epam.gym.facade.auth;

import com.epam.gym.controller.rest.auth.dto.request.ChangePasswordRequest;
import com.epam.gym.controller.rest.auth.dto.request.LoginRequest;
import com.epam.gym.controller.rest.auth.dto.request.RegisterTraineeRequest;
import com.epam.gym.controller.rest.auth.dto.request.RegisterTrainerRequest;
import com.epam.gym.domain.user.Trainee;
import com.epam.gym.domain.user.Trainer;
import com.epam.gym.domain.user.User;
import com.epam.gym.exception.AuthException;
import com.epam.gym.service.auth.IPasswordService;
import com.epam.gym.service.trainee.ITraineeService;
import com.epam.gym.service.trainee.dto.CreateTraineeDto;
import com.epam.gym.service.trainer.ITrainerService;
import com.epam.gym.service.trainer.dto.CreateTrainerDto;
import com.epam.gym.service.user.IUserService;
import com.epam.gym.service.user.dto.ChangePasswordDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.convert.ConversionService;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class AuthFacadeTest {

    private static final String FIRSTNAME = "firstname";
    private static final String LASTNAME = "lastname";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String HASHED_PASSWORD = "hashed_password";
    private static final String OLD_PASSWORD = "old_password";
    private static final String NEW_PASSWORD = "new_password";
    private static final String SPECIALIZATION = "specialization";
    private static final LocalDate BIRTHDATE = LocalDate.of(2000, 1, 1);
    private static final String ADDRESS = "address";

    @Mock
    private ITraineeService traineeService;
    @Mock
    private ITrainerService trainerService;
    @Mock
    private IUserService userService;
    @Mock
    private IPasswordService passwordService;
    @Mock
    private ConversionService conversionService;

    private AuthFacade testObject;

    @BeforeEach
    void setUp() {
        testObject = new AuthFacade(
            traineeService,
            trainerService,
            userService,
            passwordService,
            conversionService
        );
    }

    @Test
    void registerTrainee_shouldRegisterAndReturnResponse_whenAlways() {
        var request = getRegisterTraineeRequest();
        var createTraineeDto = getCreateTraineeDto();
        var trainee = getTrainee();
        doReturn(createTraineeDto).when(conversionService).convert(request, CreateTraineeDto.class);
        doReturn(trainee).when(traineeService).create(createTraineeDto);

        var result = testObject.registerTrainee(request);

        assertNotNull(result);
        assertEquals(USERNAME, result.username());
        assertEquals(PASSWORD, result.password());

        assertNoUnexpectedInteractions();
    }

    @ParameterizedTest
    @NullSource
    void registerTrainee_shouldThrowException_whenRequestNull(RegisterTraineeRequest request) {
        assertThrows(NullPointerException.class, () -> testObject.registerTrainee(request));

        assertNoUnexpectedInteractions();
    }


    @Test
    void registerTrainer_shouldRegisterAndReturnResponse_whenAlways() {
        var request = getRegisterTrainerRequest();
        var createTrainerDto = getCreateTrainerDto();
        var trainer = getTrainer();
        doReturn(createTrainerDto).when(conversionService).convert(request, CreateTrainerDto.class);
        doReturn(trainer).when(trainerService).create(createTrainerDto);

        var result = testObject.registerTrainer(request);

        assertNotNull(result);
        assertEquals(USERNAME, result.username());
        assertEquals(PASSWORD, result.password());

        assertNoUnexpectedInteractions();
    }

    @ParameterizedTest
    @NullSource
    void registerTrainer_shouldThrowException_whenRequestNull(RegisterTrainerRequest request) {
        assertThrows(NullPointerException.class, () -> testObject.registerTrainer(request));

        assertNoUnexpectedInteractions();
    }


    @Test
    void login_shouldSucceed_whenCredentialsValid() {
        var request = getLoginRequest();
        var user = getUser();
        doReturn(user).when(userService).getByUsername(USERNAME);
        doReturn(true).when(passwordService).checkPassword(PASSWORD, HASHED_PASSWORD);

        testObject.login(request);

        assertNoUnexpectedInteractions();
    }

    @Test
    void login_shouldThrowException_whenPasswordInvalid() {
        var request = getLoginRequest();
        var user = getUser();
        doReturn(user).when(userService).getByUsername(USERNAME);
        doReturn(false).when(passwordService).checkPassword(PASSWORD, HASHED_PASSWORD);

        assertThrows(AuthException.class, () -> testObject.login(request));

        assertNoUnexpectedInteractions();
    }

    @ParameterizedTest
    @NullSource
    void login_shouldThrowException_whenRequestNull(LoginRequest request) {
        assertThrows(NullPointerException.class, () -> testObject.login(request));

        assertNoUnexpectedInteractions();
    }


    @Test
    void changePassword_shouldCallUserService_whenAlways() {
        var request = getChangePasswordRequest();
        var changePasswordDto = getChangePasswordDto();
        doReturn(changePasswordDto).when(conversionService).convert(request, ChangePasswordDto.class);

        testObject.changePassword(request);

        verify(userService).changePassword(changePasswordDto);

        assertNoUnexpectedInteractions();
    }

    @ParameterizedTest
    @NullSource
    void changePassword_shouldThrowException_whenRequestNull(ChangePasswordRequest request) {
        assertThrows(NullPointerException.class, () -> testObject.changePassword(request));

        assertNoUnexpectedInteractions();
    }

    private static RegisterTraineeRequest getRegisterTraineeRequest() {
        return RegisterTraineeRequest.builder()
            .firstName(FIRSTNAME)
            .lastName(LASTNAME)
            .birthdate(BIRTHDATE)
            .address(ADDRESS)
            .build();
    }

    private static RegisterTrainerRequest getRegisterTrainerRequest() {
        return RegisterTrainerRequest.builder()
            .firstName(FIRSTNAME)
            .lastName(LASTNAME)
            .specialization(SPECIALIZATION)
            .build();
    }

    private static LoginRequest getLoginRequest() {
        return LoginRequest.builder()
            .username(USERNAME)
            .password(PASSWORD)
            .build();
    }

    private static ChangePasswordRequest getChangePasswordRequest() {
        return ChangePasswordRequest.builder()
            .username(USERNAME)
            .oldPassword(OLD_PASSWORD)
            .newPassword(NEW_PASSWORD)
            .build();
    }

    private static CreateTraineeDto getCreateTraineeDto() {
        return CreateTraineeDto.builder()
            .firstName(FIRSTNAME)
            .lastName(LASTNAME)
            .birthdate(BIRTHDATE)
            .address(ADDRESS)
            .build();
    }

    private static CreateTrainerDto getCreateTrainerDto() {
        return CreateTrainerDto.builder()
            .firstName(FIRSTNAME)
            .lastName(LASTNAME)
            .specialization(SPECIALIZATION)
            .build();
    }

    private static ChangePasswordDto getChangePasswordDto() {
        return ChangePasswordDto.builder()
            .username(USERNAME)
            .oldPassword(OLD_PASSWORD)
            .newPassword(NEW_PASSWORD)
            .build();
    }

    private static Trainee getTrainee() {
        return Trainee.builder()
            .uid(UUID.randomUUID())
            .username(USERNAME)
            .password(PASSWORD)
            .build();
    }

    private static Trainer getTrainer() {
        return Trainer.builder()
            .uid(UUID.randomUUID())
            .username(USERNAME)
            .password(PASSWORD)
            .build();
    }

    private static User getUser() {
        return User.builder()
            .uid(UUID.randomUUID())
            .username(USERNAME)
            .password(HASHED_PASSWORD)
            .build();
    }

    private void assertNoUnexpectedInteractions() {
        verifyNoMoreInteractions(
            traineeService,
            trainerService,
            userService,
            passwordService,
            conversionService
        );
    }
}
