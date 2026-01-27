package com.epam.gym.facade;

import com.epam.gym.domain.training.Training;
import com.epam.gym.domain.training.TrainingType;
import com.epam.gym.domain.user.Trainee;
import com.epam.gym.domain.user.Trainer;
import com.epam.gym.service.trainee.ITraineeService;
import com.epam.gym.service.trainee.dto.CreateTraineeDto;
import com.epam.gym.service.trainee.dto.UpdateTraineeDto;
import com.epam.gym.service.trainer.ITrainerService;
import com.epam.gym.service.trainer.dto.CreateTrainerDto;
import com.epam.gym.service.trainer.dto.UpdateTrainerDto;
import com.epam.gym.service.training.ITrainingService;
import com.epam.gym.service.training.dto.CreateTrainingDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GymFacadeTest {

    private static final UUID TRAINER_UID = UUID.randomUUID();
    private static final UUID TRAINEE_UID = UUID.randomUUID();
    private static final UUID TRAINING_UID = UUID.randomUUID();
    private static final String FIRSTNAME = "John";
    private static final String LASTNAME = "Doe";
    private static final String USERNAME = "John.Doe";
    private static final String SPECIALIZATION = "Yoga";
    private static final String ADDRESS = "Main Street 21";
    private static final String TRAINING_NAME = "Training";
    private static final LocalDate DATE_OF_BIRTH = LocalDate.of(1990, 1, 1);
    private static final LocalDate TRAINING_DATE = LocalDate.of(2023, 1, 1);
    private static final Duration DURATION = Duration.ofHours(1);

    private static final CreateTrainerDto CREATE_TRAINER_DTO = new CreateTrainerDto(
        FIRSTNAME, LASTNAME, SPECIALIZATION
    );
    private static final UpdateTrainerDto UPDATE_TRAINER_DTO = new UpdateTrainerDto(
        TRAINEE_UID, SPECIALIZATION
    );
    private static final CreateTraineeDto CREATE_TRAINEE_DTO = new CreateTraineeDto(
        FIRSTNAME, LASTNAME, ADDRESS
    );
    private static final UpdateTraineeDto UPDATE_TRAINEE_DTO = new UpdateTraineeDto(
        TRAINEE_UID, ADDRESS
    );
    private static final CreateTrainingDto CREATE_TRAINING_DTO = new CreateTrainingDto(
        TRAINER_UID, TRAINEE_UID, TRAINING_NAME, TrainingType.CARDIO, TRAINING_DATE, DURATION
    );

    private static final Trainer TRAINER = Trainer.builder()
        .uid(TRAINER_UID)
        .firstName(FIRSTNAME)
        .lastName(LASTNAME)
        .username(USERNAME)
        .build();
    private static final Trainee TRAINEE = Trainee.builder()
        .uid(TRAINEE_UID)
        .firstName(FIRSTNAME)
        .lastName(LASTNAME)
        .address(ADDRESS)
        .build();
    private static final Training TRAINING = Training.builder()
        .trainingUid(TRAINING_UID)
        .trainingName(TRAINING_NAME)
        .build();

    @Mock
    private ITrainerService trainerService;
    @Mock
    private ITraineeService traineeService;
    @Mock
    private ITrainingService trainingService;

    @InjectMocks
    private GymFacade testObject;

    @Test
    void createTrainer_shouldCallServiceAndReturnTrainee() {
        when(trainerService.create(CREATE_TRAINER_DTO))
            .thenReturn(TRAINER);

        var result = testObject.createTrainer(CREATE_TRAINER_DTO);

        assertNotNull(result);
        assertEquals(TRAINER, result);
        verify(trainerService, times(1)).create(CREATE_TRAINER_DTO);
    }

    @Test
    void createTrainer_shouldThrowNpe_whenArgumentIsNull() {
        assertThrows(NullPointerException.class,
            () -> testObject.createTrainer(null));

        verify(trainerService, never()).create(any());
    }

    @Test
    void updateTrainer_shouldCallService() {
        testObject.updateTrainer(UPDATE_TRAINER_DTO);

        verify(trainerService, times(1)).update(UPDATE_TRAINER_DTO);
    }

    @Test
    void updateTrainer_shouldThrowNpe_whenArgumentIsNull() {
        assertThrows(NullPointerException.class,
            () -> testObject.updateTrainer(null));

        verify(trainerService, never()).update(any());
    }

    @Test
    void createTrainee_shouldCallServiceAndReturnTrainee() {
        when(traineeService.create(CREATE_TRAINEE_DTO)).thenReturn(TRAINEE);

        var result = testObject.createTrainee(CREATE_TRAINEE_DTO);

        assertNotNull(result);
        assertEquals(TRAINEE, result);
        verify(traineeService, times(1)).create(CREATE_TRAINEE_DTO);
    }

    @Test
    void createTrainee_shouldThrowNpe_whenArgumentIsNull() {
        assertThrows(NullPointerException.class,
            () -> testObject.createTrainee(null));

        verify(traineeService, never()).create(any());
    }

    @Test
    void updateTrainee_shouldCallService() {
        testObject.updateTrainee(UPDATE_TRAINEE_DTO);

        verify(traineeService, times(1)).update(UPDATE_TRAINEE_DTO);
    }

    @Test
    void updateTrainee_shouldThrowNpe_whenArgumentIsNull() {
        assertThrows(NullPointerException.class,
            () -> testObject.updateTrainee(null));

        verify(traineeService, never()).update(any());
    }

    @Test
    void deleteTrainee_shouldCallService() {
        testObject.deleteTrainee(TRAINEE_UID);

        verify(traineeService, times(1)).delete(TRAINEE_UID);
    }

    @Test
    void deleteTrainee_shouldThrowNpe_whenArgumentIsNull() {
        assertThrows(NullPointerException.class,
            () -> testObject.deleteTrainee(null));

        verify(traineeService, never()).delete(any());
    }

    @Test
    void createTraining_shouldCallServiceAndReturnTraining() {
        when(trainingService.create(CREATE_TRAINING_DTO)).thenReturn(TRAINING);

        var result = testObject.createTraining(CREATE_TRAINING_DTO);

        assertNotNull(result);
        assertEquals(TRAINING, result);
        verify(trainingService, times(1)).create(CREATE_TRAINING_DTO);
    }

    @Test
    void createTraining_shouldThrowNpe_whenArgumentIsNull() {
        assertThrows(NullPointerException.class,
            () -> testObject.createTraining(null));

        verify(trainingService, never()).create(any());
    }
}
