package com.epam.gym.validator.single.training;

import com.epam.gym.domain.training.Training;
import com.epam.gym.exception.DomainNotFoundException;
import com.epam.gym.repository.training.ITrainingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TraineeHasTrainingValidatorTest {

    private final static UUID UID = UUID.randomUUID();
    private final static Training TRAINING = new Training();

    @Mock
    private ITrainingRepository trainingRepository;

    @InjectMocks
    private TraineeHasTrainingValidator testObject;

    @Test
    void validate_shouldPass_whenTraineeHasTraining() {
        when(trainingRepository.findByTraineeUid(UID))
            .thenReturn(List.of(TRAINING));

        assertDoesNotThrow(() -> testObject.validate(UID));
    }

    @Test
    void validate_shouldThrowException_whenTraineeHasNotTraining() {
        when(trainingRepository.findByTraineeUid(UID))
            .thenReturn(List.of());

        assertThrows(DomainNotFoundException.class,
            () -> testObject.validate(UID));
    }

    @ParameterizedTest
    @NullSource
    void validate_shouldThrowException_whenDataNull(UUID uid) {
        assertThrows(NullPointerException.class,
            () -> testObject.validate(uid));
    }
}
