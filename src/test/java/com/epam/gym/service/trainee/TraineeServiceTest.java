package com.epam.gym.service.trainee;

import com.epam.gym.domain.user.Trainee;
import com.epam.gym.exception.AuthException;
import com.epam.gym.exception.not.found.TraineeNotFoundException;
import com.epam.gym.repository.domain.trainee.ITraineeRepository;
import com.epam.gym.service.auth.IPasswordService;
import com.epam.gym.service.generator.name.IUsernameGenerator;
import com.epam.gym.service.generator.password.IPasswordGenerator;
import com.epam.gym.service.trainee.dto.CreateTraineeDto;
import com.epam.gym.service.trainee.dto.UpdateTraineeDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
/*

@ExtendWith(MockitoExtension.class)
class TraineeServiceTest {

    private static final LocalDate DATE = LocalDate.of(2000, 1, 1);
    private static final LocalDate NEW_DATE = LocalDate.of(1995, 6, 15);
    private static final UUID UID = UUID.randomUUID();
    private static final String FIRSTNAME = "firstname";
    private static final String NEW_FIRSTNAME = "new_firstname";
    private static final String LASTNAME = "lastname";
    private static final String NEW_LASTNAME = "new_lastname";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String HASHED_PASSWORD = "hashed_password";
    private static final String NEW_PASSWORD = "new_password";
    private static final String NEW_HASHED_PASSWORD = "new_hashed_password";
    private static final String ADDRESS = "address";
    private static final String NEW_ADDRESS = "new address";

    @Mock
    private IPasswordGenerator passwordGenerator;
    @Mock
    private IUsernameGenerator usernameGenerator;
    @Mock
    private ITraineeRepository traineeRepository;
    @Mock
    private IPasswordService passwordService;

    @Captor
    private ArgumentCaptor<Trainee> traineeCaptor;

    private TraineeService testObject;

    @BeforeEach
    void setUp() {
        testObject = new TraineeService(
            passwordGenerator,
            usernameGenerator,
            traineeRepository,
            passwordService
        );
    }

    @Test
    void create_shouldCreateTrainee() {
        var createTraineeDto = getCreateTraineeDto();
        doReturn(USERNAME).when(usernameGenerator).generate(FIRSTNAME, LASTNAME);
        doReturn(PASSWORD).when(passwordGenerator).generate();
        doReturn(HASHED_PASSWORD).when(passwordService).hashPassword(PASSWORD);

        var result = testObject.create(createTraineeDto);

        verify(traineeRepository).save(traineeCaptor.capture());
        var saved = traineeCaptor.getValue();

        assertSame(result, saved);
        assertNotNull(saved.getUid());
        assertEquals(FIRSTNAME, saved.getFirstName());
        assertEquals(LASTNAME, saved.getLastName());
        assertEquals(DATE, saved.getBirthdate());
        assertEquals(ADDRESS, saved.getAddress());
        assertEquals(USERNAME, saved.getUsername());
        assertEquals(HASHED_PASSWORD, saved.getPassword());
        assertTrue(saved.isActive());

        assertNoUnexpectedInteractions();
    }

    @ParameterizedTest
    @NullSource
    void create_shouldThrowException_whenArgumentNull(CreateTraineeDto dto) {
        assertThrows(NullPointerException.class, () -> testObject.create(dto));

        assertNoUnexpectedInteractions();
    }


    @Test
    void update_shouldUpdateTrainee_whenTraineeExist() {
        var trainee = getTrainee();
        var updateTraineeDto = getUpdateTraineeDto();
        doReturn(Optional.of(trainee)).when(traineeRepository).getByUsername(USERNAME);

        testObject.update(updateTraineeDto);

        verify(traineeRepository).save(traineeCaptor.capture());
        var saved = traineeCaptor.getValue();

        assertEquals(NEW_FIRSTNAME, saved.getFirstName());
        assertEquals(NEW_LASTNAME, saved.getLastName());
        assertEquals(NEW_ADDRESS, saved.getAddress());
        assertEquals(NEW_DATE, saved.getBirthdate());
        assertEquals(UID, saved.getUid());
        assertEquals(USERNAME, saved.getUsername());
        assertEquals(HASHED_PASSWORD, saved.getPassword());
        assertTrue(saved.isActive());

        assertNoUnexpectedInteractions();
    }

    @Test
    void update_shouldThrowException_whenTraineeNotExist() {
        var updateTraineeDto = getUpdateTraineeDto();
        doReturn(Optional.empty()).when(traineeRepository).getByUsername(USERNAME);

        assertThrows(TraineeNotFoundException.class, () -> testObject.update(updateTraineeDto));

        assertNoUnexpectedInteractions();
    }

    @ParameterizedTest
    @NullSource
    void update_shouldThrowException_whenArgumentNull(UpdateTraineeDto dto) {
        assertThrows(NullPointerException.class, () -> testObject.update(dto));

        assertNoUnexpectedInteractions();
    }


    @Test
    void changePassword_shouldUpdatePassword_whenOldPasswordMatch() {
        var trainee = getTrainee();
        var changePasswordDto = getChangePasswordDto();
        doReturn(Optional.of(trainee)).when(traineeRepository).getByUsername(USERNAME);
        doReturn(true).when(passwordService).checkPassword(PASSWORD, HASHED_PASSWORD);
        doReturn(NEW_HASHED_PASSWORD).when(passwordService).hashPassword(NEW_PASSWORD);

        testObject.changePassword(changePasswordDto);

        verify(traineeRepository).save(traineeCaptor.capture());
        var saved = traineeCaptor.getValue();

        assertEquals(NEW_HASHED_PASSWORD, saved.getPassword());
        assertEquals(UID, saved.getUid());
        assertEquals(USERNAME, saved.getUsername());
        assertTrue(saved.isActive());

        assertNoUnexpectedInteractions();
    }

    @Test
    void changePassword_shouldThrowException_whenOldPasswordNotMatch() {
        var trainee = getTrainee();
        var changePasswordDto = getChangePasswordDto();
        doReturn(Optional.of(trainee)).when(traineeRepository).getByUsername(USERNAME);
        doReturn(false).when(passwordService).checkPassword(PASSWORD, HASHED_PASSWORD);

        assertThrows(AuthException.class, () -> testObject.changePassword(changePasswordDto));

        assertNoUnexpectedInteractions();
    }

    @ParameterizedTest
    @NullSource
    void changePassword_shouldThrowException_whenArgumentNull(ChangePasswordDto dto) {
        assertThrows(NullPointerException.class, () -> testObject.changePassword(dto));

        assertNoUnexpectedInteractions();
    }


    @Test
    void toggleStatus_shouldDeactivate_whenTraineeIsActive() {
        var trainee = getTrainee();
        doReturn(Optional.of(trainee)).when(traineeRepository).getByUsername(USERNAME);

        testObject.changeStatus(USERNAME);

        verify(traineeRepository).save(traineeCaptor.capture());
        assertFalse(traineeCaptor.getValue().isActive());

        assertNoUnexpectedInteractions();
    }

    @Test
    void toggleStatus_shouldActivate_whenTraineeIsInactive() {
        var inactiveTrainee = Trainee.builder()
            .uid(UID)
            .username(USERNAME)
            .active(false)
            .build();
        doReturn(Optional.of(inactiveTrainee)).when(traineeRepository).getByUsername(USERNAME);

        testObject.changeStatus(USERNAME);

        verify(traineeRepository).save(traineeCaptor.capture());
        assertTrue(traineeCaptor.getValue().isActive());

        assertNoUnexpectedInteractions();
    }

    @ParameterizedTest
    @NullSource
    void toggleStatus_shouldThrowNullPointerException_whenArgumentNull(String username) {
        assertThrows(NullPointerException.class, () -> testObject.changeStatus(username));

        assertNoUnexpectedInteractions();
    }


    @Test
    void delete_shouldDeleteTrainee() {
        testObject.delete(USERNAME);

        verify(traineeRepository).deleteByUsername(USERNAME);
        assertNoUnexpectedInteractions();
    }

    @ParameterizedTest
    @NullSource
    void delete_shouldThrowException_whenArgumentNull(String username) {
        assertThrows(NullPointerException.class, () -> testObject.delete(username));

        assertNoUnexpectedInteractions();
    }


    @Test
    void getByUsername_shouldReturnTrainee_whenTraineeExists() {
        var trainee = getTrainee();
        doReturn(Optional.of(trainee)).when(traineeRepository).getByUsername(USERNAME);

        var result = testObject.getByUsername(USERNAME);

        assertSame(trainee, result);
        assertNoUnexpectedInteractions();
    }

    @Test
    void getByUsername_shouldThrowException_whenTraineeNotExists() {
        doReturn(Optional.empty()).when(traineeRepository).getByUsername(USERNAME);

        assertThrows(TraineeNotFoundException.class, () -> testObject.getByUsername(USERNAME));

        assertNoUnexpectedInteractions();
    }

    @ParameterizedTest
    @NullSource
    void getByUsername_shouldThrowException_whenArgumentNull(String username) {
        assertThrows(NullPointerException.class, () -> testObject.getByUsername(username));

        assertNoUnexpectedInteractions();
    }

    private static CreateTraineeDto getCreateTraineeDto() {
        return new CreateTraineeDto(
            FIRSTNAME, LASTNAME, DATE, ADDRESS
        );
    }

    private static UpdateTraineeDto getUpdateTraineeDto() {
        return new UpdateTraineeDto(
            USERNAME, NEW_FIRSTNAME, NEW_LASTNAME, NEW_DATE, NEW_ADDRESS
        );
    }

    private static ChangePasswordDto getChangePasswordDto() {
        return new ChangePasswordDto(
            USERNAME, PASSWORD, NEW_PASSWORD
        );
    }

    private static Trainee getTrainee() {
        return Trainee.builder()
            .uid(UID)
            .firstName(FIRSTNAME)
            .lastName(LASTNAME)
            .username(USERNAME)
            .password(HASHED_PASSWORD)
            .birthdate(DATE)
            .address(ADDRESS)
            .active(true)
            .build();
    }

    private void assertNoUnexpectedInteractions() {
        verifyNoMoreInteractions(
            passwordGenerator,
            usernameGenerator,
            traineeRepository,
            passwordService
        );
    }
}
*/
