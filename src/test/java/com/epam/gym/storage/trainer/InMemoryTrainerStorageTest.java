package com.epam.gym.storage.trainer;

import com.epam.gym.GymApplication;
import com.epam.gym.domain.user.Trainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InMemoryTrainerStorageTest {

    private static final UUID UUID_1 = UUID.randomUUID();
    private static final UUID UUID_2 = UUID.randomUUID();
    private static final UUID NON_EXISTENT_UUID = UUID.randomUUID();
    private static final String FIRSTNAME = "firstname";
    private static final String LASTNAME = "lastname";
    private static final String UPDATED_FIRSTNAME = "new firstname";
    private static final String UPDATED_LASTNAME = "new lastname";
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
    private static final Trainer UPDATED_TRAINER = Trainer.builder()
        .uid(UUID_1)
        .firstName(UPDATED_FIRSTNAME)
        .lastName(UPDATED_LASTNAME)
        .username(USERNAME)
        .build();

    private InMemoryTrainerStorage testObject;

    @BeforeEach
    void setUp() {
        testObject = new InMemoryTrainerStorage();
    }

    static Stream<Arguments> providePutTestData() {
        return Stream.of(
            Arguments.of(
                Set.of(),
                TRAINER_1,
                Set.of(TRAINER_1)
            ),
            Arguments.of(
                Set.of(TRAINER_1),
                TRAINER_2,
                Set.of(TRAINER_1, TRAINER_2)
            ),
            Arguments.of(
                Set.of(TRAINER_1),
                UPDATED_TRAINER,
                Set.of(UPDATED_TRAINER)
            ),
            Arguments.of(
                Set.of(TRAINER_1, TRAINER_2),
                UPDATED_TRAINER,
                Set.of(UPDATED_TRAINER, TRAINER_2)
            )
        );
    }

    @ParameterizedTest
    @MethodSource("providePutTestData")
    void put_shouldAddOrUpdateTraineeInStorage(Set<Trainer> existingTrainers,
                                               Trainer trainerToSave,
                                               Set<Trainer> expectedTrainees) {
        fillStorage(existingTrainers);

        testObject.put(trainerToSave.getUid(), trainerToSave);

        assertEquals(expectedTrainees, new HashSet<>(testObject.values()));
    }

    @Test
    void get_shouldReturnTrainer_whenTrainerExist() {
        fillStorage(Set.of(TRAINER_1));

        var actualTrainee = testObject.get(UUID_1);

        assertEquals(TRAINER_1, actualTrainee.orElseThrow());
    }

    static Stream<Arguments> provideGetTestData() {
        return Stream.of(
            Arguments.of(
                NON_EXISTENT_UUID,
                List.of()
            ),
            Arguments.of(
                NON_EXISTENT_UUID,
                List.of(TRAINER_1)
            ),
            Arguments.of(
                NON_EXISTENT_UUID,
                List.of(TRAINER_1, TRAINER_2)
            )
        );
    }

    @ParameterizedTest
    @MethodSource("provideGetTestData")
    void get_shouldReturnExpectedResult_whenTraineeNotExist(UUID uidToFind,
                                                            List<Trainer> existingTrainers) {
        fillStorage(existingTrainers);

        var actualTrainee = testObject.get(uidToFind);

        assertTrue(actualTrainee.isEmpty());
    }

    @Test
    void remove_shouldDeleteValue() {
        testObject.put(TRAINER_1.getUid(), TRAINER_1);

        assertTrue(testObject.get(TRAINER_1.getUid()).isPresent());

        testObject.remove(TRAINER_1.getUid());

        assertTrue(testObject.get(TRAINER_1.getUid()).isEmpty());
    }

    @Test
    void remove_shouldNotFailOnMissingKey() {
        testObject.remove(NON_EXISTENT_UUID);

        assertTrue(testObject.get(NON_EXISTENT_UUID).isEmpty());
    }

    static Stream<Set<Trainer>> provideValuesTestData() {
        return Stream.of(
            Set.of(TRAINER_1),
            Set.of(TRAINER_1, TRAINER_2)
        );
    }

    @ParameterizedTest
    @EmptySource
    @MethodSource("provideValuesTestData")
    void values_shouldReturnExpectedResult(Set<Trainer> existingTrainers) {
        fillStorage(existingTrainers);

        var actualTrainees = testObject.values();

        assertEquals(existingTrainers, new HashSet<>(actualTrainees));
    }

    private void fillStorage(Collection<Trainer> trainers) {
        trainers.forEach(trainer -> testObject.put(trainer.getUid(), trainer));
    }
}
