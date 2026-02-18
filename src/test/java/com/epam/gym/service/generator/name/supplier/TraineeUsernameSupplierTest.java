package com.epam.gym.service.generator.name.supplier;

import com.epam.gym.domain.user.Trainee;
import com.epam.gym.mother.TraineeMother;
import com.epam.gym.mother.UsernameMother;
import com.epam.gym.repository.domain.trainee.ITraineeRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
    private static final String USERNAME_WITHOUT_SUFFIX = UsernameMother.get();
    private static final String USERNAME_WITH_SUFFIX_1 = UsernameMother.get(SUFFIX_1);
    private static final Trainee TRAINEE_WITHOUT_SUFFIX =
        TraineeMother.get(UID, FIRSTNAME, LASTNAME, USERNAME_WITHOUT_SUFFIX);
    private static final Trainee TRAINEE_WITH_SUFFIX_1 =
        TraineeMother.get(UID, FIRSTNAME, LASTNAME, USERNAME_WITH_SUFFIX_1);
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
        when(traineeRepository.getByFirstAndNameLastName(FIRSTNAME, LASTNAME))
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
