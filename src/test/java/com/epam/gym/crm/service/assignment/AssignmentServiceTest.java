package com.epam.gym.crm.service.assignment;

import com.epam.gym.crm.domain.user.Trainee;
import com.epam.gym.crm.domain.user.Trainer;
import com.epam.gym.crm.exception.conflict.assignment.AlreadyAssignedException;
import com.epam.gym.crm.exception.not.active.TraineeNotActiveException;
import com.epam.gym.crm.exception.not.active.TrainerNotActiveException;
import com.epam.gym.crm.exception.not.found.TraineeNotFoundException;
import com.epam.gym.crm.exception.not.found.TrainerNotFoundException;
import com.epam.gym.crm.repository.domain.assignment.IAssignmentRepository;
import com.epam.gym.crm.service.trainee.ITraineeService;
import com.epam.gym.crm.service.trainer.ITrainerService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
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

    @Mock
    private ITraineeService traineeService;
    @Mock
    private ITrainerService trainerService;
    @Mock
    private IAssignmentRepository assignmentRepository;

    @InjectMocks
    private AssignmentService testObject;

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(traineeService, trainerService, assignmentRepository);
    }

    @Test
    void assign_shouldAssign_whenTraineeAndTrainerExistAndActive() {
        var trainee = buildTrainee(true);
        var trainer = buildTrainer(true);
        doReturn(trainee).when(traineeService).getByUsername(TRAINEE_USERNAME);
        doReturn(trainer).when(trainerService).getByUsername(TRAINER_USERNAME);
        doReturn(false).when(assignmentRepository).checkAssign(TRAINEE_USERNAME, TRAINER_USERNAME);

        testObject.assign(TRAINEE_USERNAME, TRAINER_USERNAME);

        verify(assignmentRepository).assign(trainee, trainer);
    }

    @Test
    void assign_shouldThrowException_whenTraineeNotActive() {
        var trainee = buildTrainee(false);
        doReturn(trainee).when(traineeService).getByUsername(TRAINEE_USERNAME);

        assertThrows(TraineeNotActiveException.class, () -> testObject.assign(TRAINEE_USERNAME, TRAINER_USERNAME));
    }

    @Test
    void assign_shouldThrowException_whenTrainerNotActive() {
        var trainee = buildTrainee(true);
        var trainer = buildTrainer(false);
        doReturn(trainee).when(traineeService).getByUsername(TRAINEE_USERNAME);
        doReturn(trainer).when(trainerService).getByUsername(TRAINER_USERNAME);

        assertThrows(TrainerNotActiveException.class, () -> testObject.assign(TRAINEE_USERNAME, TRAINER_USERNAME));
    }

    @Test
    void assign_shouldThrowException_whenTraineeAlreadyAssignedToTrainer() {
        var trainee = buildTrainee(true);
        var trainer = buildTrainer(true);
        doReturn(trainee).when(traineeService).getByUsername(TRAINEE_USERNAME);
        doReturn(trainer).when(trainerService).getByUsername(TRAINER_USERNAME);
        doReturn(true).when(assignmentRepository).checkAssign(TRAINEE_USERNAME, TRAINER_USERNAME);

        assertThrows(AlreadyAssignedException.class, () -> testObject.assign(TRAINEE_USERNAME, TRAINER_USERNAME));
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
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void checkAssignmentExist_shouldReturnBoolean(boolean value) {
        doReturn(value).when(assignmentRepository).checkAssign(TRAINEE_USERNAME, TRAINER_USERNAME);

        var result = testObject.checkAssignmentExist(TRAINEE_USERNAME, TRAINER_USERNAME);

        assertEquals(value, result);
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
    void checkAssignmentExist_shouldThrowException_whenArgumentsNull(String traineeUsername, String trainerUsername) {
        assertThrows(NullPointerException.class,
            () -> testObject.checkAssignmentExist(traineeUsername, trainerUsername));
    }

    private static Stream<Arguments> provideTraineesData() {
        return Stream.of(
            Arguments.of(List.of(), true, true),
            Arguments.of(List.of(), true, false),
            Arguments.of(List.of(), false, true),
            Arguments.of(List.of(), false, false),
            Arguments.of(List.of(buildTrainee(true), buildTrainee(true)), true, true),
            Arguments.of(List.of(buildTrainee(true), buildTrainee(true)), true, false),
            Arguments.of(List.of(buildTrainee(true), buildTrainee(true)), false, true),
            Arguments.of(List.of(buildTrainee(true), buildTrainee(true)), false, false)
        );
    }

    @ParameterizedTest
    @MethodSource("provideTraineesData")
    void getTrainees_shouldReturnList_whenTrainerExist(List<Trainee> list, Boolean assigned, Boolean active) {
        var trainer = buildTrainer(true);
        doReturn(trainer).when(trainerService).getByUsername(TRAINER_USERNAME);
        doReturn(list).when(assignmentRepository).getTrainees(TRAINER_USERNAME, assigned, active);

        var result = testObject.getTrainees(TRAINER_USERNAME, assigned, active);

        assertEquals(list, result);
    }

    @Test
    void getTrainees_shouldThrowException_whenTrainerNotExist() {
        doThrow(new TrainerNotFoundException(TRAINER_USERNAME)).when(trainerService).getByUsername(TRAINER_USERNAME);

        assertThrows(TrainerNotFoundException.class,
            () -> testObject.getTrainees(TRAINER_USERNAME, true, true));
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
        assertThrows(NullPointerException.class, () -> testObject.getTrainees(trainerUsername, assigned, active));
    }

    private static Stream<Arguments> provideTrainersData() {
        return Stream.of(
            Arguments.of(List.of(), true, true),
            Arguments.of(List.of(), true, false),
            Arguments.of(List.of(), false, true),
            Arguments.of(List.of(), false, false),
            Arguments.of(List.of(buildTrainer(true), buildTrainer(true)), true, true),
            Arguments.of(List.of(buildTrainer(true), buildTrainer(true)), true, false),
            Arguments.of(List.of(buildTrainer(true), buildTrainer(true)), false, true),
            Arguments.of(List.of(buildTrainer(true), buildTrainer(true)), false, false)
        );
    }

    @ParameterizedTest
    @MethodSource("provideTrainersData")
    void getTrainers_shouldReturnList_whenTraineeExist(List<Trainer> list, Boolean assigned, Boolean active) {
        var trainee = buildTrainee(true);
        doReturn(trainee).when(traineeService).getByUsername(TRAINEE_USERNAME);
        doReturn(list).when(assignmentRepository).getTrainers(TRAINEE_USERNAME, assigned, active);

        var result = testObject.getTrainers(TRAINEE_USERNAME, assigned, active);

        assertEquals(list, result);
    }

    @Test
    void getTrainers_shouldThrowException_whenTraineeNotExist() {
        doThrow(new TraineeNotFoundException(TRAINEE_USERNAME)).when(traineeService).getByUsername(TRAINEE_USERNAME);

        assertThrows(TraineeNotFoundException.class,
            () -> testObject.getTrainers(TRAINEE_USERNAME, true, true));
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
        assertThrows(NullPointerException.class, () -> testObject.getTrainers(traineeUsername, assigned, active));
    }

    private static Trainee buildTrainee(boolean active) {
        return Trainee.builder()
            .username(TRAINEE_USERNAME)
            .active(active)
            .build();
    }

    private static Trainer buildTrainer(boolean active) {
        return Trainer.builder()
            .username(TRAINER_USERNAME)
            .active(active)
            .build();
    }
}
