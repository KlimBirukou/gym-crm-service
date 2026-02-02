package com.epam.gym.validator.composite.training;

import com.epam.gym.domain.training.TrainingType;
import com.epam.gym.service.training.dto.CreateTrainingDto;
import com.epam.gym.validator.IValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CreateTrainingValidatorTest {

    private static final LocalDate DATE = LocalDate.of(2026, 1, 1);
    private final static UUID TRAINEE_UID = UUID.randomUUID();
    private final static UUID TRAINER_UID = UUID.randomUUID();
    private final static CreateTrainingDto CREATE_TRAINING_DTO = new CreateTrainingDto(
        TRAINEE_UID,
        TRAINER_UID,
        "name",
        TrainingType.CARDIO,
        DATE,
        Duration.ZERO
    );

    @Mock
    private IValidator<LocalDate> trainingDateValidator;
    @Mock
    private IValidator<UUID> traineePersistenceValidator;
    @Mock
    private IValidator<UUID> trainerPersistenceValidator;
    @Mock
    private IValidator<CreateTrainingDto> userAvailabilityOnDateValidator;

    private CreateTrainingValidator testObject;

    @BeforeEach
    void setUp() {
        testObject = new CreateTrainingValidator(
            trainingDateValidator,
            traineePersistenceValidator,
            trainerPersistenceValidator,
            userAvailabilityOnDateValidator
        );
        testObject.setUpValidator();
    }

    @Test
    void validate_shouldPass_whenAllValidatorsSuccessful() {
        doNothing().when(trainingDateValidator)
            .validate(DATE);
        doNothing().when(traineePersistenceValidator)
            .validate(TRAINEE_UID);
        doNothing().when(trainerPersistenceValidator)
            .validate(TRAINER_UID);
        doNothing().when(userAvailabilityOnDateValidator)
            .validate(CREATE_TRAINING_DTO);

        assertDoesNotThrow(() -> testObject.validate(CREATE_TRAINING_DTO));

        var order = inOrder(
            trainingDateValidator,
            traineePersistenceValidator,
            trainerPersistenceValidator,
            userAvailabilityOnDateValidator
        );

        order.verify(trainingDateValidator, times(1))
            .validate(DATE);
        order.verify(traineePersistenceValidator, times(1))
            .validate(TRAINEE_UID);
        order.verify(trainerPersistenceValidator, times(1))
            .validate(TRAINER_UID);
        order.verify(userAvailabilityOnDateValidator, times(1))
            .validate(CREATE_TRAINING_DTO);
    }

    @Test
    void validate_shouldThrow_whenTrainingDateValidatorFailed() {
        doThrow(new RuntimeException()).when(trainingDateValidator)
            .validate(DATE);

        assertThrows(RuntimeException.class,
            () -> testObject.validate(CREATE_TRAINING_DTO));

        verify(traineePersistenceValidator, never())
            .validate(TRAINEE_UID);
        verify(trainerPersistenceValidator, never())
            .validate(TRAINER_UID);
        verify(userAvailabilityOnDateValidator, never())
            .validate(CREATE_TRAINING_DTO);
    }

    @Test
    void validate_shouldThrow_whenTraineePersistenceValidatorFailed() {
        doNothing().when(trainingDateValidator)
            .validate(DATE);
        doThrow(new RuntimeException()).when(traineePersistenceValidator)
            .validate(TRAINEE_UID);

        assertThrows(RuntimeException.class,
            () -> testObject.validate(CREATE_TRAINING_DTO));

        verify(trainerPersistenceValidator, never())
            .validate(TRAINER_UID);
        verify(userAvailabilityOnDateValidator, never())
            .validate(CREATE_TRAINING_DTO);
    }

    @Test
    void validate_shouldThrow_whenTrainerPersistenceValidatorFailed() {
        doNothing().when(trainingDateValidator)
            .validate(DATE);
        doNothing().when(traineePersistenceValidator)
            .validate(TRAINEE_UID);
        doThrow(new RuntimeException()).when(trainerPersistenceValidator)
            .validate(TRAINER_UID);

        assertThrows(RuntimeException.class,
            () -> testObject.validate(CREATE_TRAINING_DTO));

        verify(userAvailabilityOnDateValidator, never())
            .validate(CREATE_TRAINING_DTO);
    }

    @Test
    void validate_shouldThrow_whenUserAvailabilityOnDateValidatorFailed() {
        doNothing().when(trainingDateValidator)
            .validate(DATE);
        doNothing().when(traineePersistenceValidator)
            .validate(TRAINEE_UID);
        doNothing().when(trainerPersistenceValidator)
            .validate(TRAINER_UID);
        doThrow(new RuntimeException()).when(userAvailabilityOnDateValidator)
            .validate(CREATE_TRAINING_DTO);

        assertThrows(RuntimeException.class,
            () -> testObject.validate(CREATE_TRAINING_DTO));
    }

    @ParameterizedTest
    @NullSource
    void validate_shouldThrowException_whenDataNull(CreateTrainingDto dto) {
        assertThrows(NullPointerException.class,
            () -> testObject.validate(dto));
    }
}
