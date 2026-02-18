package com.epam.gym.service.trainer;

import com.epam.gym.domain.training.TrainingType;
import com.epam.gym.domain.user.Trainer;
import com.epam.gym.exception.AuthException;
import com.epam.gym.exception.not.found.TrainerNotFoundException;
import com.epam.gym.repository.domain.trainer.ITrainerRepository;
import com.epam.gym.service.auth.IPasswordService;
import com.epam.gym.service.generator.name.IUsernameGenerator;
import com.epam.gym.service.generator.password.IPasswordGenerator;
import com.epam.gym.service.trainer.dto.ChangePasswordDto;
import com.epam.gym.service.trainer.dto.CreateTrainerDto;
import com.epam.gym.service.trainer.dto.UpdateTrainerDto;
import com.epam.gym.service.type.ITrainingTypeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

@ExtendWith(MockitoExtension.class)
class TrainerServiceTest {

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
    private static final String SPECIALIZATION_NAME = "specialization_name";

    private static final CreateTrainerDto CREATE_DTO = new CreateTrainerDto(
        FIRSTNAME, LASTNAME, SPECIALIZATION_NAME
    );
    private static final UpdateTrainerDto UPDATE_DTO = new UpdateTrainerDto(
        USERNAME, NEW_FIRSTNAME, NEW_LASTNAME
    );
    private static final ChangePasswordDto CHANGE_PASSWORD_DTO = new ChangePasswordDto(
        USERNAME, PASSWORD, NEW_PASSWORD
    );
    private static final TrainingType TRAINING_TYPE = TrainingType.builder()
        .uid(UID)
        .name(SPECIALIZATION_NAME)
        .build();
    private static final Trainer EXISTED_TRAINER = Trainer.builder()
        .uid(UID)
        .firstName(FIRSTNAME)
        .lastName(LASTNAME)
        .username(USERNAME)
        .password(HASHED_PASSWORD)
        .specialization(TRAINING_TYPE)
        .active(true)
        .build();

    @Mock
    private IUsernameGenerator usernameGenerator;
    @Mock
    private IPasswordGenerator passwordGenerator;
    @Mock
    private ITrainerRepository trainerRepository;
    @Mock
    private ITrainingTypeService trainingTypeService;
    @Mock
    private IPasswordService passwordService;

    @Captor
    private ArgumentCaptor<Trainer> trainerCaptor;

    private TrainerService testObject;

    @BeforeEach
    void setUp() {
        testObject = new TrainerService(
            passwordGenerator,
            usernameGenerator,
            trainerRepository,
            trainingTypeService,
            passwordService
        );
    }

    @Test
    void create_shouldCreateTrainer_whenAlways() {
        doReturn(USERNAME).when(usernameGenerator).generate(FIRSTNAME, LASTNAME);
        doReturn(PASSWORD).when(passwordGenerator).generate();
        doReturn(TRAINING_TYPE).when(trainingTypeService).getByName(SPECIALIZATION_NAME);
        doReturn(HASHED_PASSWORD).when(passwordService).hashPassword(PASSWORD);

        var result = testObject.create(CREATE_DTO);

        verify(trainerRepository).save(trainerCaptor.capture());
        var saved = trainerCaptor.getValue();

        assertSame(result, saved);
        assertNotNull(saved.getUid());
        assertEquals(FIRSTNAME, saved.getFirstName());
        assertEquals(LASTNAME, saved.getLastName());
        assertEquals(TRAINING_TYPE, saved.getSpecialization());
        assertEquals(USERNAME, saved.getUsername());
        assertEquals(HASHED_PASSWORD, saved.getPassword());
        assertTrue(saved.isActive());

        assertNoUnexpectedInteractions();
    }

    @ParameterizedTest
    @NullSource
    void create_shouldThrowException_whenArgumentNull(CreateTrainerDto dto) {
        assertThrows(NullPointerException.class, () -> testObject.create(dto));

        assertNoUnexpectedInteractions();
    }


    @Test
    void update_shouldUpdateTrainer_whenTrainerExist() {
        doReturn(Optional.of(EXISTED_TRAINER)).when(trainerRepository).getByUsername(USERNAME);

        testObject.update(UPDATE_DTO);

        verify(trainerRepository).save(trainerCaptor.capture());
        var saved = trainerCaptor.getValue();

        assertEquals(NEW_FIRSTNAME, saved.getFirstName());
        assertEquals(NEW_LASTNAME, saved.getLastName());
        assertEquals(TRAINING_TYPE, saved.getSpecialization());
        assertEquals(UID, saved.getUid());
        assertEquals(USERNAME, saved.getUsername());
        assertEquals(HASHED_PASSWORD, saved.getPassword());
        assertTrue(saved.isActive());

        assertNoUnexpectedInteractions();
    }

    @Test
    void update_shouldThrowException_whenTrainerNotExist() {
        doReturn(Optional.empty()).when(trainerRepository).getByUsername(USERNAME);

        assertThrows(TrainerNotFoundException.class, () -> testObject.update(UPDATE_DTO));

        assertNoUnexpectedInteractions();
    }

    @ParameterizedTest
    @NullSource
    void update_shouldThrowException_whenDataNull(UpdateTrainerDto dto) {
        assertThrows(NullPointerException.class, () -> testObject.update(dto));

        assertNoUnexpectedInteractions();
    }


    @Test
    void changePassword_shouldUpdatePassword_whenOldPasswordMatch() {
        doReturn(Optional.of(EXISTED_TRAINER)).when(trainerRepository).getByUsername(USERNAME);
        doReturn(true).when(passwordService).checkPassword(PASSWORD, HASHED_PASSWORD);
        doReturn(NEW_HASHED_PASSWORD).when(passwordService).hashPassword(NEW_PASSWORD);

        testObject.changePassword(CHANGE_PASSWORD_DTO);

        verify(trainerRepository).save(trainerCaptor.capture());
        var saved = trainerCaptor.getValue();

        assertEquals(NEW_HASHED_PASSWORD, saved.getPassword());
        assertEquals(UID, saved.getUid());
        assertEquals(USERNAME, saved.getUsername());
        assertTrue(saved.isActive());

        assertNoUnexpectedInteractions();
    }

    @Test
    void changePassword_shouldThrowException_whenOldPasswordNotMatch() {
        doReturn(Optional.of(EXISTED_TRAINER)).when(trainerRepository).getByUsername(USERNAME);
        doReturn(false).when(passwordService).checkPassword(PASSWORD, HASHED_PASSWORD);

        assertThrows(AuthException.class, () -> testObject.changePassword(CHANGE_PASSWORD_DTO));

        assertNoUnexpectedInteractions();
    }

    @ParameterizedTest
    @NullSource
    void changePassword_shouldThrowException_whenArgumentNull(ChangePasswordDto dto) {
        assertThrows(NullPointerException.class, () -> testObject.changePassword(dto));

        assertNoUnexpectedInteractions();
    }


    @Test
    void toggleStatus_shouldDeactivate_whenTrainerIsActive() {
        doReturn(Optional.of(EXISTED_TRAINER)).when(trainerRepository).getByUsername(USERNAME);

        testObject.toggleStatus(USERNAME);

        verify(trainerRepository).save(trainerCaptor.capture());
        assertFalse(trainerCaptor.getValue().isActive());

        assertNoUnexpectedInteractions();
    }

    @Test
    void toggleStatus_shouldActivate_whenTrainerIsInactive() {
        var inactiveTrainer = Trainer.builder()
            .uid(UID)
            .username(USERNAME)
            .active(false)
            .build();
        doReturn(Optional.of(inactiveTrainer)).when(trainerRepository).getByUsername(USERNAME);

        testObject.toggleStatus(USERNAME);

        verify(trainerRepository).save(trainerCaptor.capture());
        assertTrue(trainerCaptor.getValue().isActive());

        assertNoUnexpectedInteractions();
    }

    @ParameterizedTest
    @NullSource
    void toggleStatus_shouldThrowNullPointerException_whenArgumentNull(String username) {
        assertThrows(NullPointerException.class, () -> testObject.toggleStatus(username));

        assertNoUnexpectedInteractions();
    }


    @Test
    void getByUsername_shouldReturnTrainer_whenTrainerExists() {
        doReturn(Optional.of(EXISTED_TRAINER)).when(trainerRepository).getByUsername(USERNAME);

        var result = testObject.getByUsername(USERNAME);

        assertSame(EXISTED_TRAINER, result);
        assertNoUnexpectedInteractions();
    }

    @Test
    void getByUsername_shouldThrowException_whenTrainerNotExists() {
        doReturn(Optional.empty()).when(trainerRepository).getByUsername(USERNAME);

        assertThrows(TrainerNotFoundException.class, () -> testObject.getByUsername(USERNAME));

        assertNoUnexpectedInteractions();
    }

    @ParameterizedTest
    @NullSource
    void getByUsername_shouldThrowException_whenArgumentNull(String username) {
        assertThrows(NullPointerException.class, () -> testObject.getByUsername(username));

        assertNoUnexpectedInteractions();
    }


    private void assertNoUnexpectedInteractions() {
        verifyNoMoreInteractions(
            passwordGenerator,
            usernameGenerator,
            trainerRepository,
            trainingTypeService,
            passwordService
        );
    }
}
