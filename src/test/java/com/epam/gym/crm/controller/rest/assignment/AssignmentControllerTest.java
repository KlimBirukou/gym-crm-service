package com.epam.gym.crm.controller.rest.assignment;

import com.epam.gym.crm.controller.rest.assignment.dto.request.AssignRequest;
import com.epam.gym.crm.controller.rest.trainee.dto.response.TrainerProfileResponse;
import com.epam.gym.crm.controller.rest.trainer.dto.response.TraineeProfileResponse;
import com.epam.gym.crm.facade.assignment.IAssignmentFacade;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class AssignmentControllerTest {

    private static final String TRAINEE_USERNAME = "trainee_username";
    private static final String TRAINER_USERNAME = "trainer_username";
    private static final String FIRST_NAME = "firstname";
    private static final String LAST_NAME = "last_name";
    private static final String SPECIALIZATION_NAME = "specialization_name";
    private static final Boolean ASSIGNED = true;
    private static final Boolean ACTIVE = true;

    @Mock
    private IAssignmentFacade assignmentFacade;

    @InjectMocks
    private AssignmentController testObject;

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(assignmentFacade);
    }

    @Test
    void getTrainees() {
        var expected = List.of(buildTraineeProfileResponse());
        doReturn(expected).when(assignmentFacade).getTrainees(TRAINEE_USERNAME, ASSIGNED, ACTIVE);

        var actual = testObject.getTrainees(TRAINEE_USERNAME, ASSIGNED, ACTIVE);

        assertEquals(expected, actual);
        verify(assignmentFacade).getTrainees(TRAINEE_USERNAME, ASSIGNED, ACTIVE);
    }

    @Test
    void getTrainers() {
        var expected = List.of(buildTrainerProfileResponse());
        doReturn(expected).when(assignmentFacade).getTrainers(TRAINER_USERNAME, ASSIGNED, ACTIVE);

        var actual = testObject.getTrainers(TRAINER_USERNAME, ASSIGNED, ACTIVE);

        assertEquals(expected, actual);
        verify(assignmentFacade).getTrainers(TRAINER_USERNAME, ASSIGNED, ACTIVE);
    }

    @Test
    void assign() {
        var request = buildAssignRequest();

        testObject.assign(request);

        verify(assignmentFacade).assign(request);
    }

    private static AssignRequest buildAssignRequest() {
        return AssignRequest.builder()
            .traineeUsername(TRAINEE_USERNAME)
            .trainerUsername(TRAINER_USERNAME)
            .build();
    }

    private static TraineeProfileResponse buildTraineeProfileResponse() {
        return TraineeProfileResponse.builder()
            .username(TRAINEE_USERNAME)
            .firstName(FIRST_NAME)
            .lastName(LAST_NAME)
            .build();
    }

    private static TrainerProfileResponse buildTrainerProfileResponse() {
        return TrainerProfileResponse.builder()
            .username(TRAINER_USERNAME)
            .firstName(FIRST_NAME)
            .lastName(LAST_NAME)
            .specializationName(SPECIALIZATION_NAME)
            .build();
    }
}
