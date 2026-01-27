package com.epam.gym.repository.trainer;

import com.epam.gym.GymApplication;
import com.epam.gym.domain.user.Trainer;
import com.epam.gym.storage.InMemoryStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
class InMemoryTrainerRepositoryTest {

    private static final UUID UUID_1 = UUID.randomUUID();
    private static final UUID UUID_2 = UUID.randomUUID();
    private static final UUID UUID_3 = UUID.randomUUID();
    private static final UUID NON_EXISTENT_UUID = UUID.randomUUID();
    private static final String FIRSTNAME = "John";
    private static final String LASTNAME = "Doe";
    private static final String OTHER_FIRSTNAME = "firstname";
    private static final String OTHER_LASTNAME = "lastname";
    private static final String UPDATED_FIRSTNAME = "JohnNew";
    private static final String UPDATED_LASTNAME = "DoeNew";
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
    private static final Trainer TRAINER_3 = Trainer.builder()
        .uid(UUID_3)
        .firstName(OTHER_FIRSTNAME)
        .lastName(OTHER_LASTNAME)
        .username(USERNAME)
        .build();
    private static final Trainer UPDATED_TRAINER = Trainer.builder()
        .uid(UUID_1)
        .firstName(UPDATED_FIRSTNAME)
        .lastName(UPDATED_LASTNAME)
        .username(USERNAME)
        .build();

    @Mock
    private InMemoryStorage inMemoryStorage;

    @InjectMocks
    private InMemoryTrainerRepository testObject;

    private Map<UUID, Trainer> trainerStorage;

    @BeforeEach
    void setUp() {
        trainerStorage = new HashMap<>();
        when(inMemoryStorage.getTrainerStorage()).thenReturn(trainerStorage);
    }

    static Stream<Arguments> provideSaveTestData() {
        return Stream.of(
            Arguments.of(List.of(), TRAINER_1, 1),
            Arguments.of(List.of(TRAINER_1), TRAINER_2, 2),
            Arguments.of(List.of(TRAINER_1), UPDATED_TRAINER, 1)
        );
    }

    @ParameterizedTest
    @MethodSource("provideSaveTestData")
    void save_shouldAddOrUpdateTrainerInStorage(
        List<Trainer> existingTrainers,
        Trainer trainerToSave,
        int expectedSize
    ) {
        fillStorage(existingTrainers);

        testObject.save(trainerToSave);

        assertEquals(expectedSize, trainerStorage.size());
        assertEquals(trainerToSave, trainerStorage.get(trainerToSave.getUid()));
        verify(inMemoryStorage, times(1)).getTrainerStorage();
    }


    static Stream<Arguments> provideFindByFirstNameAndLastNameTestData() {
        return Stream.of(
            Arguments.of(FIRSTNAME, LASTNAME, List.of()),
            Arguments.of(FIRSTNAME, LASTNAME, List.of(TRAINER_1)),
            Arguments.of(FIRSTNAME, LASTNAME, List.of(TRAINER_1, TRAINER_2))
        );
    }

    @ParameterizedTest
    @MethodSource("provideFindByFirstNameAndLastNameTestData")
    void findByFirstNameAndLastName_shouldReturnExpectedResult(
        String firstName,
        String lastName,
        List<Trainer> expected
    ) {
        fillStorage(expected);
        trainerStorage.put(TRAINER_3.getUid(), TRAINER_3);

        var result = testObject.findByFirstNameAndLastName(firstName, lastName);

        assertEquals(expected.size(), result.size());
        assertTrue(result.containsAll(expected));
        verify(inMemoryStorage, times(1)).getTrainerStorage();
    }

    static Stream<Arguments> provideFindByUidTestData() {
        return Stream.of(
            Arguments.of(UUID_1, List.of(TRAINER_1), true),
            Arguments.of(NON_EXISTENT_UUID, List.of(TRAINER_1), false),
            Arguments.of(NON_EXISTENT_UUID, List.of(), false)
        );
    }

    @ParameterizedTest
    @MethodSource("provideFindByUidTestData")
    void findByUid_shouldReturnExpectedResult(
        UUID uidToFind,
        List<Trainer> existingTrainers,
        boolean shouldBePresent
    ) {
        fillStorage(existingTrainers);

        var result = testObject.findByUid(uidToFind);

        assertEquals(shouldBePresent, result.isPresent());
        if (shouldBePresent) {
            assertEquals(existingTrainers.get(0), result.get());
        }
        verify(inMemoryStorage, times(1)).getTrainerStorage();
    }

    private void fillStorage(List<Trainer> trainers) {
        trainers.forEach(trainer -> trainerStorage.put(trainer.getUid(), trainer));
    }
}
