package com.epam.gym.facade.auth;

import com.epam.gym.controller.rest.auth.dto.request.ChangePasswordRequest;
import com.epam.gym.controller.rest.auth.dto.request.LoginRequest;
import com.epam.gym.controller.rest.auth.dto.request.RegisterTraineeRequest;
import com.epam.gym.controller.rest.auth.dto.request.RegisterTrainerRequest;
import com.epam.gym.controller.rest.auth.dto.response.LoginResponse;
import com.epam.gym.domain.user.Trainee;
import com.epam.gym.domain.user.Trainer;
import com.epam.gym.domain.user.User;
import com.epam.gym.exception.auth.InvalidCredentialsException;
import com.epam.gym.security.JwtProperties;
import com.epam.gym.security.service.IJwtService;
import com.epam.gym.service.auth.IPasswordService;
import com.epam.gym.service.trainee.ITraineeService;
import com.epam.gym.service.trainee.dto.CreateTraineeDto;
import com.epam.gym.service.trainer.ITrainerService;
import com.epam.gym.service.trainer.dto.CreateTrainerDto;
import com.epam.gym.service.user.IUserService;
import com.epam.gym.service.user.dto.ChangePasswordDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.InjectMocks;
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
    private static final String TOKEN = "token";

    @Mock
    private ITraineeService traineeService;
    @Mock
    private ITrainerService trainerService;
    @Mock
    private IUserService userService;
    @Mock
    private IPasswordService passwordService;
    @Mock
    private IJwtService jwtService;
    @Mock
    private JwtProperties jwtProperties;
    @Mock
    private ConversionService conversionService;

    @InjectMocks
    private AuthFacade testObject;

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(
            traineeService,
            trainerService,
            userService,
            passwordService,
            jwtService,
            jwtProperties,
            conversionService);
    }

    @Test
    void registerTrainee_shouldRegisterAndReturnResponse_whenAlways() {
        var request = buildRegisterTraineeRequest();
        var createTraineeDto = buildCreateTraineeDto();
        var trainee = buildTrainee();
        doReturn(createTraineeDto).when(conversionService).convert(request, CreateTraineeDto.class);
        doReturn(trainee).when(traineeService).create(createTraineeDto);

        var result = testObject.registerTrainee(request);

        assertNotNull(result);
        assertEquals(USERNAME, result.username());
        assertEquals(PASSWORD, result.password());
    }

    @ParameterizedTest
    @NullSource
    void registerTrainee_shouldThrowException_whenRequestNull(RegisterTraineeRequest request) {
        assertThrows(NullPointerException.class, () -> testObject.registerTrainee(request));
    }

    @Test
    void registerTrainer_shouldRegisterAndReturnResponse_whenAlways() {
        var request = buildRegisterTrainerRequest();
        var createTrainerDto = buildCreateTrainerDto();
        var trainer = buildTrainer();
        doReturn(createTrainerDto).when(conversionService).convert(request, CreateTrainerDto.class);
        doReturn(trainer).when(trainerService).create(createTrainerDto);

        var result = testObject.registerTrainer(request);

        assertNotNull(result);
        assertEquals(USERNAME, result.username());
        assertEquals(PASSWORD, result.password());
    }

    @ParameterizedTest
    @NullSource
    void registerTrainer_shouldThrowException_whenRequestNull(RegisterTrainerRequest request) {
        assertThrows(NullPointerException.class, () -> testObject.registerTrainer(request));
    }

    @Test
    void login_shouldSucceed_whenCredentialsValid() {
        var request = buildLoginRequest();
        var user = buildUser();
        doReturn(user).when(userService).getByUsername(USERNAME);
        doReturn(true).when(passwordService).checkPassword(PASSWORD, HASHED_PASSWORD);
        doReturn(TOKEN).when(jwtService).generateToken(USERNAME);
        doReturn(100L).when(jwtProperties).getExpiration();

        var result = testObject.login(request);
        assertEquals(TOKEN, result.accessToken());
        assertNotNull(result);

        verify(userService).getByUsername(USERNAME);
        verify(passwordService).checkPassword(PASSWORD, HASHED_PASSWORD);
        verify(jwtService).generateToken(USERNAME);
        verify(jwtProperties).getExpiration();
    }

    @Test
    void login_shouldThrowException_whenPasswordInvalid() {
        var request = buildLoginRequest();
        var user = buildUser();
        doReturn(user).when(userService).getByUsername(USERNAME);
        doReturn(false).when(passwordService).checkPassword(PASSWORD, HASHED_PASSWORD);

        assertThrows(InvalidCredentialsException.class, () -> testObject.login(request));
    }

    @ParameterizedTest
    @NullSource
    void login_shouldThrowException_whenRequestNull(LoginRequest request) {
        assertThrows(NullPointerException.class, () -> testObject.login(request));
    }


    @Test
    void changePassword_shouldCallUserService_whenAlways() {
        var request = buildChangePasswordRequest();
        var changePasswordDto = getChangePasswordDto();
        doReturn(changePasswordDto).when(conversionService).convert(request, ChangePasswordDto.class);

        testObject.changePassword(request);

        verify(userService).changePassword(changePasswordDto);
    }

    @ParameterizedTest
    @NullSource
    void changePassword_shouldThrowException_whenRequestNull(ChangePasswordRequest request) {
        assertThrows(NullPointerException.class, () -> testObject.changePassword(request));
    }

    private static RegisterTraineeRequest buildRegisterTraineeRequest() {
        return RegisterTraineeRequest.builder()
            .firstName(FIRSTNAME)
            .lastName(LASTNAME)
            .birthdate(BIRTHDATE)
            .address(ADDRESS)
            .build();
    }

    private static RegisterTrainerRequest buildRegisterTrainerRequest() {
        return RegisterTrainerRequest.builder()
            .firstName(FIRSTNAME)
            .lastName(LASTNAME)
            .specialization(SPECIALIZATION)
            .build();
    }

    private static LoginRequest buildLoginRequest() {
        return LoginRequest.builder()
            .username(USERNAME)
            .password(PASSWORD)
            .build();
    }

    private static LoginResponse buildLoginResponse() {
        return LoginResponse.of(TOKEN, 100L);
    }

    private static ChangePasswordRequest buildChangePasswordRequest() {
        return ChangePasswordRequest.builder()
            .username(USERNAME)
            .oldPassword(OLD_PASSWORD)
            .newPassword(NEW_PASSWORD)
            .build();
    }

    private static CreateTraineeDto buildCreateTraineeDto() {
        return CreateTraineeDto.builder()
            .firstName(FIRSTNAME)
            .lastName(LASTNAME)
            .birthdate(BIRTHDATE)
            .address(ADDRESS)
            .build();
    }

    private static CreateTrainerDto buildCreateTrainerDto() {
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

    private static Trainee buildTrainee() {
        return Trainee.builder()
            .uid(UUID.randomUUID())
            .username(USERNAME)
            .password(PASSWORD)
            .build();
    }

    private static Trainer buildTrainer() {
        return Trainer.builder()
            .uid(UUID.randomUUID())
            .username(USERNAME)
            .password(PASSWORD)
            .build();
    }

    private static User buildUser() {
        return User.builder()
            .uid(UUID.randomUUID())
            .username(USERNAME)
            .password(HASHED_PASSWORD)
            .build();
    }
}
