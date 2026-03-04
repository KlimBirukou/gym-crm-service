package com.epam.gym.service.trainig;

import com.epam.gym.domain.training.Training;
import com.epam.gym.domain.training.TrainingType;
import com.epam.gym.domain.user.Trainee;
import com.epam.gym.domain.user.Trainer;
import com.epam.gym.exception.conflict.date.TraineeDateConflictException;
import com.epam.gym.exception.conflict.date.TrainerDateConflictException;
import com.epam.gym.exception.not.active.TraineeNotActiveException;
import com.epam.gym.exception.not.active.TrainerNotActiveException;
import com.epam.gym.exception.not.assigned.NotAssignmentException;
import com.epam.gym.repository.domain.training.ITrainingRepository;
import com.epam.gym.service.assignment.IAssignmentService;
import com.epam.gym.service.trainee.ITraineeService;
import com.epam.gym.service.trainer.ITrainerService;
import com.epam.gym.service.training.TrainingService;
import com.epam.gym.service.training.dto.CreateTrainingDto;
import com.epam.gym.service.training.dto.TraineeTrainingsDto;
import com.epam.gym.service.training.dto.TrainerTrainingsDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class TrainingServiceTest {

    private static final LocalDate DATE = LocalDate.of(2026, 1, 1);
    private static final LocalDate FROM_DATE = LocalDate.of(2025, 12, 1);
    private static final LocalDate TO_DATE = LocalDate.of(2026, 2, 1);
    private static final UUID TRAINEE_UID = UUID.randomUUID();
    private static final UUID TRAINER_UID = UUID.randomUUID();
    private static final UUID TRAINING_TYPE_UID = UUID.randomUUID();
    private static final String TRAINEE_USERNAME = "trainee_username";
    private static final String TRAINER_USERNAME = "trainer_username";
    private static final String TRAINING_NAME = "training_name";
    private static final String TRAINING_TYPE_NAME = "training_type_name";
    private static final int DURATION_MINUTES = 60;

    @Mock
    private ITrainingRepository trainingRepository;
    @Mock
    private ITraineeService traineeService;
    @Mock
    private ITrainerService trainerService;
    @Mock
    private IAssignmentService assignmentService;

    @Captor
    private ArgumentCaptor<Training> trainingCaptor;

    private TrainingService testObject;

    @BeforeEach
    void setUp() {
        testObject = new TrainingService(
            trainingRepository,
            traineeService,
            trainerService,
            assignmentService
        );
    }

    @Test
    void create_shouldCreateTraining_whenValidationPassed() {
        var trainee = getTrainee(true);
        var trainer = getTrainer(true);
        var createTrainingDto = getCreateTrainingDto();
        doReturn(trainee).when(traineeService).getByUsername(TRAINEE_USERNAME);
        doReturn(trainer).when(trainerService).getByUsername(TRAINER_USERNAME);
        doReturn(true).when(assignmentService).checkAssignExist(TRAINEE_USERNAME, TRAINER_USERNAME);
        doReturn(List.of()).when(trainingRepository).getTrainingsOnDate(DATE);

        var result = testObject.create(createTrainingDto);

        verify(trainingRepository).save(trainingCaptor.capture());
        var saved = trainingCaptor.getValue();

        assertSame(result, saved);
        assertNotNull(saved.getUid());
        assertEquals(TRAINEE_UID, saved.getTraineeUid());
        assertEquals(TRAINER_UID, saved.getTrainerUid());
        assertEquals(TRAINING_NAME, saved.getName());
        assertEquals(getTrainingType(), saved.getTrainingType());
        assertEquals(DATE, saved.getDate());
        assertEquals(Duration.ofMinutes(DURATION_MINUTES), saved.getDuration());

        assertNoUnexpectedInteractions();
    }

    @Test
    void create_shouldThrowException_whenTraineeNotActive() {
        var trainee = getTrainee(false);
        var createTrainingDto = getCreateTrainingDto();
        doReturn(trainee).when(traineeService).getByUsername(TRAINEE_USERNAME);

        assertThrows(TraineeNotActiveException.class, () -> testObject.create(createTrainingDto));

        assertNoUnexpectedInteractions();
    }

    @Test
    void create_shouldThrowException_whenTrainerNotActive() {
        var trainee = getTrainee(true);
        var trainer = getTrainer(false);
        var createTrainingDto = getCreateTrainingDto();
        doReturn(trainee).when(traineeService).getByUsername(TRAINEE_USERNAME);
        doReturn(trainer).when(trainerService).getByUsername(TRAINER_USERNAME);

        assertThrows(TrainerNotActiveException.class, () -> testObject.create(createTrainingDto));

        assertNoUnexpectedInteractions();
    }

    @Test
    void create_shouldThrowException_whenNotAssigned() {
        var trainee = getTrainee(true);
        var trainer = getTrainer(true);
        var createTrainingDto = getCreateTrainingDto();
        doReturn(trainee).when(traineeService).getByUsername(TRAINEE_USERNAME);
        doReturn(trainer).when(trainerService).getByUsername(TRAINER_USERNAME);
        doReturn(false).when(assignmentService).checkAssignExist(TRAINEE_USERNAME, TRAINER_USERNAME);

        assertThrows(NotAssignmentException.class, () -> testObject.create(createTrainingDto));

        assertNoUnexpectedInteractions();
    }

    private static Stream<Arguments> provideConflictData() {
        return Stream.of(
            Arguments.of(TRAINEE_UID, UUID.randomUUID(), TraineeDateConflictException.class),
            Arguments.of(UUID.randomUUID(), TRAINER_UID, TrainerDateConflictException.class)
        );
    }

    @ParameterizedTest
    @MethodSource("provideConflictData")
    void create_shouldThrowException_whenDateConflictExists(UUID traineeUid,
                                                            UUID trainerUid,
                                                            Class<? extends Throwable> expectedException) {
        var trainee = getTrainee(true);
        var trainer = getTrainer(true);
        var createTrainingDto = getCreateTrainingDto();
        var existingTraining = Training.builder()
            .uid(UUID.randomUUID())
            .traineeUid(traineeUid)
            .trainerUid(trainerUid)
            .date(DATE)
            .build();
        doReturn(trainee).when(traineeService).getByUsername(TRAINEE_USERNAME);
        doReturn(trainer).when(trainerService).getByUsername(TRAINER_USERNAME);
        doReturn(true).when(assignmentService).checkAssignExist(TRAINEE_USERNAME, TRAINER_USERNAME);
        doReturn(List.of(existingTraining)).when(trainingRepository).getTrainingsOnDate(DATE);

        assertThrows(expectedException, () -> testObject.create(createTrainingDto));

        assertNoUnexpectedInteractions();
    }

    @ParameterizedTest
    @NullSource
    void create_shouldThrowException_whenArgumentNull(CreateTrainingDto dto) {
        assertThrows(NullPointerException.class, () -> testObject.create(dto));

        assertNoUnexpectedInteractions();
    }


    @Test
    void getTraineeTrainings_shouldReturnList_whenValidInput() {
        var trainee = getTrainee(true);
        var training = getTraining();
        var criteriaDto = getTraineeTrainingsDto();
        doReturn(trainee).when(traineeService).getByUsername(TRAINEE_USERNAME);
        doReturn(List.of(training)).when(trainingRepository).getTraineeTrainings(
            TRAINEE_UID, FROM_DATE, TO_DATE, null, TRAINING_TYPE_NAME
        );

        var result = testObject.getTraineeTrainings(criteriaDto);

        assertEquals(1, result.size());
        assertEquals(training, result.getFirst());

        assertNoUnexpectedInteractions();
    }

    @ParameterizedTest
    @NullSource
    void getTraineeTrainings_shouldThrowException_whenArgumentNull(TraineeTrainingsDto dto) {
        assertThrows(NullPointerException.class, () -> testObject.getTraineeTrainings(dto));

        assertNoUnexpectedInteractions();
    }


    @Test
    void getTrainerTrainings_shouldReturnList_whenValidInput() {
        var trainer = getTrainer(true);
        var training = getTraining();
        var criteriaDto = getTrainerTrainingsDto();
        doReturn(trainer).when(trainerService).getByUsername(TRAINER_USERNAME);
        doReturn(List.of(training)).when(trainingRepository).getTrainerTrainings(
            TRAINER_UID, FROM_DATE, TO_DATE, TRAINEE_USERNAME
        );

        var result = testObject.getTrainerTrainings(criteriaDto);

        assertEquals(1, result.size());
        assertEquals(training, result.getFirst());

        assertNoUnexpectedInteractions();
    }

    @ParameterizedTest
    @NullSource
    void getTrainerTrainings_shouldThrowException_whenArgumentNull(TrainerTrainingsDto dto) {
        assertThrows(NullPointerException.class, () -> testObject.getTrainerTrainings(dto));

        assertNoUnexpectedInteractions();
    }

    private static CreateTrainingDto getCreateTrainingDto() {
        return new CreateTrainingDto(
            TRAINEE_USERNAME,
            TRAINER_USERNAME,
            TRAINING_NAME,
            DATE,
            DURATION_MINUTES
        );
    }

    private static TraineeTrainingsDto getTraineeTrainingsDto() {
        return new TraineeTrainingsDto(
            TRAINEE_USERNAME,
            FROM_DATE,
            TO_DATE,
            null,
            TRAINING_TYPE_NAME
        );
    }

    private static TrainerTrainingsDto getTrainerTrainingsDto() {
        return new TrainerTrainingsDto(
            TRAINER_USERNAME,
            FROM_DATE,
            TO_DATE,
            TRAINEE_USERNAME
        );
    }

    private static TrainingType getTrainingType() {
        return TrainingType.builder()
            .uid(TRAINING_TYPE_UID)
            .name(TRAINING_TYPE_NAME)
            .build();
    }

    private static Trainee getTrainee(boolean status) {
        return Trainee.builder()
            .uid(TRAINEE_UID)
            .username(TRAINEE_USERNAME)
            .active(status)
            .build();
    }

    private static Trainer getTrainer(boolean status) {
        return Trainer.builder()
            .uid(TRAINER_UID)
            .username(TRAINER_USERNAME)
            .specialization(getTrainingType())
            .active(status)
            .build();
    }

    private static Training getTraining() {
        return Training.builder()
            .uid(UUID.randomUUID())
            .traineeUid(UUID.randomUUID())
            .trainerUid(UUID.randomUUID())
            .date(DATE)
            .build();
    }

    private void assertNoUnexpectedInteractions() {
        verifyNoMoreInteractions(
            trainingRepository,
            traineeService,
            trainerService,
            assignmentService
        );
    }
}
