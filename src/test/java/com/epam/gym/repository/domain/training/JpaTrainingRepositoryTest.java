package com.epam.gym.repository.domain.training;

import com.epam.gym.domain.training.Training;
import com.epam.gym.repository.entity.TrainingEntity;
import com.epam.gym.repository.jpa.training.ITrainingEntityRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.convert.ConversionService;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
public class JpaTrainingRepositoryTest {

    private static final LocalDate DATE = LocalDate.of(2026, 1, 1);
    private static final UUID TRAINEE_UID = UUID.randomUUID();
    private static final UUID TRAINER_UID = UUID.randomUUID();
    private static final Training TRAINING_1 = new Training();
    private static final Training TRAINING_2 = new Training();
    private static final TrainingEntity TRAINING_ENTITY_1 = new TrainingEntity();
    private static final TrainingEntity TRAINING_ENTITY_2 = new TrainingEntity();
    public static final String TRAINEE_USERNAME = "trainee";
    public static final String TRAINING_TYPE_NAME = "type";

    @Mock
    private ITrainingEntityRepository repository;
    @Mock
    private ConversionService conversionService;

    @InjectMocks
    private JpaTrainingRepository testObject;

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(repository, conversionService);
    }

    @Test
    void save_shouldSaveEntity() {
        doReturn(TRAINING_ENTITY_1).when(conversionService).convert(TRAINING_1, TrainingEntity.class);

        testObject.save(TRAINING_1);

        verify(repository).save(TRAINING_ENTITY_1);
    }

    @ParameterizedTest
    @NullSource
    void save_shouldThrowException_whenArgumentNull(Training training) {
        assertThrows(NullPointerException.class, () -> testObject.save(training));
    }

    private static Stream<Arguments> provideEntitiesData() {
        return Stream.of(
            Arguments.of(List.of()),
            Arguments.of(List.of(TRAINING_ENTITY_1)),
            Arguments.of(List.of(TRAINING_ENTITY_1, TRAINING_ENTITY_2))
        );
    }

    @ParameterizedTest
    @MethodSource("provideEntitiesData")
    void getTraineeTrainings_shouldReturnConvertedList(List<TrainingEntity> entities) {
        doReturn(entities).when(repository).findTraineeTrainings(
            TRAINEE_UID, DATE, DATE, TRAINEE_USERNAME, TRAINING_TYPE_NAME);
        entities.forEach(entity ->
            doReturn(new Training()).when(conversionService).convert(entity, Training.class)
        );

        var result = testObject.getTraineeTrainings(TRAINEE_UID, DATE, DATE, TRAINEE_USERNAME, TRAINING_TYPE_NAME);

        assertEquals(entities.size(), result.size());
        verify(repository).findTraineeTrainings(TRAINEE_UID, DATE, DATE, TRAINEE_USERNAME, TRAINING_TYPE_NAME);
        verify(conversionService, times(entities.size())).convert(any(TrainingEntity.class), eq(Training.class));
    }

    @ParameterizedTest
    @NullSource
    void getTraineeTrainings_shouldThrowException_whenUidNull(UUID traineeUid) {
        assertThrows(NullPointerException.class,
            () -> testObject.getTraineeTrainings(traineeUid, DATE, DATE, TRAINEE_USERNAME, TRAINING_TYPE_NAME));
    }

    @ParameterizedTest
    @MethodSource("provideEntitiesData")
    void getTrainerTrainings_shouldReturnConvertedList(List<TrainingEntity> entities) {
        doReturn(entities).when(repository).findTrainerTrainings(
            TRAINER_UID, DATE, DATE, TRAINEE_USERNAME);
        entities.forEach(entity ->
            doReturn(new Training()).when(conversionService).convert(entity, Training.class)
        );

        var result = testObject.getTrainerTrainings(TRAINER_UID, DATE, DATE, TRAINEE_USERNAME);

        assertEquals(entities.size(), result.size());
        verify(repository).findTrainerTrainings(TRAINER_UID, DATE, DATE, TRAINEE_USERNAME);
        verify(conversionService, times(entities.size())).convert(any(TrainingEntity.class), eq(Training.class));
    }

    @ParameterizedTest
    @NullSource
    void getTrainerTrainings_shouldThrowException_whenUidNull(UUID trainerUid) {
        assertThrows(NullPointerException.class,
            () -> testObject.getTrainerTrainings(trainerUid, DATE, DATE, TRAINEE_USERNAME));
    }

    private static Stream<Arguments> provideTestData() {
        return Stream.of(
            Arguments.of(List.of(), List.of()),
            Arguments.of(List.of(TRAINING_ENTITY_1), List.of(TRAINING_1)),
            Arguments.of(List.of(TRAINING_ENTITY_1, TRAINING_ENTITY_2), List.of(TRAINING_1, TRAINING_2))
        );
    }

    @ParameterizedTest
    @MethodSource("provideTestData")
    void getTrainingsOnDate_shouldReturnTrainings(List<TrainingEntity> entities, List<Training> trainings) {
        doReturn(entities).when(repository).findByDate(DATE);
        IntStream.range(0, entities.size()).forEach(i ->
            doReturn(trainings.get(i)).when(conversionService).convert(entities.get(i), Training.class)
        );

        var result = testObject.getTrainingsOnDate(DATE);

        assertEquals(trainings.size(), result.size());
        assertEquals(trainings, result);
    }

    @ParameterizedTest
    @NullSource
    void getTrainerTrainings_shouldThrowsException_whenArgumentsNull(LocalDate date) {
        assertThrows(NullPointerException.class, () -> testObject.getTrainingsOnDate(date));
    }
}
