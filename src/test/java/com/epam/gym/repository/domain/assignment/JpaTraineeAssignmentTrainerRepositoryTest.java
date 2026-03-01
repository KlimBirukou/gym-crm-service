package com.epam.gym.repository.domain.assignment;

import com.epam.gym.domain.user.Trainee;
import com.epam.gym.domain.user.Trainer;
import com.epam.gym.repository.entity.TraineeEntity;
import com.epam.gym.repository.entity.TraineeTrainerEntity;
import com.epam.gym.repository.entity.TrainerEntity;
import com.epam.gym.repository.jpa.assignment.IAssignmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.convert.ConversionService;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
public class JpaTraineeAssignmentTrainerRepositoryTest {

    private static final String TRAINEE_USERNAME = "trainee_username";
    private static final String TRAINER_USERNAME = "trainer_username";

    private static final Trainee TRAINEE = Trainee.builder().username(TRAINEE_USERNAME).build();
    private static final Trainer TRAINER = Trainer.builder().username(TRAINER_USERNAME).build();
    private static final Trainer TRAINER_2 = Trainer.builder().username("trainer.user2").build();

    private static final TraineeEntity TRAINEE_ENTITY = new TraineeEntity();
    private static final TrainerEntity TRAINER_ENTITY = new TrainerEntity();
    private static final TrainerEntity TRAINER_ENTITY_2 = new TrainerEntity();

    @Mock
    private IAssignmentRepository repository;
    @Mock
    private ConversionService conversionService;

    @Captor
    private ArgumentCaptor<TraineeTrainerEntity> trainerEntityArgumentCaptor;

    private JpaAssignmentRepository testObject;

    @BeforeEach
    void setUp() {
        testObject = new JpaAssignmentRepository(
            repository,
            conversionService
        );
    }

    @Test
    void assign_shouldSaveEntity_whenAlways() {
        doReturn(TRAINEE_ENTITY).when(conversionService).convert(TRAINEE, TraineeEntity.class);
        doReturn(TRAINER_ENTITY).when(conversionService).convert(TRAINER, TrainerEntity.class);

        testObject.assign(TRAINEE, TRAINER);

        verify(repository).save(trainerEntityArgumentCaptor.capture());
        var capturedEntity = trainerEntityArgumentCaptor.getValue();

        assertEquals(TRAINEE_ENTITY, capturedEntity.getTrainee());
        assertEquals(TRAINER_ENTITY, capturedEntity.getTrainer());

        assertNoUnexpectedInteractions();
    }

    private static Stream<Arguments> provideNullsForAssign() {
        return Stream.of(
            Arguments.of(null, TRAINER),
            Arguments.of(TRAINEE, null),
            Arguments.of(null, null)
        );
    }

    @ParameterizedTest
    @MethodSource("provideNullsForAssign")
    void assign_shouldThrowException_whenArgumentsNull(Trainee trainee, Trainer trainer) {
        assertThrows(NullPointerException.class, () -> testObject.assign(trainee, trainer));

        assertNoUnexpectedInteractions();
    }


    @Test
    void checkAssign_shouldReturnTrue_whenExists() {
        doReturn(true).when(repository).existsByTraineeUserUsernameAndTrainerUserUsername(TRAINEE_USERNAME, TRAINER_USERNAME);

        var result = testObject.checkAssign(TRAINEE_USERNAME, TRAINER_USERNAME);

        assertTrue(result);

        assertNoUnexpectedInteractions();
    }

    private static Stream<Arguments> provideNullsForCheckAssign() {
        return Stream.of(
            Arguments.of(null, TRAINER_USERNAME),
            Arguments.of(TRAINEE_USERNAME, null),
            Arguments.of(null, null)
        );
    }

    @ParameterizedTest
    @MethodSource("provideNullsForCheckAssign")
    void checkAssign_shouldThrowException_whenArgumentsNull(String traineeUsername, String trainerUsername) {
        assertThrows(NullPointerException.class, () -> testObject.checkAssign(traineeUsername, trainerUsername));

        assertNoUnexpectedInteractions();
    }

    private static Stream<Arguments> provideTestData() {
        return Stream.of(
            Arguments.of(List.of(), List.of()),
            Arguments.of(List.of(TRAINER_ENTITY), List.of(TRAINER)),
            Arguments.of(List.of(TRAINER_ENTITY, TRAINER_ENTITY_2), List.of(TRAINER, TRAINER_2))
        );
    }

    @ParameterizedTest
    @MethodSource("provideTestData")
    void getAssignedTrainers_shouldReturnTrainers(List<TrainerEntity> entities, List<Trainer> trainers) {
        doReturn(entities).when(repository).getAssignedTrainers(TRAINEE_USERNAME);
        IntStream.range(0, entities.size()).forEach(i ->
            doReturn(trainers.get(i)).when(conversionService).convert(entities.get(i), Trainer.class)
        );

        var result = testObject.getAssignedTrainers(TRAINEE_USERNAME);

        assertEquals(trainers.size(), result.size());
        assertEquals(trainers, result);

        assertNoUnexpectedInteractions();
    }

    @ParameterizedTest
    @NullSource
    void getAssignedTrainers_shouldThrowException_whenArgumentNull(String username) {
        assertThrows(NullPointerException.class, () -> testObject.getAssignedTrainers(username));

        assertNoUnexpectedInteractions();
    }


    @ParameterizedTest
    @MethodSource("provideTestData")
    void getUnassignedTrainers_shouldReturnTrainers(List<TrainerEntity> entities, List<Trainer> trainers) {
        doReturn(entities).when(repository).getUnassignedTrainers(TRAINEE_USERNAME);
        IntStream.range(0, entities.size()).forEach(i ->
            doReturn(trainers.get(i)).when(conversionService).convert(entities.get(i), Trainer.class)
        );

        var result = testObject.getUnassignedTrainers(TRAINEE_USERNAME);

        assertEquals(trainers.size(), result.size());
        assertEquals(trainers, result);

        assertNoUnexpectedInteractions();
    }

    @ParameterizedTest
    @NullSource
    void getUnassignedTrainers_shouldThrowException_whenArgumentNull(String username) {
        assertThrows(NullPointerException.class, () -> testObject.getUnassignedTrainers(username));

        assertNoUnexpectedInteractions();
    }


    private void assertNoUnexpectedInteractions() {
        verifyNoMoreInteractions(
            repository,
            conversionService
        );
    }
}
