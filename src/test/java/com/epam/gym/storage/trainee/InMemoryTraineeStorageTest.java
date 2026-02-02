package com.epam.gym.storage.trainee;

import com.epam.gym.GymApplication;
import com.epam.gym.domain.user.Trainee;
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

class InMemoryTraineeStorageTest {

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
    private static final Trainee TRAINEE_1 = Trainee.builder()
        .uid(UUID_1)
        .firstName(FIRSTNAME)
        .lastName(LASTNAME)
        .username(USERNAME)
        .build();
    private static final Trainee TRAINEE_2 = Trainee.builder()
        .uid(UUID_2)
        .firstName(FIRSTNAME)
        .lastName(LASTNAME)
        .username(USERNAME)
        .build();
    private static final Trainee UPDATED_TRAINEE = Trainee.builder()
        .uid(UUID_1)
        .firstName(UPDATED_FIRSTNAME)
        .lastName(UPDATED_LASTNAME)
        .username(USERNAME)
        .build();

    private InMemoryTraineeStorage testObject;

    @BeforeEach
    void setUp() {
        testObject = new InMemoryTraineeStorage();
    }

    static Stream<Arguments> providePutTestData() {
        return Stream.of(
            Arguments.of(
                Set.of(),
                TRAINEE_1,
                Set.of(TRAINEE_1)
            ),
            Arguments.of(
                Set.of(TRAINEE_1),
                TRAINEE_2,
                Set.of(TRAINEE_1, TRAINEE_2)
            ),
            Arguments.of(
                Set.of(TRAINEE_1),
                UPDATED_TRAINEE,
                Set.of(UPDATED_TRAINEE)
            ),
            Arguments.of(
                Set.of(TRAINEE_1, TRAINEE_2),
                UPDATED_TRAINEE,
                Set.of(UPDATED_TRAINEE, TRAINEE_2)
            )
        );
    }

    @ParameterizedTest
    @MethodSource("providePutTestData")
    void put_shouldAddOrUpdateTraineeInStorage(Set<Trainee> existingTrainees,
                                               Trainee traineeToSave,
                                               Set<Trainee> expectedTrainees) {
        fillStorage(existingTrainees);

        testObject.put(traineeToSave.getUid(), traineeToSave);

        assertEquals(expectedTrainees, new HashSet<>(testObject.values()));
    }

    @Test
    void get_shouldReturnTrainee_whenTraineeExist() {
        fillStorage(Set.of(TRAINEE_1));

        var actualTrainee = testObject.get(UUID_1);

        assertEquals(TRAINEE_1, actualTrainee.orElseThrow());
    }

    static Stream<Arguments> provideGetTestData() {
        return Stream.of(
            Arguments.of(
                NON_EXISTENT_UUID,
                List.of()
            ),
            Arguments.of(
                NON_EXISTENT_UUID,
                List.of(TRAINEE_1)
            ),
            Arguments.of(
                NON_EXISTENT_UUID,
                List.of(TRAINEE_1, TRAINEE_2)
            )
        );
    }

    @ParameterizedTest
    @MethodSource("provideGetTestData")
    void get_shouldReturnExpectedResult_whenTraineeNotExist(UUID uidToFind,
                                                            List<Trainee> existingTrainees) {
        fillStorage(existingTrainees);

        var actualTrainee = testObject.get(uidToFind);

        assertTrue(actualTrainee.isEmpty());
    }

    @Test
    void remove_shouldDeleteValue() {
        testObject.put(TRAINEE_1.getUid(), TRAINEE_1);

        assertTrue(testObject.get(TRAINEE_1.getUid()).isPresent());

        testObject.remove(TRAINEE_1.getUid());

        assertTrue(testObject.get(TRAINEE_1.getUid()).isEmpty());
    }

    @Test
    void remove_shouldNotFailOnMissingKey() {
        testObject.remove(NON_EXISTENT_UUID);

        assertTrue(testObject.get(NON_EXISTENT_UUID).isEmpty());
    }

    static Stream<Set<Trainee>> provideValuesTestData() {
        return Stream.of(
            Set.of(TRAINEE_1),
            Set.of(TRAINEE_1, TRAINEE_2)
        );
    }


    @ParameterizedTest
    @EmptySource
    @MethodSource("provideValuesTestData")
    void values_shouldReturnExpectedResult(Set<Trainee> existingTrainees) {
        fillStorage(existingTrainees);

        var actualTrainees = testObject.values();

        assertEquals(existingTrainees, new HashSet<>(actualTrainees));
    }

    private void fillStorage(Collection<Trainee> trainees) {
        trainees.forEach(trainee -> testObject.put(trainee.getUid(), trainee));
    }
}
