package com.epam.gym.service.trainer;

import com.epam.gym.domain.training.TrainingType;
import com.epam.gym.domain.user.Trainer;
import com.epam.gym.exception.not.found.TrainerNotFoundException;
import com.epam.gym.repository.domain.trainer.ITrainerRepository;
import com.epam.gym.service.auth.IPasswordService;
import com.epam.gym.service.generator.name.IUsernameGenerator;
import com.epam.gym.service.generator.password.IPasswordGenerator;
import com.epam.gym.service.trainer.dto.CreateTrainerDto;
import com.epam.gym.service.trainer.dto.UpdateTrainerDto;
import com.epam.gym.service.type.ITrainingTypeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
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
    private static final String SPECIALIZATION_NAME = "specialization_name";

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

    @InjectMocks
    private TrainerService testObject;

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(
            passwordGenerator,
            usernameGenerator,
            trainerRepository,
            trainingTypeService,
            passwordService);
    }

    @Test
    void create_shouldCreateTrainer_whenAlways() {
        var trainingType = getTrainingType();
        var createTrainerDto = getCreateTrainerDto();
        doReturn(USERNAME).when(usernameGenerator).generate(FIRSTNAME, LASTNAME);
        doReturn(PASSWORD).when(passwordGenerator).generate();
        doReturn(trainingType).when(trainingTypeService).getByName(SPECIALIZATION_NAME);
        doReturn(HASHED_PASSWORD).when(passwordService).hashPassword(PASSWORD);

        var result = testObject.create(createTrainerDto);

        verify(trainerRepository).save(trainerCaptor.capture());
        var saved = trainerCaptor.getValue();

        assertSame(result, saved);
        assertNotNull(saved.getUid());
        assertEquals(FIRSTNAME, saved.getFirstName());
        assertEquals(LASTNAME, saved.getLastName());
        assertEquals(trainingType, saved.getSpecialization());
        assertEquals(USERNAME, saved.getUsername());
        assertEquals(PASSWORD, saved.getPassword());
        assertTrue(saved.isActive());
    }

    @ParameterizedTest
    @NullSource
    void create_shouldThrowException_whenArgumentNull(CreateTrainerDto dto) {
        assertThrows(NullPointerException.class, () -> testObject.create(dto));

    }

    @Test
    void update_shouldUpdateTrainer_whenTrainerExist() {
        var trainer = getTrainer();
        var trainerType = getTrainingType();
        var updateTrainerDto = getUpdateTrainerDto();
        doReturn(Optional.of(trainer)).when(trainerRepository).getByUsername(USERNAME);

        testObject.update(updateTrainerDto);

        verify(trainerRepository).update(trainerCaptor.capture());
        var saved = trainerCaptor.getValue();

        assertEquals(NEW_FIRSTNAME, saved.getFirstName());
        assertEquals(NEW_LASTNAME, saved.getLastName());
        assertEquals(trainerType, saved.getSpecialization());
        assertEquals(UID, saved.getUid());
        assertEquals(USERNAME, saved.getUsername());
        assertEquals(HASHED_PASSWORD, saved.getPassword());
        assertTrue(saved.isActive());
    }

    @Test
    void update_shouldThrowException_whenTrainerNotExist() {
        var updateTrainerDto = getUpdateTrainerDto();
        doReturn(Optional.empty()).when(trainerRepository).getByUsername(USERNAME);

        assertThrows(TrainerNotFoundException.class, () -> testObject.update(updateTrainerDto));
    }

    @ParameterizedTest
    @NullSource
    void update_shouldThrowException_whenDataNull(UpdateTrainerDto dto) {
        assertThrows(NullPointerException.class, () -> testObject.update(dto));
    }


    @Test
    void getByUsername_shouldReturnTrainer_whenTrainerExists() {
        var trainee = getTrainer();
        doReturn(Optional.of(trainee)).when(trainerRepository).getByUsername(USERNAME);

        var result = testObject.getByUsername(USERNAME);

        assertSame(trainee, result);
    }

    @Test
    void getByUsername_shouldThrowException_whenTrainerNotExists() {
        doReturn(Optional.empty()).when(trainerRepository).getByUsername(USERNAME);

        assertThrows(TrainerNotFoundException.class, () -> testObject.getByUsername(USERNAME));
    }

    @ParameterizedTest
    @NullSource
    void getByUsername_shouldThrowException_whenArgumentNull(String username) {
        assertThrows(NullPointerException.class, () -> testObject.getByUsername(username));
    }

    @Test
    void getByUids_shouldReturnEmptyList_whenUidsIsEmpty() {
        var result = testObject.getByUids(List.of());

        assertTrue(result.isEmpty());

        verifyNoInteractions(trainerRepository);
    }

    @ParameterizedTest
    @MethodSource("provideUidsData")
    void getByUids_shouldReturnExpectedResult(List<UUID> uids, List<Trainer> trainees) {
        doReturn(trainees).when(trainerRepository).findAllByUids(uids);

        var result = testObject.getByUids(uids);

        assertEquals(trainees.size(), result.size());
        assertEquals(trainees, result);
    }

    @ParameterizedTest
    @NullSource
    void getByUids_shouldThrowNpe_whenArgumentNull(List<UUID> uids) {
        assertThrows(NullPointerException.class, () -> testObject.getByUids(uids));
    }

    private static Stream<Arguments> provideUidsData() {
        return Stream.of(
            Arguments.of(
                List.of(UUID.randomUUID()),
                List.of(new Trainer())
            ),
            Arguments.of(
                List.of(UUID.randomUUID(), UUID.randomUUID()),
                List.of(new Trainer(), new Trainer())
            )
        );
    }

    private static CreateTrainerDto getCreateTrainerDto() {
        return new CreateTrainerDto(
            FIRSTNAME, LASTNAME, SPECIALIZATION_NAME
        );
    }

    private static UpdateTrainerDto getUpdateTrainerDto() {
        return new UpdateTrainerDto(
            USERNAME, NEW_FIRSTNAME, NEW_LASTNAME
        );
    }

    private static TrainingType getTrainingType() {
        return TrainingType.builder()
            .uid(UID)
            .name(SPECIALIZATION_NAME)
            .build();
    }

    private static Trainer getTrainer() {
        return Trainer.builder()
            .uid(UID)
            .firstName(FIRSTNAME)
            .lastName(LASTNAME)
            .username(USERNAME)
            .password(HASHED_PASSWORD)
            .specialization(getTrainingType())
            .active(true)
            .build();
    }
}
