package com.epam.gym.controller.rest.trainee;

import com.epam.gym.controller.rest.trainee.dto.request.UpdateTraineeRequest;
import com.epam.gym.controller.rest.trainee.dto.response.TraineeResponse;
import com.epam.gym.facade.trainee.ITraineeFacade;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class TraineeControllerTest {

    private static final LocalDate DATE = LocalDate.of(2000, 1, 1);
    private static final String USERNAME = "username";
    private static final String FIRST_NAME = "firstname";
    private static final String LAST_NAME = "last_name";
    private static final String ADDRESS = "address";
    private static final Boolean ACTIVE = true;

    @Mock
    private ITraineeFacade traineeFacade;

    @InjectMocks
    private TraineeController testObject;

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(traineeFacade);
    }

    @Test
    void getTrainee() {
        var expected = buildTraineeResponse();
        doReturn(expected).when(traineeFacade).getProfile(USERNAME);

        var actual = testObject.getTrainee(USERNAME);

        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(traineeFacade).getProfile(USERNAME);
    }

    @Test
    void updateTrainee() {
        var request = buildUpdateTraineeRequest();
        var expected = buildTraineeResponse();
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

    private static TraineeResponse buildTraineeResponse() {
        return TraineeResponse.builder()
            .username(USERNAME)
            .firstName(FIRST_NAME)
            .lastName(LAST_NAME)
            .address(ADDRESS)
            .active(ACTIVE)
            .birthdate(DATE)
            .trainers(List.of())
            .build();
    }

    private static UpdateTraineeRequest buildUpdateTraineeRequest() {
        return UpdateTraineeRequest.builder()
            .firstName(FIRST_NAME)
            .lastName(LAST_NAME)
            .address(ADDRESS)
            .birthdate(DATE)
            .build();
    }
}
