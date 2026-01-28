package com.epam.gym.repository.trainer;

import com.epam.gym.GymApplication;
import com.epam.gym.domain.user.Trainer;
import com.epam.gym.storage.trainer.InMemoryTrainerStorage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InMemoryTrainerRepositoryTest {

    private static final UUID UUID_1 = UUID.randomUUID();
    private static final UUID UUID_2 = UUID.randomUUID();
    private static final UUID UUID_3 = UUID.randomUUID();
    private static final UUID NON_EXISTENT_UUID = UUID.randomUUID();
    private static final String FIRSTNAME = "John";
    private static final String LASTNAME = "Doe";
    private static final String OTHER_FIRSTNAME = "firstname";
    private static final String OTHER_LASTNAME = "lastname";
    private static final String USERNAME = String.join(GymApplication.DEFAULT_USERNAME_DELIMITER,
        FIRSTNAME,
        LASTNAME);
    private static final Trainer TRAINER_1 = Trainer.builder()
        .uid(UUID_1)
        .firstName(FIRSTNAME)
        .lastName(LASTNAME)
        .username(USERNAME)
        .build();
    private static final Trainer TRAINER_2 = Trainer.builder()
        .uid(UUID_2)
        .firstName(FIRSTNAME)
        .lastName(LASTNAME)
        .username(USERNAME)
        .build();
    private static final Trainer TRAINER_OTHER = Trainer.builder()
        .uid(UUID_3)
        .firstName(OTHER_FIRSTNAME)
        .lastName(OTHER_LASTNAME)
        .username(USERNAME)
        .build();

    @Mock
    private InMemoryTrainerStorage storage;

    @InjectMocks
    private InMemoryTrainerRepository testObject;

    static Stream<Trainer> provideSaveTestData() {
        return Stream.of(
            TRAINER_1,
            TRAINER_OTHER
        );
    }

    @ParameterizedTest
    @MethodSource("provideSaveTestData")
    void save_shouldAddOrUpdateTrainerInStorage(Trainer trainerToSave) {
        testObject.save(trainerToSave);

        verify(storage, times(1))
            .put(trainerToSave.getUid(), trainerToSave);
    }

    static Stream<Arguments> provideFindByFirstNameAndLastNameTestData() {
        return Stream.of(
            Arguments.of(
                FIRSTNAME,
                LASTNAME,
                List.of(),
                List.of()
            ),
            Arguments.of(
                FIRSTNAME,
                LASTNAME,
                List.of(TRAINER_1),
                List.of(TRAINER_1)
            ),
            Arguments.of(
                FIRSTNAME,
                LASTNAME,
                List.of(TRAINER_1, TRAINER_2),
                List.of(TRAINER_1, TRAINER_2)
            ),
            Arguments.of(
                FIRSTNAME,
                LASTNAME,
                List.of(TRAINER_OTHER),
                List.of()
            ),
            Arguments.of(
                OTHER_FIRSTNAME,
                OTHER_LASTNAME,
                List.of(TRAINER_1, TRAINER_2, TRAINER_OTHER),
                List.of(TRAINER_OTHER)
            )
        );
    }

    @ParameterizedTest
    @MethodSource("provideFindByFirstNameAndLastNameTestData")
    void findByFirstNameAndLastName_shouldReturnExpectedResult(String firstName,
                                                               String lastName,
                                                               List<Trainer> storedTrainers,
                                                               List<Trainer> expected) {
        when(storage.values())
            .thenReturn(storedTrainers);

        var result = testObject.findByFirstNameAndLastName(firstName, lastName);

        assertEquals(expected, result);
        verify(storage, times(1))
            .values();
    }

    @Test
    void findByUid_shouldReturnTrainer_whenTrainerExists() {
        when(storage.get(UUID_1))
            .thenReturn(Optional.of(TRAINER_1));

        var result = testObject.findByUid(UUID_1);

        assertEquals(TRAINER_1, result.orElseThrow());
        verify(storage, times(1))
            .get(UUID_1);
    }

    static Stream<UUID> provideFindByUidNegativeTestData() {
        return Stream.of(
            NON_EXISTENT_UUID,
            UUID_1
        );
    }

    @ParameterizedTest
    @MethodSource("provideFindByUidNegativeTestData")
    void findByUid_shouldReturnEmptyOptional_whenTrainerNotExists(UUID uidToFind) {
        when(storage.get(uidToFind))
            .thenReturn(Optional.empty());

        var result = testObject.findByUid(uidToFind);

        assertTrue(result.isEmpty());
        verify(storage, times(1))
            .get(uidToFind);
    }
}
