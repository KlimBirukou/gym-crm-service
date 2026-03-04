package com.epam.gym.controller.rest.trainer;

import com.epam.gym.controller.rest.trainer.dto.request.UpdateTrainerRequest;
import com.epam.gym.controller.rest.trainer.dto.response.TrainerResponse;
import com.epam.gym.facade.trainer.ITrainerFacade;
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
class TrainerControllerTest {

    private static final String USERNAME = "username";
    private static final Boolean ACTIVE = true;

    @Mock
    private ITrainerFacade trainerFacade;

    private TrainerController testObject;

    @BeforeEach
    void setUp() {
        testObject = new TrainerController(trainerFacade);
    }

    @Test
    void getTrainer() {
        var expected = mock(TrainerResponse.class);
        doReturn(expected).when(trainerFacade).getProfile(USERNAME);

        var actual = testObject.getTrainer(USERNAME);

        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(trainerFacade).getProfile(USERNAME);
    }

    @Test
    void updateTrainer() {
        var request = mock(UpdateTrainerRequest.class);
        var expected = mock(TrainerResponse.class);
        doReturn(expected).when(trainerFacade).updateTrainer(USERNAME, request);

        var actual = testObject.updateTrainer(USERNAME, request);

        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(trainerFacade).updateTrainer(USERNAME, request);
    }

    @Test
    void changeStatus() {
        testObject.changeStatus(USERNAME, ACTIVE);

        verify(trainerFacade).changeStatus(USERNAME, ACTIVE);
    }
}
