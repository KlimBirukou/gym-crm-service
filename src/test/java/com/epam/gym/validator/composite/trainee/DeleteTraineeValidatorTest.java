package com.epam.gym.validator.composite.trainee;

import com.epam.gym.validator.IValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
class DeleteTraineeValidatorTest {

    private static final UUID UID = UUID.randomUUID();

    @Mock
    private IValidator<UUID> traineePersistenceValidator;
    @Mock
    private IValidator<UUID> traineeHasTrainingValidator;

    private DeleteTraineeValidator testObject;

    @BeforeEach
    void setUp () {
        testObject = new DeleteTraineeValidator(
            traineePersistenceValidator,
            traineeHasTrainingValidator
        );
        testObject.setUpValidator();
    }

    @Test
    void validate_shouldPass_whenAllValidatorsSuccessful() {
        doNothing().when(traineePersistenceValidator).validate(UID);
        doNothing().when(traineeHasTrainingValidator).validate(UID);

        assertDoesNotThrow(() -> testObject.validate(UID));

        var order = inOrder(
            traineePersistenceValidator,
            traineeHasTrainingValidator
        );

        order.verify(traineePersistenceValidator, times(1))
            .validate(UID);
        order.verify(traineeHasTrainingValidator, times(1))
            .validate(UID);
    }

    @Test
    void validate_shouldThrow_whenTraineePersistenceValidatorFailed() {
        doThrow(new RuntimeException()).when(traineePersistenceValidator)
            .validate(UID);

        assertThrows(RuntimeException.class,
            () -> testObject.validate(UID));

        verify(traineeHasTrainingValidator, never())
            .validate(UID);
    }

    @Test
    void validate_shouldThrow_whenTraineeHasTrainingValidatorFailed() {
        doNothing().when(traineePersistenceValidator)
            .validate(UID);
        doThrow(new RuntimeException()).when(traineeHasTrainingValidator)
            .validate(UID);

        assertThrows(RuntimeException.class,
            () -> testObject.validate(UID));
    }

    @ParameterizedTest
    @NullSource
    void validate_shouldThrowException_whenDataNull(UUID uid) {
        assertThrows(NullPointerException.class,
            () -> testObject.validate(uid));
    }
}
