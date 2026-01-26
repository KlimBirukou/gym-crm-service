package com.epam.gym.service.generator.name.supplier;

import com.epam.gym.GymApplication;
import com.epam.gym.domain.user.Trainee;
import com.epam.gym.repository.trainee.ITraineeRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TraineeUsernameSupplierTest {

    private static final int SUFFIX_1 = 1;
    private static final String FIRSTNAME = "John";
    private static final String LASTNAME = "Doe";
    private static final String USERNAME_WITHOUT_SUFFIX = String.join(GymApplication.DEFAULT_USERNAME_DELIMITER,
        FIRSTNAME,
        LASTNAME);
    private static final String USERNAME_WITH_SUFFIX_1 = String.join(GymApplication.DEFAULT_USERNAME_DELIMITER,
        FIRSTNAME,
        LASTNAME,
        String.valueOf(SUFFIX_1));
    private static final Trainee TRAINEE_WITHOUT_SUFFIX = Trainee.builder()
        .firstName(FIRSTNAME)
        .lastName(LASTNAME)
        .username(USERNAME_WITHOUT_SUFFIX)
        .build();
    private static final Trainee TRAINEE_WITH_SUFFIX_1 = Trainee.builder()
        .firstName(FIRSTNAME)
        .lastName(LASTNAME)
        .username(USERNAME_WITH_SUFFIX_1)
        .build();
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
        when(traineeRepository.findByFirstNameAndLastName(FIRSTNAME, LASTNAME))
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
}
