package com.epam.gym.service.trainig;

import com.epam.gym.domain.training.Training;
import com.epam.gym.domain.training.TrainingType;
import com.epam.gym.domain.user.Trainee;
import com.epam.gym.domain.user.Trainer;
import com.epam.gym.repository.domain.training.ITrainingRepository;
import com.epam.gym.service.assignment.ITraineeAssignmentTrainerService;
import com.epam.gym.service.trainee.ITraineeService;
import com.epam.gym.service.trainer.ITrainerService;
import com.epam.gym.service.training.TrainingService;
import com.epam.gym.service.training.dto.CreateTrainingDto;
import com.epam.gym.service.type.ITrainingTypeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.doNothing;
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

   private static final CreateTrainingDto CREATE_TRAINING_DTO = new CreateTrainingDto(
        TRAINEE_USERNAME,
        TRAINER_USERNAME,
        TRAINING_NAME,
        TRAINING_TYPE_NAME,
        DATE,
        DURATION_MINUTES
    );
    private static final TrainingType TRAINING_TYPE = TrainingType.builder()
        .uid(TRAINING_TYPE_UID)
        .name(TRAINING_TYPE_NAME)
        .build();


    @Mock
    private ITrainingRepository trainingRepository;
    @Mock
    private ITraineeService traineeService;
    @Mock
    private ITrainerService trainerService;
    @Mock
    private ITrainingTypeService trainingTypeService;
    @Mock
    private ITraineeAssignmentTrainerService traineeAssignmentTrainerService;

    @Captor
    private ArgumentCaptor<Training> trainingCaptor;

    private TrainingService testObject;

    @BeforeEach
    void setUp() {
        testObject = new TrainingService(
            trainingRepository,
            traineeService,
            trainerService,
            trainingTypeService,
            traineeAssignmentTrainerService
        );
    }

    @Test
    void create_shouldCreateTraining_whenValidationPassed() {
        var trainee = Trainee.builder()
            .uid(TRAINEE_UID)
            .username(TRAINEE_USERNAME)
            .active(true)
            .build();
        var trainer = Trainer.builder()
            .uid(TRAINER_UID)
            .username(TRAINER_USERNAME)
            .specialization(TRAINING_TYPE)
            .active(true)
            .build();
        doReturn(trainee).when(traineeService).getByUsername(TRAINEE_USERNAME);
        doReturn(trainer).when(trainerService).getByUsername(TRAINER_USERNAME);
        doNothing().when(traineeAssignmentTrainerService).checkAssignExist(TRAINEE_USERNAME, TRAINER_USERNAME);
        doReturn(TRAINING_TYPE).when(trainingTypeService).getByName(TRAINING_TYPE_NAME);
        doReturn(List.of()).when(trainingRepository).getTrainingsOnDate(DATE);

        var result = testObject.create(CREATE_TRAINING_DTO);

        verify(trainingRepository).save(trainingCaptor.capture());
        var saved = trainingCaptor.getValue();

        assertSame(result, saved);
        assertNotNull(saved.getUid());
        assertEquals(TRAINEE_UID,   saved.getTraineeUid());
        assertEquals(TRAINER_UID,   saved.getTrainerUid());
        assertEquals(TRAINING_NAME, saved.getName());
        assertEquals(TRAINING_TYPE, saved.getTrainingType());
        assertEquals(DATE,          saved.getDate());
        assertEquals(Duration.ofMinutes(DURATION_MINUTES), saved.getDuration());

        assertNoUnexpectedInteractions();
    }

    /*@Test
    void create_shouldThrow_whenValidationFailed() {
        doThrow(new RuntimeException()).when(createTrainingValidator)
            .validate(CREATE_TRAINING_DTO);

        assertThrows(RuntimeException.class,
            () -> testObject.create(CREATE_TRAINING_DTO));

        verify(createTrainingValidator, times(1))
            .validate(CREATE_TRAINING_DTO);
        verifyNoInteractions(trainingRepository);
        verifyNoMoreInteractions(createTrainingValidator);
    }

    @ParameterizedTest
    @NullSource
    void create_shouldThrowException_whenDataNull(CreateTrainingDto dto) {
        assertThrows(NullPointerException.class,
            () -> testObject.create(dto));
    }

    static Stream<Arguments> provideSuccessfulFindAllTestData() {
        return Stream.of(
            Arguments.of(DATE, List.of(), 0),
            Arguments.of(DATE, List.of(TRAINING_1), 1),
            Arguments.of(DATE, List.of(TRAINING_1, TRAINING_2), 2)
        );
    }

    @ParameterizedTest
    @MethodSource("provideSuccessfulFindAllTestData")
    void findAllByDate_shouldReturnList_whenValidationPassed(LocalDate date,
                                                             List<Training> trainings,
                                                             int size) {
        doNothing().when(trainingDateValidator)
            .validate(date);
        doReturn(trainings).when(trainingRepository)
            .findByDate(date);

        var result = testObject.findAllByDate(date);

        assertNotNull(result);
        assertEquals(size, result.size());
        assertEquals(trainings, result);

        verify(trainingDateValidator, times(1))
            .validate(date);
        verify(trainingRepository, times(1)).findByDate(date);
        verifyNoMoreInteractions(trainingDateValidator, trainingRepository);
    }

    @Test
    void findAllByDate_shouldThrowException_whenValidationFailed() {
        doThrow(new RuntimeException()).when(trainingDateValidator)
            .validate(DATE);

        assertThrows(RuntimeException.class,
            () -> testObject.findAllByDate(DATE));

        verify(trainingDateValidator, times(1))
            .validate(DATE);
        verifyNoInteractions(trainingRepository);
        verifyNoMoreInteractions(trainingDateValidator);
    }

    @ParameterizedTest
    @NullSource
    void findAllByDate_shouldThrowException_whenDataNull(LocalDate date) {
        assertThrows(NullPointerException.class,
            () -> testObject.findAllByDate(date));
    }*/

    private void assertNoUnexpectedInteractions() {
        verifyNoMoreInteractions(
            trainingRepository,
            traineeService,
            trainerService,
            trainingTypeService,
            traineeAssignmentTrainerService
        );
    }
}
