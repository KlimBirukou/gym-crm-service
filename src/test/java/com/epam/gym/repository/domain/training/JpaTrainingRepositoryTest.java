package com.epam.gym.repository.domain.training;

import com.epam.gym.domain.training.Training;
import com.epam.gym.repository.entity.TrainingEntity;
import com.epam.gym.repository.jpa.training.ITrainingEntityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
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
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
public class JpaTrainingRepositoryTest {

    private static final LocalDate DATE = LocalDate.of(2026, 1, 1);
    private static final UUID TRAINEE_UID = UUID.randomUUID();
    private static final UUID TRAINER_UID = UUID.randomUUID();
    private static final UUID TRAINING_TYPE_UID = UUID.randomUUID();
    private static final Training TRAINING_1 = new Training();
    private static final Training TRAINING_2 = new Training();
    private static final TrainingEntity TRAINING_ENTITY_1 = new TrainingEntity();
    private static final TrainingEntity TRAINING_ENTITY_2 = new TrainingEntity();

    @Mock
    private ITrainingEntityRepository repository;
    @Mock
    private ConversionService conversionService;

    private JpaTrainingRepository testObject;

    @BeforeEach
    void setUp() {
        testObject = new JpaTrainingRepository(
            repository,
            conversionService
        );
    }

    @Test
    void save_shouldSaveEntity() {
        doReturn(TRAINING_ENTITY_1).when(conversionService).convert(TRAINING_1, TrainingEntity.class);

        testObject.save(TRAINING_1);

        verify(repository).save(TRAINING_ENTITY_1);

        assertNoUnexpectedInteractions();
    }

    @ParameterizedTest
    @NullSource
    void save_shouldThrowException_whenArgumentNull(Training training) {
        assertThrows(NullPointerException.class, () -> testObject.save(training));

        assertNoUnexpectedInteractions();
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
    void getTraineeTrainings_shouldReturnTrainings(List<TrainingEntity> entities, List<Training> trainings) {
        doReturn(entities).when(repository).findByTraineeUidAndTrainingTypeUid(TRAINEE_UID, TRAINING_TYPE_UID);
        IntStream.range(0, entities.size()).forEach(i ->
            doReturn(trainings.get(i)).when(conversionService).convert(entities.get(i), Training.class)
        );

        var result = testObject.getTraineeTrainings(TRAINEE_UID, TRAINING_TYPE_UID);

        assertEquals(trainings.size(), result.size());
        assertEquals(trainings, result);

        assertNoUnexpectedInteractions();
    }

    private static Stream<Arguments> provideNullArguments() {
        return Stream.of(
            Arguments.of(null, TRAINING_TYPE_UID),
            Arguments.of(TRAINEE_UID, null),
            Arguments.of(null, null)
        );
    }

    @ParameterizedTest
    @MethodSource("provideNullArguments")
    void getTraineeTrainings_shouldThrowsException_whenArgumentsNull(UUID traineeUid, UUID trainingTypeUid) {
        assertThrows(NullPointerException.class, () -> testObject.getTraineeTrainings(traineeUid, trainingTypeUid));

        assertNoUnexpectedInteractions();
    }


    @ParameterizedTest
    @MethodSource("provideTestData")
    void getTrainerTrainings_shouldReturnTrainings(List<TrainingEntity> entities, List<Training> trainings) {
        doReturn(entities).when(repository).findByTrainerUidAndTrainingTypeUid(TRAINER_UID, TRAINING_TYPE_UID);
        IntStream.range(0, entities.size()).forEach(i ->
            doReturn(trainings.get(i)).when(conversionService).convert(entities.get(i), Training.class)
        );

        var result = testObject.getTrainerTrainings(TRAINER_UID, TRAINING_TYPE_UID);

        assertEquals(trainings.size(), result.size());
        assertEquals(trainings, result);

        assertNoUnexpectedInteractions();
    }

    @ParameterizedTest
    @MethodSource("provideNullArguments")
    void getTrainerTrainings_shouldThrowsException_whenArgumentsNull(UUID trainerUid, UUID trainingTypeUid) {
        assertThrows(NullPointerException.class, () -> testObject.getTrainerTrainings(trainerUid, trainingTypeUid));

        assertNoUnexpectedInteractions();
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

        assertNoUnexpectedInteractions();
    }

    @ParameterizedTest
    @NullSource
    void getTrainerTrainings_shouldThrowsException_whenArgumentsNull(LocalDate date) {
        assertThrows(NullPointerException.class, () -> testObject.getTrainingsOnDate(date));

        assertNoUnexpectedInteractions();
    }


    private void assertNoUnexpectedInteractions() {
        verifyNoMoreInteractions(
            repository,
            conversionService
        );
    }
}
