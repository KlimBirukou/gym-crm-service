package com.epam.gym.facade;

import com.epam.gym.domain.training.Training;
import com.epam.gym.domain.user.Trainee;
import com.epam.gym.domain.user.Trainer;
import com.epam.gym.mother.TraineeMother;
import com.epam.gym.mother.TrainerMother;
import com.epam.gym.mother.TrainingMother;
import com.epam.gym.mother.dto.trainee.CreateTraineeDtoMother;
import com.epam.gym.mother.dto.trainee.UpdateTraineeDtoMother;
import com.epam.gym.mother.dto.trainer.CreateTrainerDtoMother;
import com.epam.gym.mother.dto.trainer.UpdateTrainerDtoMother;
import com.epam.gym.mother.dto.training.CreateTrainingDtoMother;

import com.epam.gym.service.trainee.dto.CreateTraineeDto;
import com.epam.gym.service.trainee.dto.UpdateTraineeDto;
import com.epam.gym.service.trainer.dto.CreateTrainerDto;
import com.epam.gym.service.trainer.dto.UpdateTrainerDto;
import com.epam.gym.service.training.dto.CreateTrainingDto;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

@Disabled
@ExtendWith(MockitoExtension.class)
class GymFacadeTest {

    private static final UUID TRAINER_UID = UUID.randomUUID();
    private static final UUID TRAINEE_UID = UUID.randomUUID();
    private static final UUID TRAINING_UID = UUID.randomUUID();
    private static final String FIRSTNAME = "firstname";
    private static final String LASTNAME = "lastname";
    private static final String USERNAME = "username";
    private static final String TRAINING_NAME = "training name";
    private static final String ADDRESS = "address";
    private static final String SPECIALIZATION = "specialization";
    private static final String NEW_SPECIALIZATION = "new specialization";
    private static final LocalDate DATE_OF_BIRTH = LocalDate.of(1990, 1, 1);
    private static final LocalDate TRAINING_DATE = LocalDate.of(2026, 2, 10);
    private static final CreateTrainerDto CREATE_TRAINER_DTO = CreateTrainerDtoMother.get(
        FIRSTNAME, LASTNAME, SPECIALIZATION);
    private static final UpdateTrainerDto UPDATE_TRAINER_DTO =
        UpdateTrainerDtoMother.get(TRAINEE_UID, NEW_SPECIALIZATION);
    private static final CreateTraineeDto CREATE_TRAINEE_DTO =
        CreateTraineeDtoMother.get(FIRSTNAME, LASTNAME, ADDRESS, DATE_OF_BIRTH);
    private static final UpdateTraineeDto UPDATE_TRAINEE_DTO =
        UpdateTraineeDtoMother.get(TRAINEE_UID, ADDRESS);
    private static final CreateTrainingDto CREATE_TRAINING_DTO =
        CreateTrainingDtoMother.get(TRAINEE_UID, TRAINER_UID, TRAINING_NAME ,TRAINING_DATE);
    private static final Trainee TRAINEE =
        TraineeMother.get(TRAINEE_UID, FIRSTNAME, LASTNAME, USERNAME);
    private static final Trainer TRAINER =
        TrainerMother.get(TRAINER_UID, FIRSTNAME, LASTNAME, USERNAME);
    private static final Training TRAINING =
        TrainingMother.get(TRAINING_UID, TRAINING_DATE);

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
