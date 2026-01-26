package com.epam.gym.service.generator.name.supplier;

import com.epam.gym.GymApplication;
import com.epam.gym.domain.user.Trainer;
import com.epam.gym.repository.trainer.ITrainerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainerUsernameSupplierTest {

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

    @Test
    void provideUsers_shouldReturnEmptyList_whenNoTrainerExist() {
        when(trainerRepository.findByFirstNameAndLastName(FIRSTNAME, LASTNAME))
            .thenReturn(EMPTY_TRAINER_LIST);

        var result = testObject.supply(FIRSTNAME, LASTNAME);

        assertEquals(EMPTY_USERNAME_LIST, result);
    }

    @Test
    void provideUsers_shouldReturnListWithOneTrainer_whenOneTrainerExist() {
        when(trainerRepository.findByFirstNameAndLastName(FIRSTNAME, LASTNAME))
            .thenReturn(LIST_WITH_TRAINER);

        var result = testObject.supply(FIRSTNAME, LASTNAME);

        assertEquals(LIST_WITH_USERNAME, result);
    }

    @Test
    void provideUsers_shouldReturnListWithManyTrainers_whenManyTrainerExist() {
        when(trainerRepository.findByFirstNameAndLastName(FIRSTNAME, LASTNAME))
            .thenReturn(LIST_WITH_TRAINERS);

        var result = testObject.supply(FIRSTNAME, LASTNAME);

        assertEquals(LIST_WITH_USERNAMES, result);
    }

    @Test
    void provideUsers_shouldThrowNpe_whenFirstNameIsNull() {
        assertThrows(NullPointerException.class,
            () -> testObject.supply(null, LASTNAME));
    }

    @Test
    void provideUsers_shouldThrowNpe_whenLastNameIsNull() {
        assertThrows(NullPointerException.class,
            () -> testObject.supply(FIRSTNAME, null));
    }

    @Test
    void provideUsers_shouldThrowNpe_whenBothNamesAreNull() {
        assertThrows(NullPointerException.class,
            () -> testObject.supply(null, null));
    }
}
