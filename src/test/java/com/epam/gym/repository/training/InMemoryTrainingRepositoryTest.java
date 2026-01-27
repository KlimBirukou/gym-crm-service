package com.epam.gym.repository.training;

import com.epam.gym.domain.training.Training;
import com.epam.gym.storage.InMemoryStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

    @Mock
    private InMemoryStorage inMemoryStorage;

    @InjectMocks
    private InMemoryTrainingRepository testObject;

    private Map<UUID, Training> trainingStorage;

    @BeforeEach
    void setUp() {
        trainingStorage = new HashMap<>();
        when(inMemoryStorage.getTrainingStorage()).thenReturn(trainingStorage);
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

        assertEquals(expectedSize, trainingStorage.size());
        assertEquals(trainingToSave, trainingStorage.get(trainingToSave.getTrainingUid()));
        verify(inMemoryStorage, times(1)).getTrainingStorage();
    }

    static Stream<Arguments> provideFindByLocalDAteTestData() {
        return Stream.of(
            Arguments.of(DATE_1, List.of()),
            Arguments.of(DATE_1, List.of(TRAINING_1))
        );
    }

    @ParameterizedTest
    @MethodSource("provideFindByLocalDAteTestData")
    void findByLocalDate_shouldReturnExpectedResult(
        LocalDate date,
        List<Training> expected
    ) {
        fillStorage(expected);
        trainingStorage.put(TRAINING_3.getTrainingUid(), TRAINING_3);

        var result = testObject.findByLocalDate(date);

        assertEquals(expected.size(), result.size());
        assertTrue(result.containsAll(expected));
        verify(inMemoryStorage, times(1)).getTrainingStorage();
    }

    private void fillStorage(List<Training> trainings) {
        trainings.forEach(training -> trainingStorage.put(training.getTrainingUid(), training));
    }
}
