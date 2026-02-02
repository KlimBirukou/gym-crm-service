package com.epam.gym.repository.training;

import com.epam.gym.domain.training.Training;
import com.epam.gym.storage.training.InMemoryTrainingStorage;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
        .uid(UUID_1)
        .date(DATE_1)
        .build();
    private static final Training TRAINING_2 = Training.builder()
        .uid(UUID_2)
        .date(DATE_2)
        .build();
    private static final Training TRAINING_OTHER = Training.builder()
        .uid(UUID_3)
        .date(DATE_3)
        .build();

    @Mock
    private InMemoryTrainingStorage storage;

    @InjectMocks
    private InMemoryTrainingRepository testObject;

    static Stream<Training> provideSaveTestData() {
        return Stream.of(
            TRAINING_1,
            TRAINING_OTHER
        );
    }

    @ParameterizedTest
    @MethodSource("provideSaveTestData")
    void save_shouldAddOrUpdateTrainingInStorage(Training trainingToSave) {
        testObject.save(trainingToSave);

        verify(storage, times(1))
            .put(trainingToSave.getUid(), trainingToSave);
    }

    static Stream<Arguments> provideFindByLocalDateTestData() {
        return Stream.of(
            Arguments.of(
                DATE_1,
                List.of(),
                List.of()
            ),
            Arguments.of(
                DATE_1,
                List.of(TRAINING_1),
                List.of(TRAINING_1)
            ),
            Arguments.of(
                DATE_1,
                List.of(TRAINING_1, TRAINING_2),
                List.of(TRAINING_1)
            ),
            Arguments.of(
                DATE_1,
                List.of(TRAINING_OTHER),
                List.of()
            ),
            Arguments.of(
                DATE_2,
                List.of(TRAINING_1, TRAINING_2, TRAINING_OTHER),
                List.of(TRAINING_2)
            )
        );
    }

    @ParameterizedTest
    @MethodSource("provideFindByLocalDateTestData")
    void findByLocalDate_shouldReturnExpectedResult(LocalDate date,
                                                    List<Training> storedTrainings,
                                                    List<Training> expected) {
        when(storage.values())
            .thenReturn(storedTrainings);

        var result = testObject.findByLocalDate(date);

        assertEquals(expected, result);
        verify(storage, times(1))
            .values();
    }
}
