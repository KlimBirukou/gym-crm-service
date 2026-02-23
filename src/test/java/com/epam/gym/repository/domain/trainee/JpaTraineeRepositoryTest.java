package com.epam.gym.repository.domain.trainee;

import com.epam.gym.domain.user.Trainee;
import com.epam.gym.repository.entity.TraineeEntity;
import com.epam.gym.repository.jpa.trainee.ITraineeEntityRepository;
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
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class JpaTraineeRepositoryTest {

    private static final String USERNAME = "username";
    private static final String FIRSTNAME = "firstname";
    private static final String LASTNAME = "lastname";
    private static final Trainee TRAINEE_1 = Trainee.builder().username(USERNAME).build();
    private static final Trainee TRAINEE_2 = Trainee.builder().username(USERNAME).build();
    private static final TraineeEntity TRAINEE_ENTITY_1 = new TraineeEntity();
    private static final TraineeEntity TRAINEE_ENTITY_2 = new TraineeEntity();

    @Mock
    private ITraineeEntityRepository repository;
    @Mock
    private ConversionService conversionService;

    private JpaTraineeRepository testObject;

    @BeforeEach
    void setUp() {
        testObject = new JpaTraineeRepository(
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
    void getByUsername_shouldReturnOptionalWithTrainee_whenEntityExist() {
        doReturn(Optional.of(TRAINEE_ENTITY_1)).when(repository).findByUserUsername(USERNAME);
        doReturn(TRAINEE_1).when(conversionService).convert(TRAINEE_ENTITY_1, Trainee.class);

        var result = testObject.getByUsername(USERNAME);

        assertTrue(result.isPresent());
        assertSame(TRAINEE_1, result.get());

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
        doReturn(TRAINEE_ENTITY_1).when(conversionService).convert(TRAINEE_1, TraineeEntity.class);

        testObject.save(TRAINEE_1);

        verify(repository).save(TRAINEE_ENTITY_1);

        assertNoUnexpectedInteractions();
    }

    @ParameterizedTest
    @NullSource
    void save_shouldThrowException_whenArgumentNull(Trainee trainee) {
        assertThrows(NullPointerException.class, () -> testObject.save(trainee));

        assertNoUnexpectedInteractions();
    }


    @Test
    void delete_shouldDeleteEntity() {
        doNothing().when(repository).deleteByUserUsername(USERNAME);

        testObject.deleteByUsername(USERNAME);

        assertNoUnexpectedInteractions();
    }

    @ParameterizedTest
    @NullSource
    void delete_shouldThrowException_whenArgumentNull(String username) {
        assertThrows(NullPointerException.class, () -> testObject.deleteByUsername(username));

        assertNoUnexpectedInteractions();
    }


    private static Stream<Arguments> provideTestData() {
        return Stream.of(
            Arguments.of(List.of(), List.of()),
            Arguments.of(List.of(TRAINEE_ENTITY_1), List.of(TRAINEE_1)),
            Arguments.of(List.of(TRAINEE_ENTITY_1, TRAINEE_ENTITY_2), List.of(TRAINEE_1, TRAINEE_2))
        );
    }

    @ParameterizedTest
    @MethodSource("provideTestData")
    void getByFirstNameAndLastName_shouldReturnTrainees(List<TraineeEntity> entities, List<Trainee> trainees) {
        doReturn(entities).when(repository).findByUserFirstNameAndUserLastName(FIRSTNAME, LASTNAME);
        IntStream.range(0, entities.size()).forEach(i ->
            doReturn(trainees.get(i)).when(conversionService).convert(entities.get(i), Trainee.class)
        );

        var result = testObject.getByFirstNameAndLastName(FIRSTNAME, LASTNAME);

        assertEquals(trainees.size(), result.size());
        assertEquals(trainees, result);

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
