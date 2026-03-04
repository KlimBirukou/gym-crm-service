package com.epam.gym.service.assignment;

import com.epam.gym.domain.user.Trainee;
import com.epam.gym.domain.user.Trainer;
import com.epam.gym.exception.conflict.assignment.AlreadyAssignedException;
import com.epam.gym.exception.not.active.TraineeNotActiveException;
import com.epam.gym.exception.not.active.TrainerNotActiveException;
import com.epam.gym.exception.not.found.TraineeNotFoundException;
import com.epam.gym.exception.not.found.TrainerNotFoundException;
import com.epam.gym.repository.domain.assignment.IAssignmentRepository;
import com.epam.gym.service.trainee.ITraineeService;
import com.epam.gym.service.trainer.ITrainerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class AssignmentServiceTest {

    private static final String TRAINEE_USERNAME = "trainee_username";
    private static final String TRAINER_USERNAME = "trainer_username";
    private static final Trainee TRAINEE_1 = new Trainee();
    private static final Trainee TRAINEE_2 = new Trainee();
    private static final Trainer TRAINER_1 = new Trainer();
    private static final Trainer TRAINER_2 = new Trainer();

    @Mock
    private ITraineeService traineeService;
    @Mock
    private ITrainerService trainerService;
    @Mock
    private IAssignmentRepository assignmentRepository;

    private AssignmentService testObject;

    @BeforeEach
    void setUp() {
        testObject = new AssignmentService(
            traineeService,
            trainerService,
            assignmentRepository
        );
    }

    @Test
    void assign_shouldAssign_whenTraineeAndTrainerExistAndActive() {
        var trainee = Trainee.builder()
            .username(TRAINEE_USERNAME)
            .active(true)
            .build();
        var trainer = Trainer.builder()
            .username(TRAINER_USERNAME)
            .active(true)
            .build();
        doReturn(trainee).when(traineeService).getByUsername(TRAINEE_USERNAME);
        doReturn(trainer).when(trainerService).getByUsername(TRAINER_USERNAME);
        doReturn(false).when(assignmentRepository).checkAssign(TRAINEE_USERNAME, TRAINER_USERNAME);

        testObject.assign(TRAINEE_USERNAME, TRAINER_USERNAME);

        verify(assignmentRepository).assign(trainee, trainer);

        assertNoUnexpectedInteractions();
    }

    @Test
    void assign_shouldThrowException_whenTraineeNotActive() {
        var unactiveTrainee = Trainee.builder()
            .username(TRAINEE_USERNAME)
            .active(false)
            .build();
        doReturn(unactiveTrainee).when(traineeService).getByUsername(TRAINEE_USERNAME);

        assertThrows(TraineeNotActiveException.class, () -> testObject.assign(TRAINEE_USERNAME, TRAINER_USERNAME));

        assertNoUnexpectedInteractions();
    }

    @Test
    void assign_shouldThrowException_whenTrainerNotActive() {
        var trainee = Trainee.builder()
            .username(TRAINEE_USERNAME)
            .active(true)
            .build();
        var unactiveTrainer = Trainer.builder()
            .username(TRAINER_USERNAME)
            .active(false)
            .build();
        doReturn(trainee).when(traineeService).getByUsername(TRAINEE_USERNAME);
        doReturn(unactiveTrainer).when(trainerService).getByUsername(TRAINER_USERNAME);

        assertThrows(TrainerNotActiveException.class, () -> testObject.assign(TRAINEE_USERNAME, TRAINER_USERNAME));

        assertNoUnexpectedInteractions();
    }

    @Test
    void assign_shouldThrowException_whenTraineeAlreadyAssignedToTrainer() {
        var trainee = Trainee.builder()
            .username(TRAINEE_USERNAME)
            .active(true)
            .build();
        var trainer = Trainer.builder()
            .username(TRAINER_USERNAME)
            .active(true)
            .build();
        doReturn(trainee).when(traineeService).getByUsername(TRAINEE_USERNAME);
        doReturn(trainer).when(trainerService).getByUsername(TRAINER_USERNAME);
        doReturn(true).when(assignmentRepository).checkAssign(TRAINEE_USERNAME, TRAINER_USERNAME);

        assertThrows(AlreadyAssignedException.class, () -> testObject.assign(TRAINEE_USERNAME, TRAINER_USERNAME));

        assertNoUnexpectedInteractions();
    }

    private static Stream<Arguments> assignProvideNullArguments() {
        return Stream.of(
            Arguments.of(null, TRAINER_USERNAME),
            Arguments.of(TRAINEE_USERNAME, null),
            Arguments.of(null, null)
        );
    }

    @ParameterizedTest
    @MethodSource("assignProvideNullArguments")
    void assign_shouldThrowException_whenArgumentsNull(String traineeUsername, String trainerUsername) {
        assertThrows(NullPointerException.class, () -> testObject.assign(traineeUsername, trainerUsername));

        assertNoUnexpectedInteractions();
    }


    private static Stream<Boolean> provideExistArgument() {
        return Stream.of(true, false);
    }

    @ParameterizedTest
    @MethodSource("provideExistArgument")
    void checkAssignExist_shouldReturnBoolean(boolean value) {
        doReturn(value).when(assignmentRepository).checkAssign(TRAINEE_USERNAME, TRAINER_USERNAME);

        var result = testObject.checkAssignExist(TRAINEE_USERNAME, TRAINER_USERNAME);

        assertEquals(value, result);

        assertNoUnexpectedInteractions();
    }

    private static Stream<Arguments> provideNullArguments() {
        return Stream.of(
            Arguments.of(null, TRAINER_USERNAME),
            Arguments.of(TRAINEE_USERNAME, null),
            Arguments.of(null, null)
        );
    }

    @ParameterizedTest
    @MethodSource("provideNullArguments")
    void checkAssignExist_shouldThrowException_whenArgumentsNull(String traineeUsername, String trainerUsername) {
        assertThrows(NullPointerException.class, () -> testObject.checkAssignExist(traineeUsername, trainerUsername));

        assertNoUnexpectedInteractions();
    }


    private static Stream<Arguments> provideTraineesData() {
        return Stream.of(
            Arguments.of(List.of(), true, true),
            Arguments.of(List.of(), true, false),
            Arguments.of(List.of(), false, true),
            Arguments.of(List.of(), false, false),
            Arguments.of(List.of(TRAINEE_1, TRAINEE_2), true, true),
            Arguments.of(List.of(TRAINEE_1, TRAINEE_2), true, false),
            Arguments.of(List.of(TRAINEE_1, TRAINEE_2), false, true),
            Arguments.of(List.of(TRAINEE_1, TRAINEE_2), false, false)
        );
    }

    @ParameterizedTest
    @MethodSource("provideTraineesData")
    void getTrainees_shouldReturnList_whenTrainerExist(List<Trainee> list, Boolean assigned, Boolean active) {
        var trainer = Trainer.builder()
            .username(TRAINER_USERNAME)
            .build();
        doReturn(trainer).when(trainerService).getByUsername(TRAINER_USERNAME);
        doReturn(list).when(assignmentRepository).getTrainees(TRAINER_USERNAME, assigned, active);

        var result = testObject.getTrainees(TRAINER_USERNAME, assigned, active);

        assertEquals(list, result);

        assertNoUnexpectedInteractions();
    }

    @Test
    void getTrainees_shouldThrowException_whenTrainerNotExist() {
        doThrow(new TrainerNotFoundException(TRAINER_USERNAME)).when(trainerService).getByUsername(TRAINER_USERNAME);

        assertThrows(TrainerNotFoundException.class,
            () -> testObject.getTrainees(TRAINER_USERNAME, true, true));

        assertNoUnexpectedInteractions();
    }

    private static Stream<Arguments> getTraineesProvideNullArguments() {
        return Stream.of(
            Arguments.of(null, true, true),
            Arguments.of(TRAINER_USERNAME, null, true),
            Arguments.of(TRAINER_USERNAME, true, null)
        );
    }

    @ParameterizedTest
    @MethodSource("getTraineesProvideNullArguments")
    void getTrainees_shouldThrowNPE_whenAnyArgumentIsNull(String trainerUsername, Boolean assigned, Boolean active) {
        assertThrows(NullPointerException.class, () ->
            testObject.getTrainees(trainerUsername, assigned, active)
        );

        assertNoUnexpectedInteractions();
    }


    private static Stream<Arguments> provideTrainersData() {
        return Stream.of(
            Arguments.of(List.of(), true, true),
            Arguments.of(List.of(), true, false),
            Arguments.of(List.of(), false, true),
            Arguments.of(List.of(), false, false),
            Arguments.of(List.of(TRAINER_1, TRAINER_2), true, true),
            Arguments.of(List.of(TRAINER_1, TRAINER_2), true, false),
            Arguments.of(List.of(TRAINER_1, TRAINER_2), false, true),
            Arguments.of(List.of(TRAINER_1, TRAINER_2), false, false)
        );
    }

    @ParameterizedTest
    @MethodSource("provideTrainersData")
    void getTrainers_shouldReturnList_whenTraineeExist(List<Trainer> list, Boolean assigned, Boolean active) {
        var trainee = Trainee.builder()
            .username(TRAINEE_USERNAME)
            .build();
        doReturn(trainee).when(traineeService).getByUsername(TRAINEE_USERNAME);
        doReturn(list).when(assignmentRepository).getTrainers(TRAINEE_USERNAME, assigned, active);

        var result = testObject.getTrainers(TRAINEE_USERNAME, assigned, active);

        assertEquals(list, result);

        assertNoUnexpectedInteractions();
    }

    @Test
    void getTrainers_shouldThrowException_whenTraineeNotExist() {
        doThrow(new TraineeNotFoundException(TRAINEE_USERNAME)).when(traineeService).getByUsername(TRAINEE_USERNAME);

        assertThrows(TraineeNotFoundException.class,
            () -> testObject.getTrainers(TRAINEE_USERNAME, true, true));

        assertNoUnexpectedInteractions();
    }

    private static Stream<Arguments> getTrainersProvideNullArguments() {
        return Stream.of(
            Arguments.of(null, true, true),
            Arguments.of(TRAINEE_USERNAME, null, true),
            Arguments.of(TRAINEE_USERNAME, true, null)
        );
    }

    @ParameterizedTest
    @MethodSource("getTrainersProvideNullArguments")
    void getTrainers_shouldThrowNPE_whenAnyArgumentIsNull(String traineeUsername, Boolean assigned, Boolean active) {
        assertThrows(NullPointerException.class, () ->
            testObject.getTrainers(traineeUsername, assigned, active)
        );

        assertNoUnexpectedInteractions();
    }


    void assertNoUnexpectedInteractions() {
        verifyNoMoreInteractions(
            traineeService,
            trainerService,
            assignmentRepository
        );
    }
}