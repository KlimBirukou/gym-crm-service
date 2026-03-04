package com.epam.gym.controller.rest.training;

import com.epam.gym.controller.rest.training.dto.request.CreateTrainingRequest;
import com.epam.gym.controller.rest.training.dto.request.GetTraineeTrainingsRequest;
import com.epam.gym.controller.rest.training.dto.request.GetTrainerTrainingRequest;
import com.epam.gym.controller.rest.training.dto.response.TraineeTrainingResponse;
import com.epam.gym.controller.rest.training.dto.response.TrainerTrainingsResponse;
import com.epam.gym.controller.rest.training.dto.response.TrainingTypeResponse;
import com.epam.gym.facade.training.ITrainingFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TrainingControllerTest {

    private static final String USERNAME = "username";
    private static final String OTHER_USERNAME = "other_username";
    private static final String TYPE_NAME = "Yoga";
    private static final LocalDate FROM = LocalDate.now();
    private static final LocalDate TO = LocalDate.now().plusDays(1);

    @Mock
    private ITrainingFacade trainingFacade;

    private TrainingController testObject;

    @BeforeEach
    void setUp() {
        testObject = new TrainingController(trainingFacade);
    }

    @Test
    void createTraining() {
        var request = mock(CreateTrainingRequest.class);

        testObject.createTraining(request);

        verify(trainingFacade).create(request);
    }

    @Test
    void getTraineeTrainings() {
        var expected = List.of(mock(TraineeTrainingResponse.class));
        doReturn(expected).when(trainingFacade).getTraineeTrainings(any(GetTraineeTrainingsRequest.class));

        var actual = testObject.getTraineeTrainings(USERNAME, FROM, TO, OTHER_USERNAME, TYPE_NAME);

        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(trainingFacade).getTraineeTrainings(any(GetTraineeTrainingsRequest.class));
    }

    @Test
    void getTrainerTrainings() {
        var expected = List.of(mock(TrainerTrainingsResponse.class));
        doReturn(expected).when(trainingFacade).getTrainerTrainings(any(GetTrainerTrainingRequest.class));

        var actual = testObject.getTrainerTrainings(USERNAME, FROM, TO, OTHER_USERNAME);

        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(trainingFacade).getTrainerTrainings(any(GetTrainerTrainingRequest.class));
    }

    @Test
    void getTrainingTypes() {
        var expected = List.of(mock(TrainingTypeResponse.class));
        doReturn(expected).when(trainingFacade).getTrainingsTypes();

        var actual = testObject.getTrainingTypes();

        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(trainingFacade).getTrainingsTypes();
    }
}
