package com.epam.gym.service.generator.name.supplier;

import com.epam.gym.GymApplication;
import com.epam.gym.domain.user.Trainer;
import com.epam.gym.repository.trainer.ITrainerRepository;
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
class TrainerUsernameSupplierTest {

    private static final int SUFFIX_1 = 1;
    private static final String FIRSTNAME = "firstname";
    private static final String LASTNAME = "lastname";
    private static final String USERNAME_WITHOUT_SUFFIX = String.join(GymApplication.DEFAULT_USERNAME_DELIMITER,
        FIRSTNAME,
        LASTNAME);
    private static final String USERNAME_WITH_SUFFIX_1 = String.join(GymApplication.DEFAULT_USERNAME_DELIMITER,
        FIRSTNAME,
        LASTNAME,
        String.valueOf(SUFFIX_1));
    private static final Trainer TRAINER_WITHOUT_SUFFIX = Trainer.builder()
        .firstName(FIRSTNAME)
        .lastName(LASTNAME)
        .username(USERNAME_WITHOUT_SUFFIX)
        .build();
    private static final Trainer TRAINER_WITH_SUFFIX_1 = Trainer.builder()
        .firstName(FIRSTNAME)
        .lastName(LASTNAME)
        .username(USERNAME_WITH_SUFFIX_1)
        .build();
    private static final List<Trainer> EMPTY_TRAINER_LIST = List.of();
    private static final List<Trainer> LIST_WITH_TRAINER = List.of(TRAINER_WITHOUT_SUFFIX);
    private static final List<Trainer> LIST_WITH_TRAINERS = List.of(TRAINER_WITHOUT_SUFFIX, TRAINER_WITH_SUFFIX_1);
    private static final List<String> EMPTY_USERNAME_LIST = List.of();
    private static final List<String> LIST_WITH_USERNAME = List.of(USERNAME_WITHOUT_SUFFIX);
    private static final List<String> LIST_WITH_USERNAMES = List.of(USERNAME_WITHOUT_SUFFIX, USERNAME_WITH_SUFFIX_1);

    @Mock
    private ITrainerRepository trainerRepository;

    @InjectMocks
    private TrainerUsernameSupplier testObject;

    static Stream<Arguments> supplyTrainersFromRepository() {
        return Stream.of(
            Arguments.of(EMPTY_TRAINER_LIST, EMPTY_USERNAME_LIST),
            Arguments.of(LIST_WITH_TRAINER, LIST_WITH_USERNAME),
            Arguments.of(LIST_WITH_TRAINERS, LIST_WITH_USERNAMES)
        );
    }

    @ParameterizedTest
    @MethodSource("supplyTrainersFromRepository")
    void supply_shouldReturnExpectedResult(List<Trainer> trainees, List<String> usernames) {
        when(trainerRepository.findByFirstNameAndLastName(FIRSTNAME, LASTNAME))
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
