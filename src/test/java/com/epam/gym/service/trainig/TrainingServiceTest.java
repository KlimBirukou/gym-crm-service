package com.epam.gym.service.trainig;

import com.epam.gym.domain.training.Training;
import com.epam.gym.mother.TrainingMother;
import com.epam.gym.mother.dto.training.CreateTrainingDtoMother;
import com.epam.gym.repository.training.ITrainingRepository;
import com.epam.gym.service.training.TrainingService;
import com.epam.gym.service.training.dto.CreateTrainingDto;
import com.epam.gym.validator.IValidator;
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

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class TrainingServiceTest {

    private static final LocalDate DATE = LocalDate.of(2026, 1, 1);
    private static final UUID UID = UUID.randomUUID();
    private static final UUID TRAINEE_UID = UUID.randomUUID();
    private static final UUID TRAINER_UID = UUID.randomUUID();
    private static final String NAME = "name";
    private static final CreateTrainingDto CREATE_TRAINING_DTO =
        CreateTrainingDtoMother.get(TRAINEE_UID, TRAINER_UID, NAME, DATE);
    private static final Training TRAINING_1 =
        TrainingMother.get(UID, DATE);
    private static final Training TRAINING_2 =
        TrainingMother.get(UID, DATE);

    @Mock
    private ITrainingRepository trainingRepository;
    @Mock
    private IValidator<CreateTrainingDto> createTrainingValidator;
    @Mock
    private IValidator<LocalDate> trainingDateValidator;

    @Captor
    private ArgumentCaptor<Training> trainingCaptor;

    private TrainingService testObject;

    @BeforeEach
    void setUp() {
        testObject = new TrainingService(
            trainingRepository,
            createTrainingValidator,
            trainingDateValidator
        );
    }

    @Test
    void create_shouldCreateTraining_whenValidationPassed() {
        doNothing().when(createTrainingValidator)
            .validate(CREATE_TRAINING_DTO);

        var result = testObject.create(CREATE_TRAINING_DTO);
        verify(trainingRepository).save(trainingCaptor.capture());
        var saved = trainingCaptor.getValue();

        assertNotNull(result);
        assertNotNull(result.getUid());
        assertEquals(CREATE_TRAINING_DTO.traineeUid(), saved.getTraineeUid());
        assertEquals(CREATE_TRAINING_DTO.trainerUid(), saved.getTrainerUid());
        assertEquals(CREATE_TRAINING_DTO.date(), saved.getDate());
        assertEquals(CREATE_TRAINING_DTO.name(), saved.getName());
        assertEquals(CREATE_TRAINING_DTO.type(), saved.getType());
        assertEquals(CREATE_TRAINING_DTO.duration(), saved.getDuration());

        assertEquals(result.getUid(), saved.getUid());

        verify(createTrainingValidator, times(1))
            .validate(CREATE_TRAINING_DTO);
        verify(trainingRepository, times(1))
            .save(saved);
        verifyNoMoreInteractions(createTrainingValidator, trainingRepository);
    }

    @Test
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
            .findByLocalDate(date);

        var result = testObject.findAllByDate(date);

        assertNotNull(result);
        assertEquals(size, result.size());
        assertEquals(trainings, result);

        verify(trainingDateValidator, times(1))
            .validate(date);
        verify(trainingRepository, times(1)).findByLocalDate(date);
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
    }
}
