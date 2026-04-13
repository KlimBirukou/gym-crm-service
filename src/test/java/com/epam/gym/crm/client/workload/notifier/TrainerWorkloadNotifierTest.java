package com.epam.gym.crm.client.workload.notifier;

import com.epam.gym.crm.client.workload.ActionType;
import com.epam.gym.crm.client.workload.IWorkloadClient;
import com.epam.gym.crm.client.workload.TrainerWorkloadRequest;
import com.epam.gym.crm.domain.training.Training;
import com.epam.gym.crm.exception.server.WorkloadServerUnavailableException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class TrainerWorkloadNotifierTest {

    private static final String TRAINER_USERNAME = "trainer_username";
    private static final ActionType ACTION_TYPE = ActionType.ADD;
    private static final LocalDate DATE = LocalDate.of(2026, 4, 15);
    private static final int DURATION_MINUTES = 60;
    private static final Duration DURATION = Duration.ofMinutes(DURATION_MINUTES);

    @Mock
    private IWorkloadClient workloadClient;

    @InjectMocks
    private TrainerWorkloadNotifier testObject;

    @Captor
    private ArgumentCaptor<TrainerWorkloadRequest> trainerWorkloadRequestCaptor;

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(workloadClient);
    }

    @Test
    void notify_shouldBuildRequestAndDelegateToClient() {
        var training = buildTraining();

        testObject.notify(training, TRAINER_USERNAME, ACTION_TYPE);

        verify(workloadClient).updateWorkload(trainerWorkloadRequestCaptor.capture());
        var notified = trainerWorkloadRequestCaptor.getValue();

        assertEquals(TRAINER_USERNAME, notified.trainerUsername());
        assertEquals(DATE, notified.trainingDate());
        assertEquals(DURATION_MINUTES, notified.trainingDuration());
        assertEquals(ACTION_TYPE, notified.actionType());
    }

    @Test
    void notifyFallback_shouldThrowWorkloadServerUnavailableException() {
        var training = buildTraining();
        var throwable = new RuntimeException("Error");

        assertThrows(WorkloadServerUnavailableException.class,
            () -> testObject.notifyFallback(training, TRAINER_USERNAME, ACTION_TYPE, throwable));
    }

    private static Training buildTraining() {
        return Training.builder()
            .date(DATE)
            .duration(DURATION)
            .build();
    }
}
