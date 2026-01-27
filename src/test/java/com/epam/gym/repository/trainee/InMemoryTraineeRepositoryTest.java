package com.epam.gym.repository.trainee;

import com.epam.gym.GymApplication;
import com.epam.gym.domain.user.Trainee;
import com.epam.gym.storage.InMemoryStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.swing.*;
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
class InMemoryTraineeRepositoryTest {

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
    private static final Trainee TRAINEE_3 = Trainee.builder()
        .uid(UUID_3)
        .firstName(OTHER_FIRSTNAME)
        .lastName(OTHER_LASTNAME)
        .username(USERNAME)
        .build();
    private static final Trainee UPDATED_TRAINEE = Trainee.builder()
        .uid(UUID_1)
        .firstName(UPDATED_FIRSTNAME)
        .lastName(UPDATED_LASTNAME)
        .username(USERNAME)
        .build();

    @Mock
    private InMemoryStorage inMemoryStorage;

    @InjectMocks
    private InMemoryTraineeRepository testObject;

    private Map<UUID, Trainee> traineeStorage;

    @BeforeEach
    void setUp() {
        traineeStorage = new HashMap<>();
        when(inMemoryStorage.getTraineeStorage()).thenReturn(traineeStorage);
    }

    static Stream<Arguments> provideSaveTestData() {
        return Stream.of(
            Arguments.of(List.of(), TRAINEE_1, 1),
            Arguments.of(List.of(TRAINEE_1), TRAINEE_2, 2),
            Arguments.of(List.of(TRAINEE_1), UPDATED_TRAINEE, 1)
        );
    }

    @ParameterizedTest
    @MethodSource("provideSaveTestData")
    void save_shouldAddOrUpdateTraineeInStorage(
        List<Trainee> existingTrainees,
        Trainee traineeToSave,
        int expectedSize
    ) {
        fillStorage(existingTrainees);

        testObject.save(traineeToSave);

        assertEquals(expectedSize, traineeStorage.size());
        assertEquals(traineeToSave, traineeStorage.get(traineeToSave.getUid()));
        verify(inMemoryStorage, times(1)).getTraineeStorage();
    }

    static Stream<Arguments> provideFindByFirstNameAndLastNameTestData() {
        return Stream.of(
            Arguments.of(FIRSTNAME, LASTNAME, List.of()),
            Arguments.of(FIRSTNAME, LASTNAME, List.of(TRAINEE_1)),
            Arguments.of(FIRSTNAME, LASTNAME, List.of(TRAINEE_1, TRAINEE_2))
        );
    }

    @ParameterizedTest
    @MethodSource("provideFindByFirstNameAndLastNameTestData")
    void findByFirstNameAndLastName_shouldReturnExpectedResult(
        String firstName,
        String lastName,
        List<Trainee> expected
    ) {
        fillStorage(expected);

        var result = testObject.findByFirstNameAndLastName(firstName, lastName);

        assertEquals(expected.size(), result.size());
        assertTrue(result.containsAll(expected));
        verify(inMemoryStorage, times(1)).getTraineeStorage();
    }

    static Stream<Arguments> provideFindByUidTestData() {
        return Stream.of(
            Arguments.of(UUID_1, List.of(TRAINEE_1), true),
            Arguments.of(NON_EXISTENT_UUID, List.of(TRAINEE_1), false),
            Arguments.of(NON_EXISTENT_UUID, List.of(), false)
        );
    }

    @ParameterizedTest
    @MethodSource("provideFindByUidTestData")
    void findByUid_shouldReturnExpectedResult(
        UUID uidToFind,
        List<Trainee> existingTrainees,
        boolean shouldBePresent
    ) {
        fillStorage(existingTrainees);

        var result = testObject.findByUid(uidToFind);

        assertEquals(shouldBePresent, result.isPresent());
        if (shouldBePresent) {
            assertEquals(existingTrainees.get(0), result.get());
        }
        verify(inMemoryStorage, times(1)).getTraineeStorage();
    }

    static Stream<Arguments> provideDeleteByUidTestData() {
        return Stream.of(
            Arguments.of(UUID_1, List.of(TRAINEE_1), 0),
            Arguments.of(UUID_1, List.of(TRAINEE_1, TRAINEE_2), 1),
            Arguments.of(NON_EXISTENT_UUID, List.of(TRAINEE_1), 1),
            Arguments.of(NON_EXISTENT_UUID, List.of(), 0)
        );
    }

    @ParameterizedTest
    @MethodSource("provideDeleteByUidTestData")
    void deleteByUid_shouldDeleteTraineeIfExists(
        UUID uidToDelete,
        List<Trainee> existingTrainees,
        int expectedSizeAfterDelete
    ) {
        fillStorage(existingTrainees);

        testObject.deleteByUid(uidToDelete);

        assertEquals(expectedSizeAfterDelete, traineeStorage.size());
        verify(inMemoryStorage, times(1)).getTraineeStorage();
    }

    private void fillStorage(List<Trainee> trainees) {
        trainees.forEach(trainee -> traineeStorage.put(trainee.getUid(), trainee));
    }
}
