package com.epam.gym.service.trainig;

import com.epam.gym.domain.training.Training;
import com.epam.gym.domain.training.TrainingType;
import com.epam.gym.domain.user.Trainee;
import com.epam.gym.domain.user.Trainer;
import com.epam.gym.repository.trainee.ITraineeRepository;
import com.epam.gym.repository.trainer.ITrainerRepository;
import com.epam.gym.repository.training.ITrainingRepository;
import com.epam.gym.service.training.TrainingService;
import com.epam.gym.service.training.dto.CreateTrainingDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TrainingServiceTest {

    private static final UUID TARGET_TRAINER_UID = UUID.randomUUID();
    private static final UUID OTHER_TRAINER_UID = UUID.randomUUID();
    private static final UUID TARGET_TRAINEE_UID = UUID.randomUUID();
    private static final UUID OTHER_TRAINEE_UID = UUID.randomUUID();
    private static final String TRAINING_NAME = "training_name";
    private static final TrainingType TRAINING_TYPE = TrainingType.CARDIO;
    private static final LocalDate TRAINING_DATE = LocalDate.of(2020, 1, 1);
    private static final Duration TRAINING_DURATION = Duration.ofHours(2);

    private static final Trainer TARGET_TRAINER = Trainer.builder()
        .uid(TARGET_TRAINER_UID)
        .build();
    private static final Trainee TARGET_TRAINEE = Trainee.builder()
        .uid(TARGET_TRAINEE_UID)
        .build();
    private static final CreateTrainingDto CREATE_TRAINING_DTO = new CreateTrainingDto(
        TARGET_TRAINER_UID,
        TARGET_TRAINEE_UID,
        TRAINING_NAME,
        TRAINING_TYPE,
        TRAINING_DATE,
        TRAINING_DURATION
    );
    private static final Training CONFLICTING_TRAINING_FOR_TRAINER = Training.builder()
        .trainerUid(CREATE_TRAINING_DTO.trainerUid())
        .traineeUid(OTHER_TRAINEE_UID)
        .build();

    private static final Training CONFLICTING_TRAINING_FOR_TRAINEE = Training.builder()
        .trainerUid(OTHER_TRAINER_UID)
        .traineeUid(CREATE_TRAINING_DTO.traineeUid())
        .build();

    private static final String EX_MESSAGE_TRAINER_NOT_FOUND =
        "No trainer with id = %s for create training".formatted(CREATE_TRAINING_DTO.trainerUid());
    private static final String EX_MESSAGE_TRAINEE_NOT_FOUND =
        "No trainee with id = %s for create training".formatted(CREATE_TRAINING_DTO.traineeUid());
    private static final String EX_MESSAGE_TRAINER_CONFLICT =
        "Trainer with id = %s already has a training in this day = %s".formatted(
            CREATE_TRAINING_DTO.trainerUid(),
            CREATE_TRAINING_DTO.trainingDate()
        );
    private static final String EX_MESSAGE_TRAINEE_CONFLICT =
        "Trainee with id = %s already has a training in this day = %s".formatted(
            CREATE_TRAINING_DTO.traineeUid(),
            CREATE_TRAINING_DTO.trainingDate()
        );

    @Mock
    private ITrainingRepository trainingRepository;
    @Mock
    private ITraineeRepository traineeRepository;
    @Mock
    private ITrainerRepository trainerRepository;

    @Captor
    private ArgumentCaptor<Training> trainingCaptor;

    @InjectMocks
    private TrainingService testObject;

    @BeforeEach
    void setUp() {
        testObject.setTrainingRepository(trainingRepository);
        testObject.setTrainerRepository(trainerRepository);
        testObject.setTraineeRepository(traineeRepository);
    }

    static Stream<Arguments> provide() {
        return Stream.of(
            Arguments.of(List.of()),
            Arguments.of(List.of(
                Training.builder()
                    .trainerUid(OTHER_TRAINER_UID)
                    .build())),
            Arguments.of(List.of(
                Training.builder()
                    .trainerUid(OTHER_TRAINEE_UID)
                    .build())),
            Arguments.of(List.of(
                Training.builder()
                    .trainerUid(OTHER_TRAINER_UID)
                    .build(),
                Training.builder()
                    .trainerUid(OTHER_TRAINEE_UID)
                    .build()))
        );
    }

    @ParameterizedTest
    @MethodSource("provide")
    void create_shouldBuildSaveAndReturnTraining_whenTrainerAndTraineeAreExistsAndAvailableOnDate(
        List<Training> trainingList
    ) {
        when(trainerRepository.findByUid(CREATE_TRAINING_DTO.trainerUid()))
            .thenReturn(Optional.of(TARGET_TRAINER));
        when(traineeRepository.findByUid(CREATE_TRAINING_DTO.traineeUid()))
            .thenReturn(Optional.of(TARGET_TRAINEE));
        when(trainingRepository.findByLocalDate(CREATE_TRAINING_DTO.trainingDate()))
            .thenReturn(trainingList);

        var result = testObject.create(CREATE_TRAINING_DTO);

        assertNotNull(result);
        assertNotNull(result.getTrainingUid());
        assertEquals(CREATE_TRAINING_DTO.trainerUid(), result.getTrainerUid());
        assertEquals(CREATE_TRAINING_DTO.traineeUid(), result.getTraineeUid());
        assertEquals(CREATE_TRAINING_DTO.trainingName(), result.getTrainingName());
        assertEquals(CREATE_TRAINING_DTO.trainingType(), result.getTrainingType());
        assertEquals(CREATE_TRAINING_DTO.trainingDate(), result.getTrainingDate());
        assertEquals(CREATE_TRAINING_DTO.trainingDuration(), result.getTrainingDuration());

        verify(traineeRepository, times(1)).findByUid(CREATE_TRAINING_DTO.traineeUid());
        verify(trainerRepository, times(1)).findByUid(CREATE_TRAINING_DTO.trainerUid());
        verify(trainingRepository, times(1)).findByLocalDate(CREATE_TRAINING_DTO.trainingDate());
        verify(trainingRepository, times(1)).save(trainingCaptor.capture());
        assertEquals(result, trainingCaptor.getValue());
    }

    @Test
    void create_shouldThrowException_whenTrainerNotExists() {
        when(trainerRepository.findByUid(CREATE_TRAINING_DTO.trainerUid()))
            .thenReturn(Optional.empty());

        var exception = assertThrows(RuntimeException.class,
            () -> testObject.create(CREATE_TRAINING_DTO));

        assertEquals(EX_MESSAGE_TRAINER_NOT_FOUND, exception.getMessage());
        verify(trainerRepository, times(1)).findByUid(CREATE_TRAINING_DTO.trainerUid());
        verify(traineeRepository, never()).findByUid(any());
        verify(trainingRepository, never()).findByLocalDate(any());
        verify(trainingRepository, never()).save(any());
    }

    @Test
    void create_shouldThrowException_whenTraineeNotExists() {
        when(trainerRepository.findByUid(CREATE_TRAINING_DTO.trainerUid()))
            .thenReturn(Optional.of(TARGET_TRAINER));
        when(traineeRepository.findByUid(CREATE_TRAINING_DTO.traineeUid()))
            .thenReturn(Optional.empty());

        var exception = assertThrows(RuntimeException.class,
            () -> testObject.create(CREATE_TRAINING_DTO));

        assertEquals(EX_MESSAGE_TRAINEE_NOT_FOUND, exception.getMessage());
        verify(trainerRepository, times(1)).findByUid(CREATE_TRAINING_DTO.trainerUid());
        verify(traineeRepository, times(1)).findByUid(CREATE_TRAINING_DTO.traineeUid());
        verify(trainingRepository, never()).findByLocalDate(any());
        verify(trainingRepository, never()).save(any());
    }

    @Test
    void create_shouldThrowException_whenTrainerHasConflict() {
        when(trainerRepository.findByUid(CREATE_TRAINING_DTO.trainerUid()))
            .thenReturn(Optional.of(TARGET_TRAINER));
        when(traineeRepository.findByUid(CREATE_TRAINING_DTO.traineeUid()))
            .thenReturn(Optional.of(TARGET_TRAINEE));
        when(trainingRepository.findByLocalDate(CREATE_TRAINING_DTO.trainingDate()))
            .thenReturn(List.of(CONFLICTING_TRAINING_FOR_TRAINER));

        var exception = assertThrows(RuntimeException.class,
            () -> testObject.create(CREATE_TRAINING_DTO));

        assertEquals(EX_MESSAGE_TRAINER_CONFLICT, exception.getMessage());
        verify(trainerRepository, times(1)).findByUid(CREATE_TRAINING_DTO.trainerUid());
        verify(traineeRepository, times(1)).findByUid(CREATE_TRAINING_DTO.traineeUid());
        verify(trainingRepository, times(1)).findByLocalDate(CREATE_TRAINING_DTO.trainingDate());
        verify(trainingRepository, never()).save(any());
    }

    @Test
    void create_shouldThrowException_whenTraineeHasConflict() {
        when(trainerRepository.findByUid(CREATE_TRAINING_DTO.trainerUid()))
            .thenReturn(Optional.of(TARGET_TRAINER));
        when(traineeRepository.findByUid(CREATE_TRAINING_DTO.traineeUid()))
            .thenReturn(Optional.of(TARGET_TRAINEE));
        when(trainingRepository.findByLocalDate(CREATE_TRAINING_DTO.trainingDate()))
            .thenReturn(List.of(CONFLICTING_TRAINING_FOR_TRAINEE));

        var exception = assertThrows(RuntimeException.class,
            () -> testObject.create(CREATE_TRAINING_DTO));

        assertEquals(EX_MESSAGE_TRAINEE_CONFLICT, exception.getMessage());
        verify(trainerRepository, times(1)).findByUid(CREATE_TRAINING_DTO.trainerUid());
        verify(traineeRepository, times(1)).findByUid(CREATE_TRAINING_DTO.traineeUid());
        verify(trainingRepository, times(1)).findByLocalDate(CREATE_TRAINING_DTO.trainingDate());
        verify(trainingRepository, never()).save(any());
    }

    @Test
    void create_shouldThrowNpe_whenDtoIsNull() {
        assertThrows(NullPointerException.class,
            () -> testObject.create(null));

        verify(trainerRepository, never()).findByUid(any());
        verify(traineeRepository, never()).findByUid(any());
        verify(trainingRepository, never()).findByLocalDate(any());
        verify(trainingRepository, never()).save(any());
    }
}
