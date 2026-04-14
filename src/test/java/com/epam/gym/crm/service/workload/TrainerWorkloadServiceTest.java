package com.epam.gym.crm.service.workload;

import com.epam.gym.crm.client.workload.ActionType;
import com.epam.gym.crm.client.workload.notifier.ITrainerWorkloadNotifier;
import com.epam.gym.crm.domain.training.Training;
import com.epam.gym.crm.domain.user.Trainer;
import com.epam.gym.crm.service.trainer.ITrainerService;
import com.epam.gym.crm.service.training.ITrainingService;
import com.epam.gym.crm.service.training.dto.TraineeTrainingsDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class TrainerWorkloadServiceTest {

    private static final String TRAINEE_USERNAME = "trainee_username";
    private static final String TRAINER_USERNAME_1 = "trainer_username_1";
    private static final String TRAINER_USERNAME_2 = "trainer_username_2";
    private static final UUID TRAINER_UID_1 = UUID.fromString("a1b2c3d4-e5f6-7890-abcd-ef1234567890");
    private static final UUID TRAINER_UID_2 = UUID.fromString("b2c3d4e5-f6a7-8901-bcde-f12345678901");
    private static final ZoneId ZONE = ZoneId.systemDefault();
    private static final LocalDate TODAY = LocalDate.of(2025, 6, 15);
    private static final Instant TODAY_INSTANT = TODAY.atStartOfDay(ZONE).toInstant();

    @Mock
    private ITrainingService trainingService;
    @Mock
    private ITrainerService trainerService;
    @Mock
    private Clock clock;
    @Mock
    private ITrainerWorkloadNotifier workloadService;

    @InjectMocks
    private TrainerWorkloadService testObject;

    @BeforeEach
    void setUp() {
        lenient().doReturn(TODAY_INSTANT).when(clock).instant();
        lenient().doReturn(ZONE).when(clock).getZone();
    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(trainingService, trainerService, workloadService);
    }

    @Test
    void processMany_shouldNotifyForEachTraining_whenSingleTrainingExists() {
        var dto = buildTraineeTrainingsDto();
        var training = buildTraining(TRAINER_UID_1);
        var trainer = buildTrainer(TRAINER_UID_1, TRAINER_USERNAME_1);
        doReturn(List.of(training)).when(trainingService).getTraineeTrainings(dto);
        doReturn(List.of(trainer)).when(trainerService).getByUids(List.of(TRAINER_UID_1));

        testObject.processMany(TRAINEE_USERNAME);

        verify(trainingService).getTraineeTrainings(dto);
        verify(trainerService).getByUids(List.of(TRAINER_UID_1));
        verify(workloadService).notify(training, TRAINER_USERNAME_1, ActionType.DELETE);
    }

    @Test
    void processMany_shouldNotifyWithCorrectTrainer_whenMultipleTrainingsWithDifferentTrainersExist() {
        var dto = buildTraineeTrainingsDto();
        var training1 = buildTraining(TRAINER_UID_1);
        var training2 = buildTraining(TRAINER_UID_2);
        var trainer1 = buildTrainer(TRAINER_UID_1, TRAINER_USERNAME_1);
        var trainer2 = buildTrainer(TRAINER_UID_2, TRAINER_USERNAME_2);
        doReturn(List.of(training1, training2)).when(trainingService).getTraineeTrainings(dto);
        doReturn(List.of(trainer1, trainer2)).when(trainerService).getByUids(List.of(TRAINER_UID_1, TRAINER_UID_2));

        testObject.processMany(TRAINEE_USERNAME);

        verify(trainingService).getTraineeTrainings(dto);
        verify(trainerService).getByUids(List.of(TRAINER_UID_1, TRAINER_UID_2));
        verify(workloadService).notify(training1, TRAINER_USERNAME_1, ActionType.DELETE);
        verify(workloadService).notify(training2, TRAINER_USERNAME_2, ActionType.DELETE);
    }

    @Test
    void processMany_shouldFetchTrainerOnlyOnce_whenMultipleTrainingsShareSameTrainer() {
        var training1 = buildTraining(TRAINER_UID_1);
        var training2 = buildTraining(TRAINER_UID_1);
        var dto = buildTraineeTrainingsDto();
        var trainer = buildTrainer(TRAINER_UID_1, TRAINER_USERNAME_1);
        doReturn(List.of(training1, training2)).when(trainingService).getTraineeTrainings(dto);
        doReturn(List.of(trainer)).when(trainerService).getByUids(List.of(TRAINER_UID_1));

        testObject.processMany(TRAINEE_USERNAME);

        verify(trainingService).getTraineeTrainings(dto);
        verify(trainerService, times(1)).getByUids(List.of(TRAINER_UID_1));
        verify(workloadService, times(2))
            .notify(any(), eq(TRAINER_USERNAME_1), eq(ActionType.DELETE));
    }

    @Test
    void processMany_shouldNotInteractWithTrainerServiceOrNotifier_whenNoTrainingsExist() {
        var dto = buildTraineeTrainingsDto();
        doReturn(List.of()).when(trainingService).getTraineeTrainings(dto);

        testObject.processMany(TRAINEE_USERNAME);

        verify(trainingService).getTraineeTrainings(dto);
        verifyNoInteractions(trainerService, workloadService);
    }

    @ParameterizedTest
    @NullSource
    void processMany_shouldThrowNullPointerException_whenUsernameIsNull(String username) {
        assertThrows(NullPointerException.class, () -> testObject.processMany(username));
    }

    private static TraineeTrainingsDto buildTraineeTrainingsDto() {
        return TraineeTrainingsDto.builder()
            .username(TRAINEE_USERNAME)
            .from(TODAY)
            .build();
    }

    private static Training buildTraining(UUID trainerUid) {
        return Training.builder()
            .trainerUid(trainerUid)
            .build();
    }

    private static Trainer buildTrainer(UUID uid, String username) {
        return Trainer.builder()
            .uid(uid)
            .username(username)
            .build();
    }
}
