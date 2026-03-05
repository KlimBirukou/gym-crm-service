package com.epam.gym.controller.rest.training;

import com.epam.gym.controller.rest.training.dto.request.CreateTrainingRequest;
import com.epam.gym.controller.rest.training.dto.request.GetTraineeTrainingsRequest;
import com.epam.gym.controller.rest.training.dto.request.GetTrainerTrainingRequest;
import com.epam.gym.controller.rest.training.dto.response.TraineeTrainingResponse;
import com.epam.gym.controller.rest.training.dto.response.TrainerTrainingsResponse;
import com.epam.gym.controller.rest.training.dto.response.TrainingTypeResponse;
import com.epam.gym.facade.training.ITrainingFacade;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class TrainingControllerTest {

    private static final String TRAINEE_USERNAME = "trainee_username";
    private static final String TRAINER_USERNAME = "trainer_username";
    private static final String NAME = "name";
    private static final String TRAINING_TYPE_NAME = "specialization_name";
    private static final LocalDate DATE = LocalDate.now();
    private static final int DURATION = 60;

    @Mock
    private ITrainingFacade trainingFacade;

    @InjectMocks
    private TrainingController testObject;

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(trainingFacade);
    }

    @Test
    void createTraining() {
        var request = buildCreateTrainingRequest();

        testObject.createTraining(request);

        verify(trainingFacade).create(request);
    }

    @Test
    void getTraineeTrainings() {
        var expected = List.of(buildTraineeTrainingResponse());
        doReturn(expected).when(trainingFacade).getTraineeTrainings(any(GetTraineeTrainingsRequest.class));

        var actual = testObject.getTraineeTrainings(TRAINEE_USERNAME, DATE, DATE, TRAINER_USERNAME, TRAINING_TYPE_NAME);

        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(trainingFacade).getTraineeTrainings(any(GetTraineeTrainingsRequest.class));
    }

    @Test
    void getTrainerTrainings() {
        var expected = List.of(buildTrainerTrainingsResponse());
        doReturn(expected).when(trainingFacade).getTrainerTrainings(any(GetTrainerTrainingRequest.class));

        var actual = testObject.getTrainerTrainings(TRAINER_USERNAME, DATE, DATE, TRAINEE_USERNAME);

        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(trainingFacade).getTrainerTrainings(any(GetTrainerTrainingRequest.class));
    }

    @Test
    void getTrainingTypes() {
        var expected = List.of(buildTrainingTypeResponse());
        doReturn(expected).when(trainingFacade).getTrainingsTypes();

        var actual = testObject.getTrainingTypes();

        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(trainingFacade).getTrainingsTypes();
    }

    private static CreateTrainingRequest buildCreateTrainingRequest() {
        return CreateTrainingRequest.builder()
            .traineeUsername(TRAINEE_USERNAME)
            .trainerUsername(TRAINER_USERNAME)
            .name(NAME)
            .date(DATE)
            .durationInMinutes(DURATION)
            .build();
    }

    private static TraineeTrainingResponse buildTraineeTrainingResponse() {
        return TraineeTrainingResponse.builder()
            .trainerUsername(TRAINER_USERNAME)
            .name(NAME)
            .trainingTypeName(TRAINING_TYPE_NAME)
            .date(DATE)
            .duration(DURATION)
            .build();
    }

    private static TrainerTrainingsResponse buildTrainerTrainingsResponse() {
        return TrainerTrainingsResponse.builder()
            .traineeUsername(TRAINEE_USERNAME)
            .name(NAME)
            .trainingTypeName(TRAINING_TYPE_NAME)
            .date(DATE)
            .duration(DURATION)
            .build();
    }

    private static TrainingTypeResponse buildTrainingTypeResponse() {
        return TrainingTypeResponse.builder()
            .uid(UUID.randomUUID())
            .name(NAME)
            .build();
    }
}
