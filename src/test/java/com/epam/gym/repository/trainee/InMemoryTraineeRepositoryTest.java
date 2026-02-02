package com.epam.gym.repository.trainee;

import com.epam.gym.GymApplication;
import com.epam.gym.domain.user.Trainee;
import com.epam.gym.storage.trainee.InMemoryTraineeStorage;
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
class InMemoryTraineeRepositoryTest {

    private static final UUID UUID_1 = UUID.randomUUID();
    private static final UUID UUID_2 = UUID.randomUUID();
    private static final UUID UUID_3 = UUID.randomUUID();
    private static final UUID NON_EXISTENT_UUID = UUID.randomUUID();
    private static final String FIRSTNAME = "firstname";
    private static final String LASTNAME = "lastname";
    private static final String OTHER_FIRSTNAME = "other firstname";
    private static final String OTHER_LASTNAME = "other lastname";
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
    private static final Trainee TRAINEE_OTHER = Trainee.builder()
        .uid(UUID_3)
        .firstName(OTHER_FIRSTNAME)
        .lastName(OTHER_LASTNAME)
        .username(USERNAME)
        .build();

    @Mock
    private InMemoryTraineeStorage storage;

    @InjectMocks
    private InMemoryTraineeRepository testObject;

    static Stream<Trainee> provideSaveTestData() {
        return Stream.of(
            TRAINEE_1,
            TRAINEE_OTHER
        );
    }

    @ParameterizedTest
    @MethodSource("provideSaveTestData")
    void save_shouldAddOrUpdateTraineeInStorage(Trainee traineeToSave) {
        testObject.save(traineeToSave);

        verify(storage, times(1))
            .put(traineeToSave.getUid(), traineeToSave);
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
                List.of(TRAINEE_1),
                List.of(TRAINEE_1)
            ),
            Arguments.of(
                FIRSTNAME,
                LASTNAME,
                List.of(TRAINEE_1, TRAINEE_2),
                List.of(TRAINEE_1, TRAINEE_2)
            ),
            Arguments.of(
                FIRSTNAME,
                LASTNAME,
                List.of(TRAINEE_OTHER),
                List.of()
            ),
            Arguments.of(
                OTHER_FIRSTNAME,
                OTHER_LASTNAME,
                List.of(TRAINEE_1, TRAINEE_2, TRAINEE_OTHER),
                List.of(TRAINEE_OTHER)
            )
        );
    }

    @ParameterizedTest
    @MethodSource("provideFindByFirstNameAndLastNameTestData")
    void findByFirstNameAndLastName_shouldReturnExpectedResult(String firstName,
                                                               String lastName,
                                                               List<Trainee> storedTrainees,
                                                               List<Trainee> expected) {
        when(storage.values())
            .thenReturn(storedTrainees);

        var result = testObject.findByFirstNameAndLastName(firstName, lastName);

        assertEquals(expected, result);
        verify(storage, times(1))
            .values();
    }

    @Test
    void findByUid_shouldReturnTrainee_whenTraineeExists() {
        when(storage.get(UUID_1))
            .thenReturn(Optional.of(TRAINEE_1));

        var result = testObject.findByUid(UUID_1);

        assertEquals(TRAINEE_1, result.orElseThrow());
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
    void findByUid_shouldReturnEmptyOptional_whenTraineeNotExists(UUID uidToFind) {
        when(storage.get(uidToFind))
            .thenReturn(Optional.empty());

        var result = testObject.findByUid(uidToFind);

        assertTrue(result.isEmpty());
        verify(storage, times(1))
            .get(uidToFind);
    }

    static Stream<UUID> provideDeleteByUidTestData() {
        return Stream.of(
            UUID_1,
            NON_EXISTENT_UUID
        );
    }

    @ParameterizedTest
    @MethodSource("provideDeleteByUidTestData")
    void deleteByUid_shouldDeleteTraineeIfExists(UUID uidToDelete) {
        testObject.deleteByUid(uidToDelete);

        verify(storage, times(1))
            .remove(uidToDelete);
    }
}
