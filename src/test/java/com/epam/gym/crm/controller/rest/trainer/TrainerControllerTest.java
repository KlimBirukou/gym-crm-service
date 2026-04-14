package com.epam.gym.crm.controller.rest.trainer;

import com.epam.gym.crm.controller.rest.trainer.dto.request.UpdateTrainerRequest;
import com.epam.gym.crm.controller.rest.trainer.dto.response.TrainerResponse;
import com.epam.gym.crm.facade.trainer.ITrainerFacade;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class TrainerControllerTest {

    private static final String USERNAME = "username";
    private static final String FIRST_NAME = "firstname";
    private static final String LAST_NAME = "last_name";
    private static final String SPECIALIZATION_NAME = "specialization_name";
    private static final Boolean ACTIVE = true;

    @Mock
    private ITrainerFacade trainerFacade;

    @InjectMocks
    private TrainerController testObject;

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(trainerFacade);
    }

    @Test
    void getTrainer() {
        var expected = buildTrainerResponse();
        doReturn(expected).when(trainerFacade).getProfile(USERNAME);

        var actual = testObject.getTrainer(USERNAME);

        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(trainerFacade).getProfile(USERNAME);
    }

    @Test
    void updateTrainer() {
        var request = buildUpdateTrainerRequest();
        var expected = buildTrainerResponse();
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

    private static TrainerResponse buildTrainerResponse() {
        return TrainerResponse.builder()
            .username(USERNAME)
            .firstName(FIRST_NAME)
            .lastName(LAST_NAME)
            .active(ACTIVE)
            .specialization(SPECIALIZATION_NAME)
            .trainees(List.of())
            .build();
    }

    private static UpdateTrainerRequest buildUpdateTrainerRequest() {
        return UpdateTrainerRequest.builder()
            .firstName(FIRST_NAME)
            .lastName(LAST_NAME)
            .specializationName(SPECIALIZATION_NAME)
            .build();
    }
}
