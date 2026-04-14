package com.epam.gym.crm.service.generator.name.supplier;

import com.epam.gym.crm.domain.user.Trainee;
import com.epam.gym.crm.repository.domain.trainee.ITraineeRepository;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TraineeUsernameSupplierTest {

    private static final UUID UID = UUID.randomUUID();
    private static final int SUFFIX_1 = 1;
    private static final String FIRSTNAME = "firstname";
    private static final String LASTNAME = "lastname";
    private static final LocalDate DATE = LocalDate.of(2026, 1, 1);
    private static final String PASSWORD = "password";
    private static final String ADDRESS = "address";
    private static final String DEFAULT_USERNAME_DELIMITER = ".";
    private static final String USERNAME_WITHOUT_SUFFIX = buildUsername();
    private static final String USERNAME_WITH_SUFFIX_1 = buildUsername(SUFFIX_1);
    private static final Trainee TRAINEE_WITHOUT_SUFFIX = buildTrainee(USERNAME_WITHOUT_SUFFIX);
    private static final Trainee TRAINEE_WITH_SUFFIX_1 = buildTrainee(USERNAME_WITH_SUFFIX_1);
    private static final List<Trainee> EMPTY_TRAINEE_LIST = List.of();
    private static final List<Trainee> LIST_WITH_TRAINER = List.of(TRAINEE_WITHOUT_SUFFIX);
    private static final List<Trainee> LIST_WITH_TRAINERS = List.of(TRAINEE_WITHOUT_SUFFIX, TRAINEE_WITH_SUFFIX_1);
    private static final List<String> EMPTY_USERNAME_LIST = List.of();
    private static final List<String> LIST_WITH_USERNAME = List.of(USERNAME_WITHOUT_SUFFIX);
    private static final List<String> LIST_WITH_USERNAMES = List.of(USERNAME_WITHOUT_SUFFIX, USERNAME_WITH_SUFFIX_1);

    @Mock
    private ITraineeRepository traineeRepository;

    @InjectMocks
    private TraineeUsernameSupplier testObject;

    static Stream<Arguments> supplyTraineesFromRepository() {
        return Stream.of(
            Arguments.of(EMPTY_TRAINEE_LIST, EMPTY_USERNAME_LIST),
            Arguments.of(LIST_WITH_TRAINER, LIST_WITH_USERNAME),
            Arguments.of(LIST_WITH_TRAINERS, LIST_WITH_USERNAMES)
        );
    }

    @ParameterizedTest
    @MethodSource("supplyTraineesFromRepository")
    void supply_shouldReturnExpectedResult(List<Trainee> trainees, List<String> usernames) {
        when(traineeRepository.getByFirstNameAndLastName(FIRSTNAME, LASTNAME))
            .thenReturn(trainees);

        var result = testObject.supply(FIRSTNAME, LASTNAME);

        assertEquals(usernames, result);
    }


    static Stream<Arguments> provideNullNameArguments() {
        return Stream.of(
            Arguments.of(null, LASTNAME),
            Arguments.of(FIRSTNAME, null),
            Arguments.of(null, null)
        );
    }

    @ParameterizedTest
    @MethodSource("provideNullNameArguments")
    void shouldThrowNpe_whenArgumentsAreNull(String firstName, String lastName) {
        assertThrows(NullPointerException.class,
            () -> testObject.supply(firstName, lastName));
    }

    private static Trainee buildTrainee(String username) {
        return Trainee.builder()
            .uid(UID)
            .firstName(FIRSTNAME)
            .lastName(LASTNAME)
            .address(ADDRESS)
            .username(username)
            .password(PASSWORD)
            .birthdate(DATE)
            .active(true)
            .build();
    }

    private static String buildUsername() {
        return String.join(DEFAULT_USERNAME_DELIMITER,
            FIRSTNAME,
            LASTNAME);
    }

    private static String buildUsername(int suffix) {
        return String.join(DEFAULT_USERNAME_DELIMITER,
            FIRSTNAME,
            LASTNAME,
            String.valueOf(suffix));
    }
}
