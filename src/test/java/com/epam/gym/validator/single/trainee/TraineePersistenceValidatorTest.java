package com.epam.gym.validator.single.trainee;

import com.epam.gym.exception.DomainNotFoundException;
import com.epam.gym.repository.user.trainee.ITraineeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class TraineePersistenceValidatorTest {

    private final static UUID UID = UUID.randomUUID();

    @Mock
    private ITraineeRepository traineeRepository;

    @InjectMocks
    private TraineePersistenceValidator testObject;

    @Test
    void validate_shouldPass_whenTraineeExist() {
        doReturn(Boolean.TRUE).when(traineeRepository)
            .existByUid(UID);

        assertDoesNotThrow(() -> testObject.validate(UID));
    }

    @Test
    void validate_shouldThrowException_whenTraineeNotExist() {
        doReturn(Boolean.FALSE).when(traineeRepository)
            .existByUid(UID);

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
