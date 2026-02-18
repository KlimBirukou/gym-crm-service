package com.epam.gym.repository.domain.trainer;

import com.epam.gym.domain.user.Trainer;
import com.epam.gym.repository.entity.TrainerEntity;
import com.epam.gym.repository.jpa.trainer.ITrainerEntityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.convert.ConversionService;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
public class JpaTrainerRepositoryTest {

    private static final String USERNAME = "username";
    private static final String FIRSTNAME = "firstname";
    private static final String LASTNAME = "lastname";
    private static final Trainer TRAINER_1 = Trainer.builder().username(USERNAME).build();
    private static final Trainer TRAINER_2 = Trainer.builder().username(USERNAME).build();
    private static final TrainerEntity TRAINER_ENTITY_1 = new TrainerEntity();
    private static final TrainerEntity TRAINER_ENTITY_2 = new TrainerEntity();

    @Mock
    private ITrainerEntityRepository repository;
    @Mock
    private ConversionService conversionService;

    private JpaTrainerRepository testObject;

    @BeforeEach
    void setUp() {
        testObject = new JpaTrainerRepository(
            repository,
            conversionService
        );
    }

    private static Stream<Boolean> provideBooleanData() {
        return Stream.of(true, false);
    }

    @ParameterizedTest
    @MethodSource("provideBooleanData")
    void existByUsername_shouldReturnBoolean_whenArgumentNotNull(boolean isExist) {
        doReturn(isExist).when(repository).existsByUserUsername(USERNAME);

        var result = testObject.existsByUsername(USERNAME);

        assertEquals(isExist, result);

        assertNoUnexpectedInteractions();
    }

    @ParameterizedTest
    @NullSource
    void existByUsername_shouldThrowException_whenArgumentNull(String username) {
        assertThrows(NullPointerException.class, () -> testObject.existsByUsername(username));

        assertNoUnexpectedInteractions();
    }


    @Test
    void getByUsername_shouldReturnOptionalWithTrainer_whenEntityExist() {
        doReturn(Optional.of(TRAINER_ENTITY_1)).when(repository).findByUserUsername(USERNAME);
        doReturn(TRAINER_1).when(conversionService).convert(TRAINER_ENTITY_1, Trainer.class);

        var result = testObject.getByUsername(USERNAME);

        assertTrue(result.isPresent());
        assertSame(TRAINER_1, result.get());

        assertNoUnexpectedInteractions();
    }

    @Test
    void getByUsername_shouldReturnOptionalEmpty_whenEntityNotExist() {
        doReturn(Optional.empty()).when(repository).findByUserUsername(USERNAME);

        var result = testObject.getByUsername(USERNAME);

        assertTrue(result.isEmpty());

        assertNoUnexpectedInteractions();
    }

    @ParameterizedTest
    @NullSource
    void getByUsername_shouldThrowException_whenArgumentNull(String username) {
        assertThrows(NullPointerException.class, () -> testObject.getByUsername(username));

        assertNoUnexpectedInteractions();
    }


    @Test
    void save_shouldSaveEntity() {
        doReturn(TRAINER_ENTITY_1).when(conversionService).convert(TRAINER_1, TrainerEntity.class);

        testObject.save(TRAINER_1);

        verify(repository).save(TRAINER_ENTITY_1);

        assertNoUnexpectedInteractions();
    }

    @ParameterizedTest
    @NullSource
    void save_shouldThrowException_whenArgumentNull(Trainer trainer) {
        assertThrows(NullPointerException.class, () -> testObject.save(trainer));

        assertNoUnexpectedInteractions();
    }


    private static Stream<Arguments> provideTestData() {
        return Stream.of(
            Arguments.of(List.of(), List.of()),
            Arguments.of(List.of(TRAINER_ENTITY_1), List.of(TRAINER_1)),
            Arguments.of(List.of(TRAINER_ENTITY_1, TRAINER_ENTITY_2), List.of(TRAINER_1, TRAINER_2))
        );
    }

    @ParameterizedTest
    @MethodSource("provideTestData")
    void getByFirstNameAndLastName_shouldReturnTrainers(List<TrainerEntity> entities, List<Trainer> trainers) {
        doReturn(entities).when(repository).findByUserFirstNameAndUserLastName(FIRSTNAME, LASTNAME);
        IntStream.range(0, entities.size()).forEach(i ->
            doReturn(trainers.get(i)).when(conversionService).convert(entities.get(i), Trainer.class)
        );

        var result = testObject.getByFirstNameAndLastName(FIRSTNAME, LASTNAME);

        assertEquals(trainers.size(), result.size());
        assertEquals(trainers, result);

        assertNoUnexpectedInteractions();
    }

    private static Stream<Arguments> provideNullArguments() {
        return Stream.of(
            Arguments.of(null, FIRSTNAME),
            Arguments.of(LASTNAME, null),
            Arguments.of(null, null)
        );
    }

    @ParameterizedTest
    @MethodSource("provideNullArguments")
    void getByFirstNameAndLastName_shouldThrowException_whenArgumentsNull(String firstname, String lastname) {
        assertThrows(NullPointerException.class, () -> testObject.getByFirstNameAndLastName(firstname, lastname));

        assertNoUnexpectedInteractions();
    }


    private void assertNoUnexpectedInteractions() {
        verifyNoMoreInteractions(
            repository,
            conversionService
        );
    }
}
