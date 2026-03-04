package com.epam.gym.controller.rest.trainee;

import com.epam.gym.controller.rest.trainee.dto.request.UpdateTraineeRequest;
import com.epam.gym.controller.rest.trainee.dto.response.TraineeResponse;
import com.epam.gym.facade.trainee.ITraineeFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TraineeControllerTest {

    private static final String USERNAME = "username";
    private static final Boolean ACTIVE = true;

    @Mock
    private ITraineeFacade traineeFacade;

    private TraineeController testObject;

    @BeforeEach
    void setUp() {
        testObject = new TraineeController(traineeFacade);
    }

    @Test
    void getTrainee() {
        var expected = mock(TraineeResponse.class);
        doReturn(expected).when(traineeFacade).getProfile(USERNAME);

        var actual = testObject.getTrainee(USERNAME);

        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(traineeFacade).getProfile(USERNAME);
    }

    @Test
    void updateTrainee() {
        var request = mock(UpdateTraineeRequest.class);
        var expected = mock(TraineeResponse.class);
        doReturn(expected).when(traineeFacade).updateTrainee(USERNAME, request);

        var actual = testObject.updateTrainee(USERNAME, request);

        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(traineeFacade).updateTrainee(USERNAME, request);
    }

    @Test
    void changeStatus() {
        testObject.changeStatus(USERNAME, ACTIVE);

        verify(traineeFacade).changeStatus(USERNAME, ACTIVE);
    }

    @Test
    void deleteTrainee() {
        testObject.deleteTrainee(USERNAME);

        verify(traineeFacade).delete(USERNAME);
    }
}
