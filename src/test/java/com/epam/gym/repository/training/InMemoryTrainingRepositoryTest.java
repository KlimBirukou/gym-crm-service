package com.epam.gym.repository.training;

import com.epam.gym.domain.training.Training;
import com.epam.gym.storage.training.InMemoryTrainingStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class InMemoryTrainingRepositoryTest {

    private static final UUID UUID_1 = UUID.randomUUID();
    private static final UUID UUID_2 = UUID.randomUUID();
    private static final UUID UUID_3 = UUID.randomUUID();
    private static final LocalDate DATE_1 = LocalDate.of(2020, 1, 1);
    private static final LocalDate DATE_2 = LocalDate.of(2020, 1, 2);
    private static final LocalDate DATE_3 = LocalDate.of(2020, 1, 3);
    private static final Training TRAINING_1 = Training.builder()
        .trainingUid(UUID_1)
        .trainingDate(DATE_1)
        .build();
    private static final Training UPDATED_TRAINING_1 = Training.builder()
        .trainingUid(UUID_1)
        .trainingDate(DATE_2)
        .build();
    private static final Training TRAINING_2 = Training.builder()
        .trainingUid(UUID_2)
        .trainingDate(DATE_2)
        .build();
    private static final Training TRAINING_3 = Training.builder()
        .trainingUid(UUID_3)
        .trainingDate(DATE_3)
        .build();

    @Spy
    private InMemoryTrainingStorage storage;

    @InjectMocks
    private InMemoryTrainingRepository testObject;

    @BeforeEach
    void setUp() {
        storage.clear();
    }

    static Stream<Arguments> provideSaveTestData() {
        return Stream.of(
            Arguments.of(List.of(), TRAINING_1, 1),
            Arguments.of(List.of(TRAINING_1), TRAINING_2, 2),
            Arguments.of(List.of(TRAINING_1), UPDATED_TRAINING_1, 1)
        );
    }

    @ParameterizedTest
    @MethodSource("provideSaveTestData")
    void save_shouldAddOrUpdateTrainingInStorage(
        List<Training> existingTrainings,
        Training trainingToSave,
        int expectedSize
    ) {
        fillStorage(existingTrainings);

        testObject.save(trainingToSave);

        assertEquals(expectedSize, storage.size());
        assertEquals(trainingToSave, storage.get(trainingToSave.getTrainingUid()).orElseThrow());
        verify(storage, times(1)).put(trainingToSave.getTrainingUid(), trainingToSave);
    }

    static Stream<Arguments> provideFindByLocalDAteTestData() {
        return Stream.of(
            Arguments.of(DATE_1, List.of(), List.of()),
            Arguments.of(DATE_1, List.of(), List.of(TRAINING_2, TRAINING_3)),
            Arguments.of(DATE_1, List.of(TRAINING_1), List.of(TRAINING_1)),
            Arguments.of(DATE_1, List.of(TRAINING_1), List.of(TRAINING_1, TRAINING_2, TRAINING_3))
        );
    }

    @ParameterizedTest
    @MethodSource("provideFindByLocalDAteTestData")
    void findByLocalDate_shouldReturnExpectedResult(
        LocalDate date,
        List<Training> expected,
        List<Training> existingTrainings
    ) {
        fillStorage(existingTrainings);

        var result = testObject.findByLocalDate(date);

        assertEquals(expected.size(), result.size());
        assertTrue(result.containsAll(expected));
        verify(storage, times(1)).values();
    }

    private void fillStorage(List<Training> trainings) {
        trainings.forEach(training -> storage.put(training.getTrainingUid(), training));
    }
}
