package com.epam.gym.crm.facade.assignment;

import com.epam.gym.crm.controller.rest.assignment.dto.request.AssignRequest;
import com.epam.gym.crm.controller.rest.trainee.dto.response.TrainerProfileResponse;
import com.epam.gym.crm.controller.rest.trainer.dto.response.TraineeProfileResponse;
import com.epam.gym.crm.domain.user.Trainee;
import com.epam.gym.crm.domain.user.Trainer;
import com.epam.gym.crm.service.assignment.IAssignmentService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.convert.ConversionService;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class AssignmentFacadeTest {

    private static final String TRAINEE_USERNAME = "trainee_username";
    private static final String TRAINER_USERNAME = "trainer_username";
    private static final String FIRSTNAME = "firstname";
    private static final String LASTNAME = "lastname";
    private static final String SPECIALIZATION_NAME = "specialization";
    private static final Trainee TRAINEE_1 = new Trainee();
    private static final Trainee TRAINEE_2 = new Trainee();
    private static final Trainer TRAINER_1 = new Trainer();
    private static final Trainer TRAINER_2 = new Trainer();

    @Mock
    private IAssignmentService assignmentService;
    @Mock
    private ConversionService conversionService;

    @InjectMocks
    private AssignmentFacade testObject;

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(assignmentService, conversionService);
    }

    @Test
    void getTrainees_shouldReturnEmptyList_whenNoTraineesFound() {
        doReturn(List.of()).when(assignmentService).getTrainees(TRAINER_USERNAME, true, true);

        var result = testObject.getTrainees(TRAINER_USERNAME, true, true);

        assertTrue(result.isEmpty());
        verify(assignmentService).getTrainees(TRAINER_USERNAME, true, true);
        verifyNoInteractions(conversionService);
    }

    private static Stream<Arguments> provideTraineesData() {
        return Stream.of(
            Arguments.of(List.of(TRAINEE_1), true, false),
            Arguments.of(List.of(TRAINEE_1, TRAINEE_2), false, true)
        );
    }

    @ParameterizedTest
    @MethodSource("provideTraineesData")
    void getTrainees_shouldReturnMappedList_whenAlways(List<Trainee> trainees, Boolean assigned, Boolean active) {
        doReturn(trainees).when(assignmentService).getTrainees(TRAINER_USERNAME, assigned, active);
        doReturn(new TraineeProfileResponse(TRAINEE_USERNAME, FIRSTNAME, LASTNAME))
            .when(conversionService).convert(any(Trainee.class), eq(TraineeProfileResponse.class));

        var result = testObject.getTrainees(TRAINER_USERNAME, assigned, active);

        assertEquals(trainees.size(), result.size());
        verify(assignmentService).getTrainees(TRAINER_USERNAME, assigned, active);
        verify(conversionService, times(trainees.size())).convert(any(Trainee.class), eq(TraineeProfileResponse.class));
    }

    private static Stream<Arguments> provideGetTraineesNullArguments() {
        return Stream.of(
            Arguments.of(null, true, true),
            Arguments.of(TRAINER_USERNAME, null, true),
            Arguments.of(TRAINER_USERNAME, true, null)
        );
    }

    @ParameterizedTest
    @MethodSource("provideGetTraineesNullArguments")
    void getTrainees_shouldThrowException_whenAnyArgumentNull(String username, Boolean assigned, Boolean active) {
        assertThrows(NullPointerException.class, () -> testObject.getTrainees(username, assigned, active));
    }

    @Test
    void getTrainers_shouldReturnEmptyList_whenNoTrainersFound() {
        doReturn(List.of()).when(assignmentService).getTrainers(TRAINEE_USERNAME, true, true);

        var result = testObject.getTrainers(TRAINEE_USERNAME, true, true);

        assertTrue(result.isEmpty());
        verify(assignmentService).getTrainers(TRAINEE_USERNAME, true, true);
        verifyNoInteractions(conversionService);
    }

    private static Stream<Arguments> provideTrainersData() {
        return Stream.of(
            Arguments.of(List.of(TRAINER_1), true, false),
            Arguments.of(List.of(TRAINER_1, TRAINER_2), false, true)
        );
    }

    @ParameterizedTest
    @MethodSource("provideTrainersData")
    void getTrainers_shouldReturnMappedList_whenAlways(List<Trainer> trainers, Boolean assigned, Boolean active) {
        doReturn(trainers).when(assignmentService).getTrainers(TRAINEE_USERNAME, assigned, active);
        doReturn(new TrainerProfileResponse(TRAINER_USERNAME, FIRSTNAME, LASTNAME, SPECIALIZATION_NAME))
            .when(conversionService).convert(any(Trainer.class), eq(TrainerProfileResponse.class));

        var result = testObject.getTrainers(TRAINEE_USERNAME, assigned, active);

        assertEquals(trainers.size(), result.size());
        verify(assignmentService).getTrainers(TRAINEE_USERNAME, assigned, active);
        verify(conversionService, times(trainers.size())).convert(any(Trainer.class), eq(TrainerProfileResponse.class));
    }

    private static Stream<Arguments> provideGetTrainersNullArguments() {
        return Stream.of(
            Arguments.of(null, true, true),
            Arguments.of(TRAINEE_USERNAME, null, true),
            Arguments.of(TRAINEE_USERNAME, true, null)
        );
    }

    @ParameterizedTest
    @MethodSource("provideGetTrainersNullArguments")
    void getTrainers_shouldThrowException_whenAnyArgumentNull(String username, Boolean assigned, Boolean active) {
        assertThrows(NullPointerException.class, () -> testObject.getTrainers(username, assigned, active));
    }

    @Test
    void assign_shouldCallAssignmentService_whenAlways() {
        var request = buildAssignRequest();

        testObject.assign(request);

        verify(assignmentService).assign(TRAINEE_USERNAME, TRAINER_USERNAME);
    }

    @ParameterizedTest
    @NullSource
    void assign_shouldThrowException_whenRequestNull(AssignRequest request) {
        assertThrows(NullPointerException.class, () -> testObject.assign(request));
    }

    private static AssignRequest buildAssignRequest() {
        return AssignRequest.builder()
            .traineeUsername(TRAINEE_USERNAME)
            .trainerUsername(TRAINER_USERNAME)
            .build();
    }
}
