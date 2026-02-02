package com.epam.gym.storage.training;

import com.epam.gym.domain.training.Training;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InMemoryTrainingStorageTest {

    private static final UUID UUID_1 = UUID.randomUUID();
    private static final UUID UUID_2 = UUID.randomUUID();
    private static final UUID NON_EXISTENT_UUID = UUID.randomUUID();
    private static final LocalDate DATE_1 = LocalDate.of(2020, 1, 1);
    private static final LocalDate DATE_2 = LocalDate.of(2020, 1, 2);
    private static final Training TRAINING_1 = Training.builder()
        .uid(UUID_1)
        .date(DATE_1)
        .build();
    private static final Training UPDATED_TRAINING = Training.builder()
        .uid(UUID_1)
        .date(DATE_2)
        .build();
    private static final Training TRAINING_2 = Training.builder()
        .uid(UUID_2)
        .date(DATE_2)
        .build();

    private InMemoryTrainingStorage testObject;

    @BeforeEach
    void setUp() {
        testObject = new InMemoryTrainingStorage();
    }

    static Stream<Arguments> providePutTestData() {
        return Stream.of(
            Arguments.of(
                Set.of(),
                TRAINING_1,
                Set.of(TRAINING_1)
            ),
            Arguments.of(
                Set.of(TRAINING_1),
                TRAINING_2,
                Set.of(TRAINING_1, TRAINING_2)
            ),
            Arguments.of(
                Set.of(TRAINING_1),
                UPDATED_TRAINING,
                Set.of(UPDATED_TRAINING)
            ),
            Arguments.of(
                Set.of(TRAINING_1, TRAINING_2),
                UPDATED_TRAINING,
                Set.of(UPDATED_TRAINING, TRAINING_2)
            )
        );
    }

    @ParameterizedTest
    @MethodSource("providePutTestData")
    void put_shouldAddOrUpdateTrainingInStorage(Set<Training> existingTrainings,
                                                Training trainingToSave,
                                                Set<Training> expectedTrainings) {
        fillStorage(existingTrainings);

        testObject.put(trainingToSave.getUid(), trainingToSave);

        assertEquals(expectedTrainings, new HashSet<>(testObject.values()));
    }

    @Test
    void get_shouldReturnTraining_whenTrainingExist() {
        fillStorage(Set.of(TRAINING_1));

        var actualTraining = testObject.get(UUID_1);

        assertEquals(TRAINING_1, actualTraining.orElseThrow());
    }

    static Stream<Arguments> provideGetTestData() {
        return Stream.of(
            Arguments.of(
                NON_EXISTENT_UUID,
                List.of()
            ),
            Arguments.of(
                NON_EXISTENT_UUID,
                List.of(TRAINING_1)
            ),
            Arguments.of(
                NON_EXISTENT_UUID,
                List.of(TRAINING_1, TRAINING_2)
            )
        );
    }

    @ParameterizedTest
    @MethodSource("provideGetTestData")
    void get_shouldReturnExpectedResult_whenTraineeNotExist(UUID uidToFind,
                                                            List<Training> existingTrainings) {
        fillStorage(existingTrainings);

        var actualTraining = testObject.get(uidToFind);

        assertTrue(actualTraining.isEmpty());
    }

    @Test
    void remove_shouldDeleteValue() {
        testObject.put(TRAINING_1.getUid(), TRAINING_1);

        assertTrue(testObject.get(TRAINING_1.getUid()).isPresent());

        testObject.remove(TRAINING_1.getUid());

        assertTrue(testObject.get(TRAINING_1.getUid()).isEmpty());
    }

    @Test
    void remove_shouldNotFailOnMissingKey() {
        testObject.remove(NON_EXISTENT_UUID);

        assertTrue(testObject.get(NON_EXISTENT_UUID).isEmpty());
    }

    static Stream<Set<Training>> provideValuesTestData() {
        return Stream.of(
            Set.of(TRAINING_1),
            Set.of(TRAINING_1, TRAINING_2)
        );
    }

    @ParameterizedTest
    @EmptySource
    @MethodSource("provideValuesTestData")
    void values_shouldReturnExpectedResult(Set<Training> existingTraining) {
        fillStorage(existingTraining);

        var actualTrainings = testObject.values();

        assertEquals(existingTraining, new HashSet<>(actualTrainings));
    }

    private void fillStorage(Collection<Training> trainings) {
        trainings.forEach(training -> testObject.put(training.getUid(), training));
    }
}
