package com.epam.gym.controller.rest.assignment;

import com.epam.gym.controller.rest.assignment.dto.request.AssignRequest;
import com.epam.gym.controller.rest.trainee.dto.response.TrainerProfileResponse;
import com.epam.gym.controller.rest.trainer.dto.response.TraineeProfileResponse;
import com.epam.gym.facade.assignment.IAssignmentFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AssignmentControllerTest {

    private static final String USERNAME = "username";
    private static final Boolean ASSIGNED = true;
    private static final Boolean ACTIVE = true;

    @Mock
    private IAssignmentFacade assignmentFacade;

    private AssignmentController testObject;

    @BeforeEach
    void setUp() {
        testObject = new AssignmentController(assignmentFacade);
    }

    @Test
    void getTrainees() {
        var expected = List.of(mock(TraineeProfileResponse.class));
        doReturn(expected).when(assignmentFacade).getTrainees(USERNAME, ASSIGNED, ACTIVE);

        var actual = testObject.getTrainees(USERNAME, ASSIGNED, ACTIVE);

        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(assignmentFacade).getTrainees(USERNAME, ASSIGNED, ACTIVE);
    }

    @Test
    void getTrainers() {
        var expected = List.of(mock(TrainerProfileResponse.class));
        doReturn(expected).when(assignmentFacade).getTrainers(USERNAME, ASSIGNED, ACTIVE);

        var actual = testObject.getTrainers(USERNAME, ASSIGNED, ACTIVE);

        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(assignmentFacade).getTrainers(USERNAME, ASSIGNED, ACTIVE);
    }

    @Test
    void assign() {
        var request = mock(AssignRequest.class);

        testObject.assign(request);

        verify(assignmentFacade).assign(request);
    }
}
